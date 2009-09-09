package delay;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DelayServlet extends HttpServlet {
  private static final String CONTENT_TEXT = "Hello, world";
  private static final String ETAG = "" + CONTENT_TEXT.hashCode();
  private static final long LAST_MODIFIED_TIME_MILLIS = System.currentTimeMillis();
  private static final long ONE_YEAR_MILLIS = 365 * 24 * 60 * 60 * 1000;

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      // If request takes >5s to return it wasn't cached
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String qs = req.getQueryString();
    if (qs != null && qs.contains("etag")) {
      resp.setHeader("Etag", ETAG);
    } else {
      resp.setDateHeader("Last-Modified", LAST_MODIFIED_TIME_MILLIS);
    }
    resp.setDateHeader("Expires", System.currentTimeMillis() + ONE_YEAR_MILLIS);
    resp.setHeader("Cache-Control", "public, max-age=" + ONE_YEAR_MILLIS);
    resp.setContentType("text/plain");
    //    resp.setContentLength(CONTENT_TEXT.length());
    resp.setIntHeader("Content-Length", CONTENT_TEXT.length());
    resp.getWriter().print(CONTENT_TEXT);
  }
}
