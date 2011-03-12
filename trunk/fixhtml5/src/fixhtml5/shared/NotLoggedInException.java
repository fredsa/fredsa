package fixhtml5.shared;

@SuppressWarnings("serial")
public class NotLoggedInException extends Exception {

  private String loginURL;

  // for GWT rpc
  @SuppressWarnings("unused")
  private NotLoggedInException() {
  }

  public NotLoggedInException(String loginURL) {
    this.loginURL = loginURL;
  }

  public String getLoginURL() {
    return loginURL;
  }

}
