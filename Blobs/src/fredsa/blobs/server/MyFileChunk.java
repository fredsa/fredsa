package fredsa.blobs.server;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MyFileChunk {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  public Key getKey() {
    return key;
  }

  @Persistent
  private Blob blob;

  public void setBytes(byte[] bytes) {
    blob = new Blob(bytes);
  }

  public byte[] getBytes() {
    return blob.getBytes();
  }
}
