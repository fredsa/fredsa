package fixhtml5.shared;

public class NotLoggedInException extends Exception {

  private String loginURL;

  // for GWT rpc
  private NotLoggedInException() {
  }

  public NotLoggedInException(String loginURL) {
    this.loginURL = loginURL;
  }

  public String getLoginURL() {
    return loginURL;
  }

}
