package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.core.client.impl.Impl;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;

public class SqlService_Proxy extends RemoteServiceProxy implements sqlmapreduce.client.SqlServiceAsync {
  private static final String REMOTE_SERVICE_INTERFACE_NAME = "sqlmapreduce.client.SqlService";
  private static final String SERIALIZATION_POLICY ="E97AE4074400D118CF0F2FC271B24B86";
  private static final sqlmapreduce.client.SqlService_TypeSerializer SERIALIZER = new sqlmapreduce.client.SqlService_TypeSerializer();
  
  public SqlService_Proxy() {
    super(GWT.getModuleBaseURL(),
      "sql", 
      SERIALIZATION_POLICY, 
      SERIALIZER);
  }
  
  public void greetServer(java.lang.String input, com.google.gwt.user.client.rpc.AsyncCallback callback) {
    RpcStatsContext statsContext = new RpcStatsContext();
    boolean toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("SqlService_Proxy.greetServer", "begin"));
    SerializationStreamWriter streamWriter = createStreamWriter();
    // createStreamWriter() prepared the stream
    try {
      streamWriter.writeString(REMOTE_SERVICE_INTERFACE_NAME);
      streamWriter.writeString("greetServer");
      streamWriter.writeInt(1);
      streamWriter.writeString("java.lang.String/2004016611");
      streamWriter.writeString(input);
      String payload = streamWriter.toString();
      toss = statsContext.isStatsAvailable() && statsContext.stats(statsContext.timeStat("SqlService_Proxy.greetServer",  "requestSerialized"));
      doInvoke(ResponseReader.STRING, "SqlService_Proxy.greetServer", statsContext, payload, callback);
    } catch (SerializationException ex) {
      callback.onFailure(ex);
    }
  }
}
