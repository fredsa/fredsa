package highscores.shared;

/**
 * A simple exception class, used to communicate server side problems to the client.
 */
@SuppressWarnings("serial")
public class HighscoreException extends Exception {

  /**
   * Default constructor for GWT RPC.
   */
  @SuppressWarnings("unused")
  private HighscoreException() {
    // TODO Auto-generated constructor stub
  }

  public HighscoreException(String msg) {
    super(msg);
  }
}
