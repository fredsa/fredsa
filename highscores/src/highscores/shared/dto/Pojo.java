package highscores.shared.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenient base class which provides formatting and other conveniences to
 * POJO subclasses.
 */
@SuppressWarnings("serial")
public abstract class Pojo implements Serializable {

  public Map<String, String> props = new HashMap<String, String>();

  abstract String getKeyName();

  public abstract String kind();

  /**
   * Helper class for use in {@link #toString()} methods.
   */
  protected static class ToStringBuilder {
    StringBuffer buf = new StringBuffer();
    long propCount;

    public ToStringBuilder(String kind) {
      buf.append(kind).append('(');
    }

    public ToStringBuilder prop(String name, String value) {
      maybeDelimiter();
      buf.append(name).append('=').append(value);
      return this;
    }

    private void maybeDelimiter() {
      if (++propCount > 1)
        buf.append(',');
    }

    public String build() {
      buf.append(')');
      return buf.toString();
    }

    public ToStringBuilder prop(String name, long value) {
      return prop(name, "" + value);
    }

    public ToStringBuilder prop(String name, float value) {
      return prop(name, "" + value);
    }

    /**
     * Helper class for use in {@link #getKeyName} methods.
     */
    protected static class GetKeyNameBuilder {
      StringBuffer buf = new StringBuffer();

      public GetKeyNameBuilder() {
      }

      public GetKeyNameBuilder prop(String value) {
        maybeDelimiter();
        buf.append(value);
        return this;
      }

      private void maybeDelimiter() {
        if (buf.length() > 0) {
          buf.append(',');
        }
      }

      public String build() {
        return buf.toString();
      }

      public GetKeyNameBuilder prop(long value, int digits) {
        maybeDelimiter();
        String v = Long.toString(value);
        int len = v.length();
        if (len > digits) {
          throw new RuntimeException(value + " exceeds maximum of " + digits + " digigts");
        }
        for (int i = digits - len; i > 0; i--) {
          buf.append('0');
        }
        buf.append(v);
        return this;
      }
    }

  }
}
