package highscores.shared.dto;

import highscores.shared.dto.Pojo.ToStringBuilder.GetKeyNameBuilder;

/**
 * POJO representing a single level completion.
 */
@SuppressWarnings("serial")
public class Achievement extends Pojo {

  public static final String KIND = "Achievement";

  private static final String LEVEL = "level";

  public static final String INITIALS = "initials";

  public static final String POINTS = "points";

  private static final String SECONDS_TO_COMPLETE = "secondsToComplete";

  /**
   * Default constructor for GWT RPC.
   */
  @SuppressWarnings("unused")
  private Achievement() {
    // TODO Auto-generated constructor stub
  }

  public Achievement(Level level, String initials, long points, long secondsToComplete) {
    assert initials != null;
    assert initials.indexOf('/') == -1;

    this.level = level;
    this.setInitials(initials);
    this.setPoints(points);
    this.secondsToComplete = secondsToComplete;
  }

  private String initials;
  private long points;
  long secondsToComplete;
  private Level level;

  public Level getLevel() {
    return level;
  }

  public String getKeyName() {
    return new GetKeyNameBuilder().prop(getPoints(), 7).prop(secondsToComplete, 8).prop(
        getInitials()).build();
  }

  @Override
  public String kind() {
    return KIND;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(KIND).prop(LEVEL, level.toString()).prop(INITIALS, getInitials()).prop(
        POINTS, getPoints()).prop(SECONDS_TO_COMPLETE, secondsToComplete).build();
  }

  public void setInitials(String initials) {
    this.initials = initials;
  }

  public String getInitials() {
    return initials;
  }

  public void setPoints(long points) {
    this.points = points;
  }

  public long getPoints() {
    return points;
  }
}
