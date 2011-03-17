package blobupload;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class BlobuploadServlet extends HttpServlet {
  private static final int MAX_UPLOAD_FILE_SIZE = 500 * 1000; // 500KB

  private static final Logger log = Logger.getLogger(BlobuploadServlet.class.getName());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    resp.getWriter().println(
        "<html><body><form name=test method=post enctype='multipart/form-data'>"
            + "<input type=text name=mytext value='txt'>" + "<input type=file name=myfile>"
            + "<input type=file name=myfile2>" + "<input type=submit value='submit'>" + "</form>");
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException {
    try {
      ServletFileUpload upload = new ServletFileUpload();
      res.setContentType("text/plain");
      if (ServletFileUpload.isMultipartContent(req)) {
        res.getWriter().println("multipart");
      } else {
        res.getWriter().println("! multipart");
      }

      FileItemIterator iterator = upload.getItemIterator(req);
      while (iterator.hasNext()) {
        FileItemStream item = iterator.next();

        if (item.isFormField()) {
          log.info("Got a form field: " + item.getFieldName());
        } else {
          int len = copyFile(item);
          log.warning("Got an uploaded file: " + item.getFieldName() + ", name = " + item.getName()
              + ", length = " + len);
          res.getWriter().println("Stored file");
          log.info("stored file");
        }

      }
    } catch (Exception ex) {
      throw new ServletException(ex);
    }
  }

  private int copyFile(FileItemStream item) throws IOException {
    InputStream is = item.openStream();
    byte[] buffer = new byte[MAX_UPLOAD_FILE_SIZE];
    int offset = 0;
    int count;
    while ((count = is.read(buffer, offset, MAX_UPLOAD_FILE_SIZE - offset)) > 0) {
      offset += count;
    }

    if (is.read() != -1) {
      throw new RuntimeException("Uploaded file > " + MAX_UPLOAD_FILE_SIZE);
    }
    store(item.getName(), Arrays.copyOf(buffer, offset));
    return offset;
  }

  private void store(String filename, byte[] buffer) {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity userFile = new Entity("FileWithUUID", UUID.randomUUID().toString());
    Entity userFile2 = new Entity("FileDatastoreId");
    userFile.setProperty("filename", filename);
    userFile2.setProperty("filename", filename);
    userFile.setProperty("content", new Blob(buffer));
    userFile2.setProperty("content", new Blob(buffer));
    ds.put(userFile);
    ds.put(userFile2);
  }
}
