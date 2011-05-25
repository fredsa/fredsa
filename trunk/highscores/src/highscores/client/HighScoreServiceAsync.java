package highscores.client;

import java.util.List;

import highscores.shared.dto.Achievement;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HighScoreServiceAsync {
  void recordAchievement(Achievement achievement, AsyncCallback<List<Achievement>> callback);
}
