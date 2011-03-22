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
   * URI which we can redirect to post upload.
   */
  private static final String URI_OK = "/ok";

  /**
   * Datastore kind representing raw file assets.
   */
  private static final String KIND_ASSET = "Asset";

  /**
   * Datastore property for storing file contents.
   */

  private static final String PROPERTY_CONTENT = "content";

  /**
   * Datastore property for storing file MIME Type.
   */
  private static final String PROPERTY_MIME_TYPE = "mimeType";

  /**
   * Datastore property for storing the BlobKey.
   */
  private static final String PROPERTY_BLOBKEY = "blobkey";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    String filename = lastPathComponent(uri);
    Log.info("doGet(" + filename + ")");

    if (uri.equals(URI_OK)) {
      Log.info("ok");
      return;
    }

    // User has requested upload URL
    if (uri.endsWith(FileManagerConstants.REQUEST_BLOBSTORE_UPLOAD_URL)) {
      Log.info("request upload url");
      BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
      String url = bs.createUploadUrl(URI_OK);
      resp.getWriter().println(url);
      return;
    }

    // Serve the requested asset
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Entity entity = ds.get(KeyFactory.createKey(KIND_ASSET, filename));
      Blob data = (Blob) entity.getProperty(PROPERTY_CONTENT);
      String mimeType = (String) entity.getProperty(PROPERTY_MIME_TYPE);
      BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
      BlobKey blobkey = (BlobKey) entity.getProperty(PROPERTY_BLOBKEY);
      bs.serve(blobkey, resp);
    } catch (EntityNotFoundException e) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
  }


  private String lastPathComponent(String uri) {
    int pos = uri.lastIndexOf('/');
    return (pos == -1) ? uri : uri.substring(pos + 1);
  }


  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    Log.info("Trying blobstore service...");
    BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
    try {
      Map<String, BlobKey> blobs;
      try {
        blobs = bs.getUploadedBlobs(req);
      } catch (IllegalStateException ignore) {
        // User posted directly to us instead of requesting an upload URL
        String url = bs.createUploadUrl(URI_OK);
        resp.sendRedirect(url);
        return;
      }

      // User posted to blobstore and got redirect here
      if (!blobs.isEmpty()) {
        BlobKey blobKey = blobs.get("myFile");
        for (Entry<String, BlobKey> entry : blobs.entrySet()) {
          Log.info("Got blob:");
          BlobInfo info = new BlobInfoFactory().loadBlobInfo(entry.getValue());
          Log.info("- blobkey: " + info.getBlobKey());
          Log.info("- content type: " + info.getContentType());
          Log.info("- filename: " + info.getFilename());
          Log.info("- size: " + info.getSize());
          Log.info("- creation: " + info.getCreation());

          // Persist the asset information to the datastore
          Entity entity = new Entity(KIND_ASSET, info.getFilename());
          entity.setProperty(PROPERTY_MIME_TYPE, info.getContentType());
          entity.setProperty(PROPERTY_BLOBKEY, info.getBlobKey());
          DatastoreServiceFactory.getDatastoreService().put(entity);
          Log.info("datastore.put(" + info.getFilename() + ")");
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new ServletException(ex);
    }
  }
}
