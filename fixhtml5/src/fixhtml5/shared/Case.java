package fixhtml5.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Case implements Serializable {
  private String key;
  private String name;
  private String html;
  private String script;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public String getHtml() {
    return html;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public String getScript() {
    return script;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  @Override
  public String toString() {
    return "Case[key=" + key + ",name=" + name + "]";
  }
}
