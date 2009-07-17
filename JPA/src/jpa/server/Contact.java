package jpa.server;

import com.google.appengine.api.datastore.Key;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Contact {
  private String contextText;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Key key;

  public Contact() {
  }

  public Contact(String contextText) {
    this.contextText = contextText;
  }

  public Key getKey() {
    return key;
  }

}
