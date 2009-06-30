package fredsa.blobs.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MyFile {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Persistent
  private String name;

  private List<MyFileChunk> fileChunks = new ArrayList<MyFileChunk>();

  public void addChunk(MyFileChunk chunk) {
    fileChunks.add(chunk);
  }

  public List<MyFileChunk> getChunks() {
    return fileChunks;
  }
}
