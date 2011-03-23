package filemanager.server;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    Log.info("Trying blobstore service...");
    BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
    try {
      Map<String, BlobKey> blobs;
      try {
        blobs = bs.getUploadedBlobs(req);
      } catch (IllegalStateException ignore) {
        // User posted file(s) directly to us instead of requesting an upload
        // URL
        String url = bs.createUploadUrl(URI_STORE_BLOB_INFO);
        resp.sendRedirect(url);
        return;
      }

      // User posted to blobstore upload URL and got redirected here
      if (!blobs.isEmpty()) {
        BlobKey oldBlobKey = null;
        for (Entry<String, BlobKey> entry : blobs.entrySet()) {
          Log.info("Got blob:");
          BlobInfo info = new BlobInfoFactory().loadBlobInfo(entry.getValue());
          Log.info("- blobkey: " + info.getBlobKey());
          Log.info("- content type: " + info.getContentType());
          Log.info("- filename: " + info.getFilename());
          Log.info("- size: " + info.getSize());
          Log.info("- creation: " + info.getCreation());

          // Make sure we don't orphan existing blobs
          DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
          Key key = KeyFactory.createKey(KIND_ASSET, info.getFilename());
          Entity entity;
          try {
            entity = ds.get(key);
            oldBlobKey = (BlobKey) entity.getProperty(PROPERTY_BLOBKEY);
            // found an existing entity with an existing blob
          } catch (EntityNotFoundException ignore) {
            // good news: no existing entity found, so no blob to orphan
          }

          // Persist the asset information to the datastore
          entity = new Entity(KIND_ASSET, info.getFilename());
          entity.setProperty(PROPERTY_BLOBKEY, info.getBlobKey());
          Log.info("datastore.put(" + info.getFilename() + ")...");
          DatastoreServiceFactory.getDatastoreService().put(entity);

          if (oldBlobKey != null) {
            Log.info("delete orphaned blob key: " + oldBlobKey);
            bs.delete(oldBlobKey);
          }
        }
        // We are required to send a redirect in response to a blobstore upload
        resp.sendRedirect(URI_UPLOAD_COMPLETE);
        return;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ServletException(ex);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String filename = lastPathComponent(uri);

    if (uri.equals(URI_UPLOAD_COMPLETE)) {
      resp.setContentType("text/plain");
      resp.getWriter().println("Assets have been uploaded.");
    }

    // if (uri.equals(URI_STORE_BLOB_INFO)) {
    // Log.info("ok");
    // return;
    // }

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
