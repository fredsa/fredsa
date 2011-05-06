package echo;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class EchoServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(EchoServlet.class.getName());
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");

    @SuppressWarnings("unchecked")
    Enumeration<String> names = request.getHeaderNames();

    while (names.hasMoreElements()) {
      String name = names.nextElement();
      String value = request.getHeader(name);
      String msg = name + ": " + value;
      resp.getWriter().println(msg);
      logger.info(msg);
    }

  }
}
