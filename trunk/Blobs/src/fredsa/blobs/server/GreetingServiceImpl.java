package fredsa.blobs.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.blobs.client.GreetingService;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/*
 * * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
  private final static int MAX_BLOB_SIZE = 1000000;

  public String greetServer(String input) {
    int size = Integer.parseInt(input);
    PersistenceManager pm = PMF.get().getPersistenceManager();

    MyFile file = new MyFile();
    int sizeRemaining = size;
    while (sizeRemaining > MAX_BLOB_SIZE) {
      file.addChunk(makeChunk(1, MAX_BLOB_SIZE));
      size -= MAX_BLOB_SIZE;
    }
    file.addChunk(makeChunk(2, sizeRemaining));
    pm.makePersistent(file);

    Query query = pm.newQuery(MyFile.class);
    Long id = file.getId();
    query.setFilter("id == " + id);
    List<MyFile> results = (List<MyFile>) query.execute();

    file = results.get(0);
    String text = "";
    List<MyFileChunk> chunks = file.getChunks();
    for (Iterator iterator = chunks.iterator(); iterator.hasNext();) {
      MyFileChunk chunk = (MyFileChunk) iterator.next();
      byte[] bytes = chunk.getBytes();
      text += "|" + bytes[0] + " (" + bytes.length + ")";
    }
    return "<a href='/blobs/download?len=" + size + "&id=" + id + "'>" + id + ":" + text + "</a>";
  }

  private MyFileChunk makeChunk(int seq, int size) {
    byte[] bytes = new byte[size];
    bytes[0] = (byte) seq;

    MyFileChunk chunk = new MyFileChunk();
    chunk.setBytes(bytes);
    return chunk;
  }
}
