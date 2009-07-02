package fredsa.blobs.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    int size = 0;
    String filename = "foo.dat";

    String id = req.getParameter("id");
    String len = req.getParameter("len");

    if (len != null) {
      size = Integer.parseInt(len);
      filename = "size-" + size + ".ext";
    } else {
      size = 0;
      PersistenceManager pm = PMF.get().getPersistenceManager();
      Query query = pm.newQuery(MyFile.class);
      query.setFilter("id == " + id);

      List<MyFile> results = (List<MyFile>) query.execute();
      MyFile file = results.get(0);
      List<MyFileChunk> chunks = file.getChunks();

      for (Iterator iterator = chunks.iterator(); iterator.hasNext();) {
        MyFileChunk chunk = (MyFileChunk) iterator.next();
        byte[] b = chunk.getBytes();
        size += b.length;
      }
      filename = "id-" + id + "-size-" + size + ".ext";
    }

    byte[] bytes = new byte[size];

    resp.setContentType("application/octet-stream");
    resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    resp.setHeader("Content-Length", Integer.toString(size));
    resp.getOutputStream().write(bytes, 0, bytes.length);
  }
}
