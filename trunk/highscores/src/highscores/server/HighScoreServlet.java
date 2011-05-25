package highscores.server;

import highscores.client.HighScoreService;
import highscores.shared.HighscoreException;
import highscores.shared.dto.Achievement;

import java.util.ConcurrentModificationException;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class HighScoreServlet extends RemoteServiceServlet implements HighScoreService {

  @Override
  public List<Achievement> recordAchievement(Achievement achievement) throws HighscoreException {
    try {
      HighScoreManager.writeAchievement(achievement);
      return HighScoreManager.getAchievements(achievement.getLevel());
    } catch(ConcurrentModificationException e) {
      Log.error("Unable to record achievement");
      throw new HighscoreException("Concurrent Modification; please try again with exponential backoff");
    } catch (Throwable e) {
      Log.error("Unable to record achievement", e);
      throw new HighscoreException("See server log for details");
    }
  }
}
