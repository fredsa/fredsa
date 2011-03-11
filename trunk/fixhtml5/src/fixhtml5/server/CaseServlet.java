package fixhtml5.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fixhtml5.client.CaseService;
import fixhtml5.shared.Case;

@SuppressWarnings("serial")
public class CaseServlet extends RemoteServiceServlet implements CaseService {

  private Case c;

  @Override
  public Case getCase(int number) {
    c = new Case();
    c.setName("Simple case");
    c.setHtml("<audio controls id=\"laser\" src=\"36846__EcoDTR__LaserRocket.mp3\"></audio><br/>"
        + "<button onclick=\"play('laser')\">play</button>"
        + "<div>Do not use the media controls. Instead, click on the 'play' button. Click several times, while the sound is playing."
        + "Note that the sound doesn't always play from the beginning. Sometimes it plays from somewhere in the middle.</div>");
    c.setScript("function play(id) {"
        //
        + "var e = document.getElementById(id);"
        + "try {e.pause();} catch(e) {alert(\"pause: \" + e);}"
        + "try {e.currentTime = 0;} catch(e) {alert(\"currentTime: \" + e);}"
        + "try {e.play();} catch(e) {alert(\"play: \" + e);}"
        //
        + "}");
    return c;
  }

}
