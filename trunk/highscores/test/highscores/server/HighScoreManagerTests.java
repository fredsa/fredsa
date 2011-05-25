// Copyright 2011 Google Inc. All Rights Reserved.

package highscores.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Tests for {@link HighScoreManager}.
 */
public class HighScoreManagerTests extends TestCase {

  public void testDifferentPlayerHigherAchievementIsAdded() throws Exception {
    List<String> initialsList = List("ABC");
    List<Long> pointsList = List(4200L);

    String newInitials = "XYZ";
    long newPoints = 4300;

    List<String> expectedinitialsList = List("XYZ", "ABC");
    List<Long> expectedPointsList = List(4300L, 4200L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testDifferentPlayerLowerAchievementIsAdded() throws Exception {
    List<String> initialsList = List("ABC");
    List<Long> pointsList = List(4200L);

    String newInitials = "XYZ";
    long newPoints = 4100;

    List<String> expectedinitialsList = List("ABC", "XYZ");
    List<Long> expectedPointsList = List(4200L, 4100L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testFirstAchievementIsAdded() throws Exception {
    List<String> initialsList = List();
    List<Long> pointsList = List();

    String newInitials = "ABC";
    long newPoints = 4200;

    List<String> expectedinitialsList = List("ABC");
    List<Long> expectedPointsList = List(4200L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testFullLeaderboardHighestAchievementIsInserted() throws Exception {
    List<String> initialsList = List("AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
    "JJJ");
    List<Long> pointsList = List(100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L, 10L);

    String newInitials = "ABC";
    long newPoints = 4200;

    List<String> expectedinitialsList = List("ABC","AAA", "BBB", "CCC", "DDD", "EEE", "FFF",
        "GGG", "HHH", "III");
    List<Long> expectedPointsList = List(4200L, 100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testFullLeaderboardLowestAchievementIsIgnored() throws Exception {
    List<String> initialsList = List("AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
    "JJJ");
    List<Long> pointsList = List(100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L, 10L);

    String newInitials = "ABC";
    long newPoints = 5;

    List<String> expectedinitialsList = List("AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG",
        "HHH", "III", "JJJ");
    List<Long> expectedPointsList = List(100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L, 10L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testFullLeaderboardMediumAchievementIsInserted() throws Exception {
    List<String> initialsList = List("AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
    "JJJ");
    List<Long> pointsList = List(100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L, 10L);

    String newInitials = "ABC";
    long newPoints = 42;

    List<String> expectedinitialsList = List("AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "ABC",
        "GGG", "HHH", "III");
    List<Long> expectedPointsList = List(100L, 90L, 80L, 70L, 60L, 50L, 42L, 40L, 30L, 20L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testSamePlayerHigherAchievementReplacesLowerAchievement() throws Exception {
    List<String> initialsList = List("ABC");
    List<Long> pointsList = List(4200L);

    String newInitials = "ABC";
    long newPoints = 4300;

    List<String> expectedinitialsList = List("ABC");
    List<Long> expectedPointsList = List(4300L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  public void testSamePlayerLowerAchievementIsIgnored() throws Exception {
    List<String> initialsList = List("ABC");
    List<Long> pointsList = List(4200L);

    String newInitials = "ABC";
    long newPoints = 4100;

    List<String> expectedinitialsList = List("ABC");
    List<Long> expectedPointsList = List(4200L);

    assertAndCallApi(initialsList, pointsList, newInitials, newPoints,
        expectedinitialsList, expectedPointsList);
  }

  private void assertAndCallApi(List<String> initialsList, List<Long> pointsList,
      String newInitials, Long newPoints, List<String> expectedinitialsList,
      List<Long> expectedPointsList) {

    // assert matching list sizes
    assertEquals(initialsList.size(), pointsList.size());
    assertEquals(expectedinitialsList.size(), expectedPointsList.size());

    // is the API expected to modify the input lists?
    boolean expectModified = !expectedinitialsList.equals(initialsList)
    || !expectedPointsList.equals(pointsList);

    // call the API
    boolean modified = HighScoreManager.updateTopScoresLists(initialsList, pointsList, newInitials,
        newPoints);

    // assert the outputs are correct
    assertEquals(expectModified, modified);
    assertEquals(expectedinitialsList, initialsList);
    assertEquals(expectedPointsList, pointsList);
  }

  private <T> List<T> List(T... args) {
    // wrap the ArrayList returned from Arrays.asList to make it resizabLe
    return new ArrayList<T>(Arrays.asList(args));
  }

}
