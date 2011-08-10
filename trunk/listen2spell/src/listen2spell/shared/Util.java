package listen2spell.shared;

public class Util {

  private static final String SPACES = "---------------------------------------";

  public static String rightPadDashes(String txt, int len) {
    return txt + trim(SPACES, len - txt.length());
  }

  public static String trim(String txt, int len) {
    len = Math.max(len, 0);
    return txt.length() > len ? txt.substring(0, len) : txt;
  }

}
