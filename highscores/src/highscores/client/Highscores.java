package highscores.client;

import highscores.shared.HighscoreException;
import highscores.shared.dto.Achievement;
import highscores.shared.dto.Level;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Highscores implements EntryPoint {

  private final HighScoreServiceAsync highScoreService = GWT.create(HighScoreService.class);

  @Override
  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();

    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        Log.debug("***************************************************************************");
        onModuleLoad2();
      }
    });
  }

  private void onModuleLoad2() {
    final Level level = new Level("Caveman", "Poached Eggs", 7);
    
    // Record a couple of fixed achievements
    send(new Achievement(level, "FMJ", 560, 300 * 1000));
    send(new Achievement(level, "AAS", 17203, (int) (17.5f * 1000)));

    // Record a few random achievements
    Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
      private int count;

      @Override
      public boolean execute() {
        Random rand = new Random();
        
        char c1 = (char) (rand.nextInt(26) + 65);
        char c2 = (char) (rand.nextInt(26) + 65);
        char c3 = (char) (rand.nextInt(26) + 65);
        String initials = "" + c1 + c2 + c3;

        int score = rand.nextInt(10000);
        
        int secondsToComplete = (rand.nextInt(15) + 10) * 1000;
        
        send(new Achievement(level, initials, score, secondsToComplete));
        
        return ++count < 10;
      }
    }, 10);

  }

  private void send(final Achievement achievement) {
    Log.debug("Sending achievement: " + achievement);

    highScoreService.recordAchievement(achievement, new AsyncCallback<List<Achievement>>() {
      @Override
      public void onSuccess(List<Achievement> achievements) {
        Log.debug("Achievement recorded successfully: " + achievement);
        Log.info("");
        Log.info("HIGH SCORES FOR " + achievement.getLevel());
        int count = 0;
        for (Iterator<Achievement> iterator = achievements.iterator(); iterator.hasNext();) {
          Achievement a = iterator.next();
          Log.info(" " + ++count + ") " + a.getInitials() + "  " + a.getPoints());
        }
      }

      @Override
      public void onFailure(Throwable e) {
        if (e instanceof HighscoreException) {
          Log.error("Failed to record achievement: " + e.getMessage());
        } else {
          Log.error("Failed to record achievement", e);
        }
      }

    });
  }
}
