package tajo.engine;

import org.junit.After;
import org.junit.Before;
import tajo.catalog.Schema;
import tajo.catalog.TCatUtil;
import tajo.catalog.TableMeta;
import tajo.catalog.proto.CatalogProtos.DataType;
import tajo.catalog.proto.CatalogProtos.StoreType;
import tajo.rpc.Callback;
import tajo.rpc.RemoteException;
import tajo.storage.CSVFile2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestNtaClient {
  NtaClient cli = null;

  private static TajoTestingUtility util;
  private static TajoMaster master;

  @Before
  public void setUp() throws Exception {
    util = new TajoTestingUtility();
    util.startMiniCluster(1);

    master = util.getMiniTajoCluster().getMaster();
    InetSocketAddress serverAddr = master.getRpcServerAddr();
    String ip = serverAddr.getAddress().getHostAddress();
    int port = serverAddr.getPort();
    cli = new NtaClient(ip, port);
    
    
  }

  @After
  public void tearDown() throws Exception {
    cli.close();
    util.shutdownMiniCluster();
  }

  // TODO - temporarily commented out
  public void testSubmit() throws IOException, InterruptedException, ExecutionException {
    try {
      String resultSetPath = cli.executeQuery("");
      System.out.println(resultSetPath);
      
      Callback<String> cb = new Callback<String>();
      cli.executeQueryAsync(cb, "");
      
      String s = (String)cb.get();
      System.out.println(s);
      
      Schema schema = new Schema();
      schema.addColumn("name", DataType.STRING);
      schema.addColumn("id", DataType.INT);

      TableMeta meta = TCatUtil.newTableMeta(schema, StoreType.CSV);
      meta.putOption(CSVFile2.DELIMITER, ",");

      cli.attachTable("attach1", "target/testdata/TestNtaClient/attach1");
      assertTrue(cli.existsTable("attach1"));

      cli.detachTable("attach1");
      assertFalse(cli.existsTable("attach1"));

    } catch (RemoteException e) {
      System.out.println(e.getMessage());
    }
    
  }
}
