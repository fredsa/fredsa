package listen2spell.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import listen2spell.shared.Spoken;

@SuppressWarnings("serial")
public class SpeechServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    String word = req.getParameter("q");

    if (word == null || word.trim().length() == 0) {
      resp.sendError(400); // bad request
      return;
    }
    Spoken spoken = Speech.getWord(word);
    resp.getOutputStream().write(spoken.getData());
  }
}
