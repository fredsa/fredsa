package highscores.client;

import highscores.shared.HighscoreException;
import highscores.shared.dto.Achievement;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("achievements")
public interface HighScoreService extends RemoteService {
  List<Achievement> recordAchievement(Achievement achievement) throws HighscoreException;
}
