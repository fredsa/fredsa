package highscores.shared.dto;

import highscores.shared.dto.Pojo.ToStringBuilder.GetKeyNameBuilder;

import java.util.List;

/**
 * POJO representing a game level.
 */
@SuppressWarnings("serial")
public class Level extends Pojo {

  public static final String KIND = "Level";

  private static final String CHAPTER = "chapter";

  private static final String LEVEL_NUMBER = "levelNumber";

  private static final String GAME = "game";

  public static final String TOP_POINTS = "topPoints";

  public static final String TOP_INITIALS = "topInitials";

  private String game;
  private String chapter;
  private long levelNumber;
  private List<Long> topPoints;
  private List<String> topInitials;

  /**
   * Default constructor for GWT RPC.
   */
  @SuppressWarnings("unused")
  private Level() {
    // TODO Auto-generated constructor stub
  }

  public Level(String game, String chapter, long levelNumber) {
    assert game != null;
    assert chapter != null;
    assert levelNumber > 0;
    this.game = game;
    this.chapter = chapter;
    this.levelNumber = levelNumber;
  }

  public Level(String game, String chapter, long levelNumber, List<Long> topPoints,
      List<String> topInitials) {
    this(game, chapter, levelNumber);
    assert topPoints != null;
    assert topInitials != null;
    this.topPoints = topPoints;
    this.topInitials = topInitials;
  }

  @Override
  public String getKeyName() {
    return new GetKeyNameBuilder().prop(game).prop(chapter).prop(levelNumber, 1).build();
  }

  @Override
  public String kind() {
    return KIND;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(KIND).prop(GAME, game).prop(CHAPTER, chapter).prop(
        LEVEL_NUMBER, levelNumber).build();
  }

  public void setTopPoints(List<Long> topPoints) {
    this.topPoints = topPoints;
  }

  public List<Long> getTopPoints() {
    return topPoints;
  }

  public void setTopInitials(List<String> topInitials) {
    this.topInitials = topInitials;
  }

  public List<String> getTopInitials() {
    return topInitials;
  }

}
