package JDBC;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Druid 数据库连接池工具类
 */
public class Druid {
    private static DataSource dataSource;//定义数据源变量
    static {
        // 加载配置文件和建立连接池
        try (InputStream resourceAsStream = Druid.class.getClassLoader().getResourceAsStream("Druid.properties")) {
            if (resourceAsStream == null) {
                throw new IOException("Druid.properties not found");
            }
            // 创建 Properties 对象，用于读写属性文件
            Properties pro = new Properties();
            // 从配置文件输入流中加载配置信息
            pro.load(resourceAsStream);
            // 使用 DruidDataSourceFactory 创建数据源
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            // 处理配置文件读取错误
            e.printStackTrace();
        } catch (Exception e) {
            // 处理其他异常，如 DruidDataSourceFactory.createDataSource 的异常
            e.printStackTrace();
        }
    }
    /**
     * 获取数据源
     *
     * @return 数据源对象
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
    /**
     * 获取连接池中的一个连接
     *
     * @return 数据库连接对象
     * @throws SQLException SQL 异常
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = dataSource.getConnection();
        return conn;
    }
    /**
     * 测试数据库连接池配置是否成功
     *
     * @param args 命令行参数
     * @throws SQLException SQL 异常
     */
    public static void main(String[] args) throws SQLException {
        // 获取 Druid 数据库连接池对象
        Connection conn = Druid.getConnection();
        // 运行 main 后，如果能在控制台显示下列字符串，则表示数据库连接池配置成功
        System.out.println("数据库连接池连接成功");
        conn.close();
    }
}
