package highscores.server;

import highscores.shared.dto.Achievement;
import highscores.shared.dto.Level;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;

/**
 * Manage high scores
 */
public class HighScoreManager {

  private static final int MAX_SCORES = 10;

  /**
   * Record and achievement and update the parent level entity high scores.
   * 
   * TODO: add memcache TODO: cleanup
   */
  static Level writeAchievement(Achievement achievement) throws ConcurrentModificationException {
    Log.debug("write: " + achievement);

    // Determine datastore key for the level (=root entity)
    Level level = achievement.getLevel();
    Key levelKey = KeyFactory.createKey(Level.KIND, level.getKeyName());

    // Copy achievement POJO into entity
    Entity achievementEntity = new Entity(Achievement.KIND, achievement.getKeyName(), levelKey);
    achievementEntity.setProperty(Achievement.INITIALS, achievement.getInitials());
    achievementEntity.setProperty(Achievement.POINTS, achievement.getPoints());

    /*
     * Inside a transaction: 1) get the level entity (or create it if it did not exist), 2) write an
     * updated level entity and the provided child achievement entity.
     */
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Transaction txn = ds.beginTransaction();
    try {
      Entity levelEntity;
      try {
        levelEntity = ds.get(levelKey);
      } catch (EntityNotFoundException ignore) {
        // For now we automatically create missing levels
        levelEntity = new Entity(levelKey);
      }
      levelEntity.setProperty("lastUpdated", System.currentTimeMillis());

      // Update lists to reflect new top scores
      @SuppressWarnings("unchecked")
      List<String> initialsList = (List<String>) levelEntity.getProperty(Level.TOP_INITIALS);
      if (initialsList == null) {
        initialsList = new ArrayList<String>();
      }

      @SuppressWarnings("unchecked")
      List<Long> pointsList = (List<Long>) levelEntity.getProperty(Level.TOP_POINTS);
      if (pointsList == null) {
        pointsList = new ArrayList<Long>();
      }

      boolean modified = updateTopScoresLists(initialsList, pointsList, achievement.getInitials(),
          achievement.getPoints());
      if (modified) {
        levelEntity.setProperty(Level.TOP_INITIALS, initialsList);
        levelEntity.setProperty(Level.TOP_POINTS, pointsList);
      }

      // gather up entities for batch put
      ArrayList<Entity> entities = new ArrayList<Entity>();
      entities.add(levelEntity);
      entities.add(achievementEntity);

      ds.put(entities);

      // May throw ConcurrentModificationException
      txn.commit();

      // Return the modified level POJO
      level.setTopInitials(initialsList);
      level.setTopPoints(pointsList);
      return level;

    } finally {
      if (txn.isActive()) {
        txn.rollback();
      }
    }

  }

  /**
   * Determine new top scores, the hard way.
   * 
   * @throws EntityNotFoundException
   */
  @SuppressWarnings("unused")
  private static void requeryAndUpdateLevelTopScores(Key levelKey) throws EntityNotFoundException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity levelEntity = ds.get(levelKey);

    Query query = new Query(Achievement.KIND, levelKey).addSort("__key__",
        Query.SortDirection.DESCENDING);
    QueryResultList<Entity> achievements = ds.prepare(query).asQueryResultList(
        FetchOptions.Builder.withLimit(MAX_SCORES));
    List<String> initials = new ArrayList<String>();
    List<Long> points = new ArrayList<Long>();
    for (Iterator<Entity> iterator = achievements.iterator(); iterator.hasNext();) {
      Entity a = iterator.next();
      initials.add((String) a.getProperty(Achievement.INITIALS));
      points.add((Long) a.getProperty(Achievement.POINTS));
    }
    levelEntity.setProperty(Level.TOP_INITIALS, initials);
    levelEntity.setProperty(Level.TOP_POINTS, points);

    ds.put(levelEntity);
  }

  static boolean updateTopScoresLists(List<String> initialsList, List<Long> pointsList,
      String newInitials, long newPoints) {
    int numScores = pointsList.size();
    assert initialsList.size() == numScores;

    for (int i = 0; i < numScores; i++) {
      Long points = pointsList.get(i);
      if (newPoints > points) {
        // we bump everyone from here down
        pointsList.add(i, newPoints);
        initialsList.add(i, newInitials);

        // if player already had a lower score, remove it
        for (int j = i + 1; j < numScores + 1; j++) {
          if (newInitials.equals(initialsList.get(j))) {
            pointsList.remove(j);
            initialsList.remove(j);
            break;
          }
        }

        // remove overflowing score
        if (numScores +1 > MAX_SCORES) {
          pointsList.remove(MAX_SCORES);
          initialsList.remove(MAX_SCORES);
        }

        return true;
      }
      if (newInitials.equals(initialsList.get(i))) {
        // this player already has a better score, don't record this one
        return false;
      }
    }

    if (numScores < MAX_SCORES) {
      /*
       * While the achievement did not beat anyone else's score, there's fewer than max scores in
       * the list, so it's still a top score which needs to be added
       */
      pointsList.add(newPoints);
      initialsList.add(newInitials);
      return true;
    }

    return false;
  }

  /**
   * Extract the top scores from the list properties in the level entity.
   * 
   * TODO: add memcache
   */
  public static List<Achievement> getAchievements(Level level) {
    List<String> initials = level.getTopInitials();
    List<Long> points = level.getTopPoints();

    List<Achievement> achievements = new ArrayList<Achievement>();
    for (int i = 0; i < initials.size(); i++) {
      achievements.add(new Achievement(level, initials.get(i), points.get(i), 42));
    }
    return achievements;
  }
}
