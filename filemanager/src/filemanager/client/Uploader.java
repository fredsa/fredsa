package filemanager.client;
import com.allen_sauer.gwt.log.client.Log;

import filemanager.shared.FileManagerConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

public class Uploader {
  /**
   *
   */
  private static final int MAX_RETRIES = 5;

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      showUsage();
      return;
    }
    String baseUrl = args[0];
    if (baseUrl.endsWith("/")) {
      baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
    }
    Log.debug("Using base URL: " + baseUrl);

    String filename = args[1];
    Log.debug("Using filename: " + filename);


    String uploadUrl = getUploadUrl(baseUrl);
    Log.debug("Using upload URL: " + uploadUrl);

    int count = 0;
    boolean success = false;
    while (!success && ++count < MAX_RETRIES) {
      if (count > 1) {
        Log.info("ATTEMPT " + count + " of " + MAX_RETRIES + "....");
      }
      success = doUpload(baseUrl + uploadUrl, filename);
    }
  }


  private static boolean doUpload(String uploadUrl, String filename) throws IOException {
    HttpClient httpclient = new DefaultHttpClient();
    try {
      Log.info("Posting file: " + filename);
      File file = new File(filename);
      if (!file.canRead()) {
        Log.error("- File is not readable: " + filename);
        return false;
      }

      FileInputStream fileStream = new FileInputStream(file);

      // Determine MIME Type
      String mimeType =
          URLConnection.guessContentTypeFromStream(new BufferedInputStream(fileStream));
      Log.info("- MIME Type: " + mimeType);

      HttpPost httppost = new HttpPost(uploadUrl);
      FileBody fileBody = new FileBody(file, mimeType);

      MultipartEntity reqEntity = new MultipartEntity();
      reqEntity.addPart("file1", fileBody);
      // reqEntity.addPart("mimeType", new StringBody(mimeType));

      httppost.setEntity(reqEntity);

      Log.debug("Executing request " + httppost.getRequestLine());
      HttpResponse response = httpclient.execute(httppost);
      HttpEntity resEntity = response.getEntity();

      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != HttpServletResponse.SC_OK) {
        Log.error("Upload failed due to server response: " + response.getStatusLine());
        return false;
      }
      Log.info("Upload successful");
    } finally {
      try {
        httpclient.getConnectionManager().shutdown();
      } catch (Exception ignore) {
      }
    }
    return true;
  }

  private static String getUploadUrl(String baseUrl) throws IOException {
    URL url = new URL(baseUrl + FileManagerConstants.REQUEST_BLOBSTORE_UPLOAD_URL);
    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
    String uploadUrl = reader.readLine();
    return uploadUrl;
  }

  private static void showUsage() {
    System.err.println("java -jar uploader.jar <url> <file1> [file2] ...");
  }
}
