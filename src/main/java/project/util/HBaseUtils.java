package project.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * HBase操作工作累
 */
public class HBaseUtils {
    private HBaseAdmin admin = null;
    private Configuration configuration = null;

    private HBaseUtils() {
        configuration = new Configuration();
        configuration.set("hbase.zookeeper.quorum","master,slave1,slave2");
        configuration.set("hbase.rootdir","hdfs://master:9000/hbase");

        try {
            admin = new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance = null;

    /**
     * 单例模式，获取HBase实例
     * @return
     */
    public static HBaseUtils getInstance() {
        if (null == instance) {
            synchronized (HBaseUtils.class) {
                if (instance == null) {
                    instance = new HBaseUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 根据表明，获取HBase的HTabel实例
     * @param tableName
     * @return
     */
    public HTable getTable(String tableName) {
        HTable table = null;

        try {
            table = new HTable(configuration, tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return table;
    }

    /**
     * 添加一条记录到HBase表
     * @param tableName HBase表名
     * @param rowkey    HBase的rowkey
     * @param cf    HBase表的column family
     * @param column    HBase表的列
     * @param value 将要写入HBase表的值
     */
    public void put(String tableName, String rowkey, String cf, String column, String value) {
        HTable table = getTable(tableName);

        Put put = new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(cf), Bytes.toBytes(column), Bytes.toBytes(value));

        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
