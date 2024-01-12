package JDBC.member;

import JDBC.Druid;
import JDBC.member.member;
import com.example.fitness_club_management_system.TDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对会员进行增删改查
 */
public class memberDAO implements TDao<member,String> {
    private Connection connection;
    /**
     * 构造函数，接收一个Connection对象
     * @param connection 数据库连接对象
     */
    public memberDAO(Connection connection) {
        this.connection = connection;
    }
    // 创建表的SQL语句
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS member(" +
            "ID CHAR(20) PRIMARY KEY," +
            "Name VARCHAR(50) NOT NULL," +
            "Age int NOT NULL,"+
            "Sex CHAR(2) NOT NULL DEFAULT '男' CHECK(Sex in('男','女'))," +
            "Phone VARCHAR(20)," +
            "Email VARCHAR(50),"+
            "Address VARCHAR(100),"+
            "password VARCHAR(100) NOT NULL)";
    // 插入数据的SQL语句
    private static final String Insert = "INSERT INTO member(ID,Name,Age,Sex,Phone,Email,Address,password)" +
            "VALUES(?,?,?,?,?,?,?,?)";
    // 更新数据的SQL语句
    private static final String Update = "UPDATE member SET Name=?,Age=?,Sex=?,Phone=?,Email=?,Address=?,password=? WHERE ID=?";
    //根据指定的条件和更新参数更新数据库中的数据
    private static final String updateCondition = "UPDATE member SET ";
    // 删除数据的SQL语句
    private static final String Delete = "DELETE FROM member WHERE ID=?";
    // 根据ID查询数据的SQL语句
    private static final String SELECT = "SELECT * FROM member WHERE ID=?";
    // 查询所有数据的SQL语句
    private static final String SELECT_ALL = "SELECT * FROM member";
    // 根据条件查询数据的SQL语句
    private static final String SELECT_condition = "SELECT * FROM member WHERE ";
    // 分页查询数据的SQL语句
    private static final String SELECT_PAGE = "SELECT * FROM member Limit ?,?";
    /**
     * 获取数据库连接
     */
    private Connection getConnection() throws Exception {
        return Druid.getConnection();
    }
    /**
     * 创建表
     */
    public void createTabel() throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE);
        }
    }
    /**
     * 插入数据
     */
    @Override
    public void insert(member entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            statement.setString(1, entity.getID());
            statement.setString(2, entity.getName());
            statement.setInt(3,entity.getAge());
            statement.setString(4, entity.getSex());
            statement.setString(5, entity.getPhone());
            statement.setString(6, entity.getEmail());
            statement.setString(7, entity.getAddress());
            statement.setString(8, entity.getPassword());
            statement.executeUpdate();
        }
    }
    /**
     * 批量插入数据
     */
    @Override
    public void insertBatch(List<member> entities) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            for (member entity : entities) {
                statement.setString(1, entity.getID());
                statement.setString(2, entity.getName());
                statement.setInt(3,entity.getAge());
                statement.setString(4, entity.getSex());
                statement.setString(5, entity.getPhone());
                statement.setString(6, entity.getEmail());
                statement.setString(7, entity.getAddress());
                statement.setString(8, entity.getPassword());
            }
            statement.executeBatch();
        }
    }
    /**
     * 更新数据
     */
    @Override
    public void update(member entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Update)) {
            statement.setString(1, entity.getName());
            statement.setInt(2,entity.getAge());
            statement.setString(3, entity.getSex());
            statement.setString(4, entity.getPhone());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getAddress());
            statement.setString(7, entity.getPassword());
            statement.setString(8, entity.getID());
            statement.executeUpdate();
        }
    }
    /**
     * 根据指定的条件和更新参数更新数据库中的数据
     */
    @Override
    public void updateByCondition(String condition, String updateParams) throws Exception {
        String sql = updateCondition + updateParams + condition;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * 删除数据
     */
    @Override
    public void delete(String ID) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(Delete)) {
            statement.setString(1,ID);
            statement.executeUpdate();
        }
    }

    /**
     * 根据主键删除数据库中的多条数据
     */
    @Override
    public void deleteBatch(List<String> IDS) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(Delete)) {
            for (String ID : IDS) {
                statement.setString(1, ID);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    /**
     * 根据指定的条件删除数据库中的数据
     */
    @Override
    public void deleteByCondition(String condition) throws Exception {
        String sql = "DELETE FROM member WHERE " + condition;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * 根据ID查询数据
     */
    @Override
    public member selectById(String ID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT)) {
            statement.setString(1, ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetTomember(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * 查询所有数据
     */
    @Override
    public List<member> selectAll() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<member> members = new ArrayList<>();
            while (resultSet.next()) {
                members.add(mapResultSetTomember(resultSet));
            }
            return members;
        }
    }
    /**
     * 分页查询数据
     */
    @Override
    public List<member> selectByPage(int page, int pagesize) throws Exception {
        int a = ((page - 1) * pagesize);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PAGE)) {
            statement.setInt(1, a);
            statement.setInt(2, pagesize);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<member> members = new ArrayList<>();
                while (resultSet.next()) {
                    members.add(mapResultSetTomember(resultSet));
                }
                return members;
            }
        }
    }
    /**
     * 将ResultSet映射为member对象
     */
    private member mapResultSetTomember(ResultSet resultSet) throws SQLException {
        member member = new member();
        member.setID(resultSet.getString("ID"));
        member.setName(resultSet.getString("Name"));
        member.setAge(resultSet.getInt("Age"));
        member.setSex(resultSet.getString("Sex"));
        member.setPhone(resultSet.getString("Phone"));
        member.setEmail(resultSet.getString("Email"));
        member.setAddress(resultSet.getString("Address"));
        member.setPassword(resultSet.getString("password"));
        return member;
    }
    /**
     * 根据指定的条件进行模糊查询数据库中的数据实体
     * @param condition 查询条件
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    public List<member> fuzzySelectByCondition(String condition) throws Exception {
        String sql = "SELECT * FROM member WHERE Name LIKE ? OR Sex LIKE ? OR Phone LIKE ? OR Age LIKE ? OR Email LIKE ? OR Address LIKE ? OR password LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + condition + "%");
            statement.setString(2, "%" + condition + "%");
            statement.setString(3, "%" + condition + "%");
            statement.setString(4, "%" + condition + "%");
            statement.setString(5, "%" + condition + "%");
            statement.setString(6, "%" + condition + "%");
            statement.setString(7, "%" + condition + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                List<member> members = new ArrayList<>();
                while (resultSet.next()) {
                    members.add(mapResultSetTomember(resultSet));
                }
                return members;
            }
        }
    }
    /**
     * 获取表中的总记录数
     * @return 总记录数
     * @throws SQLException 查询记录数异常
     */
    public int getTotalRecords() throws Exception {
        // SQL 查询语句，统计表中的总记录数
        String sql = "SELECT COUNT(*) FROM member";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            // 如果查询结果包含数据，返回第一列的值（总记录数）
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        // 如果没有数据或发生异常，返回0
        return 0;
    }
    /**
     * 根据手机号或邮箱和密码查找用户
     */
    public member selectByPhoneOrEmailAndPassword(String phoneOrEmail, String password) throws Exception {
        String sql = "SELECT * FROM member WHERE (Phone = ? OR Email = ?) AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, phoneOrEmail);
            statement.setString(2, phoneOrEmail);
            statement.setString(3, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetTomember(resultSet);
                } else {
                    return null;
                }
            }
        }
    }

}
