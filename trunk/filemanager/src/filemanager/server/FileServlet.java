package filemanager.server;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import com.allen_sauer.gwt.log.client.Log;

import filemanager.shared.FileManagerConstants;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {


  /**
   * Datastore kind representing raw file assets.
   */
  private static final String KIND_ASSET = "Asset";

  /**
   * Datastore property for storing the BlobKey.
   */
  private static final String PROPERTY_BLOBKEY = "blobkey";

  /**
   * URI which we can redirect to post upload.
   */
  private static final String URI_STORE_BLOB_INFO = "/store-blob-info";

  /**
   * URI which we redirect to upon successful upload.
   */
  private static final String URI_UPLOAD_COMPLETE = "/upload-complete";

  /**
   * URI used by task queue to guarantee deletion of orphaned blobs.
   */
  private static final String URI_DELETE_BLOB = "/delete-blob";

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
    String uri = req.getRequestURI();

    /**
     * Task queue based deletion of orphaned blobs.
     */
    if (uri.equals(URI_DELETE_BLOB)) {
      String blobKeyString = req.getParameter(PROPERTY_BLOBKEY);
      Log.debug("Delete orphaned blob with key: " + blobKeyString);
      bs.delete(new BlobKey(blobKeyString));
      return;
    }


    Map<String, BlobKey> blobs;
    try {
      /**
       * Ensure that this request is a result of POST to a blobstore upload URL.
       */
      try {
        blobs = bs.getUploadedBlobs(req);
      } catch (IllegalStateException ignore) {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
            "File uploads must POST to a blobstore service provided upload URL");
        return;
      }

      /**
       * Ensure that at least one file was uploaded.
       */
      if (blobs.isEmpty()) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing one or more uploaded files");
        return;
      }

      /**
       * We now have a valid upload request with at least one file available to
       * us in blobstore.
       */
      for (Entry<String, BlobKey> entry : blobs.entrySet()) {
        BlobInfo info = new BlobInfoFactory().loadBlobInfo(entry.getValue());
        Log.info("Got blob: blobkey=" + info.getBlobKey() + ", content type="
            + info.getContentType() + ", filename=" + info.getFilename() + ", size="
            + info.getSize() + ", creation=" + info.getCreation());

        // Persist the asset information to the datastore
        persistAsset(info);
      }
      // We are required to send a redirect in response to a blobstore upload
      resp.sendRedirect(URI_UPLOAD_COMPLETE);
      return;
    } catch (IOException ex) {
      throw new ServletException(ex);
    }
  }

  private void persistAsset(BlobInfo info) {
    String filename = info.getFilename();
    Log.info("Persisting asset: " + filename);
    Key key = KeyFactory.createKey(KIND_ASSET, filename);
    Entity entity = new Entity(key);
    entity.setProperty(PROPERTY_BLOBKEY, info.getBlobKey());

    BlobKey oldBlobKey = getBlobKey(key);
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    if (oldBlobKey == null) {
      /**
       * There is no existing blob which would be orphaned, so simply put()
       * asset.
       */
      Log.debug("write new asset entity for '" + filename + "'");
      ds.put(entity);
    } else {
      /**
       * Use of transactional task ensures that task is created if, and only if,
       * datastore transaction succeeds.
       */
      Log.debug("write new asset entity and (transactionally) request deletion of orphaned blob key: "
          + oldBlobKey);
      Queue queue = QueueFactory.getDefaultQueue();
      Transaction txn = ds.beginTransaction();
      ds.put(entity);
      queue.add(TaskOptions.Builder.withUrl(URI_DELETE_BLOB).param(PROPERTY_BLOBKEY,
          oldBlobKey.getKeyString()));
      txn.commit();
    }
  }

  /**
   * Get blob key for an existing asset.
   *
   * @param assetEntityKey the entity key for the asset
   * @return an existing blob key or null of the asset does not yet exist
   */
  private BlobKey getBlobKey(Key assetEntityKey) {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Entity entity = ds.get(assetEntityKey);
      return (BlobKey) entity.getProperty(PROPERTY_BLOBKEY);
    } catch (EntityNotFoundException ignore) {
      return null;
    }
  }

  /**
   * Serve assets from blobstore.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String filename = lastPathComponent(uri);

    if (uri.equals(URI_UPLOAD_COMPLETE)) {
      resp.setContentType("text/plain");
      resp.getWriter().println("Assets have been uploaded.");
    }

    // User has requested upload URL
    if (uri.endsWith(FileManagerConstants.REQUEST_BLOBSTORE_UPLOAD_URL)) {
      Log.info("Creating one-time use upload URL...");
      BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
      String url = bs.createUploadUrl(URI_STORE_BLOB_INFO);
      Log.info("- Upload URL: " + url);
      resp.getWriter().println(url);
      return;
    }

    // Serve the requested asset
    Log.info("Serving requested URI: " + uri);
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
      Entity entity = ds.get(KeyFactory.createKey(KIND_ASSET, filename));
      BlobKey blobkey = (BlobKey) entity.getProperty(PROPERTY_BLOBKEY);
      bs.serve(blobkey, resp);
    } catch (EntityNotFoundException e) {
      Log.info("Blob info not found. Sending " + HttpServletResponse.SC_NOT_FOUND);
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
  }

  private String lastPathComponent(String uri) {
    int pos = uri.lastIndexOf('/');
    return (pos == -1) ? uri : uri.substring(pos + 1);
  }
}
