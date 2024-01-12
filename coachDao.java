package JDBC.coach;

import JDBC.Druid;
import com.example.fitness_club_management_system.TDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 教练数据访问对象，实现TDao接口
 */
public class coachDao implements TDao<coach,String> {
    private Connection connection;
    /**
     * 构造函数，接收一个Connection对象
     * @param connection 数据库连接对象
     */
    public coachDao(Connection connection) {
        this.connection = connection;
    }
    // 创建表的SQL语句
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS coach(" +
            "ID CHAR(11) PRIMARY KEY," +
            "Name VARCHAR(50) NOT NULL," +
            "Age int NOT NULL,"+
            "Sex CHAR(2) NOT NULL DEFAULT '男' CHECK(Sex in('男','女'))," +
            "Phone VARCHAR(20)," +
            "Email VARCHAR(50),"+
            "Specialization VARCHAR(50),"+
            "Experience VARCHAR(3) NOT NULL,"+
            "examine VARCHAR(1)";
    // 插入数据的SQL语句
    private static final String Insert = "INSERT INTO coach(ID,Name,Age,Sex,Phone,Email,Specialization,Experience,examine)" +
            "VALUES(?,?,?,?,?,?,?,?,?)";
    // 更新数据的SQL语句
    private static final String Update = "UPDATE coach SET Name=?,Age=?,Sex=?,Phone=?,Email=?,Specialization=?,Experience=?,examine=? WHERE ID=?";
    //根据指定的条件和更新参数更新数据库中的数据
    private static final String updateCondition = "UPDATE coach SET ";
    // 删除数据的SQL语句
    private static final String Delete = "DELETE FROM coach WHERE ID=?";
    // 根据ID查询数据的SQL语句
    private static final String SELECT = "SELECT * FROM coach WHERE ID=?";
    // 查询所有数据的SQL语句
    private static final String SELECT_ALL = "SELECT * FROM coach";
    // 根据条件查询数据的SQL语句
    private static final String SELECT_condition = "SELECT * FROM coach WHERE ";
    // 分页查询数据的SQL语句
    private static final String SELECT_PAGE = "SELECT * FROM coach Limit ?,?";
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
    public void insert(coach entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            statement.setString(1, entity.getID());
            statement.setString(2, entity.getName());
            statement.setInt(3,entity.getAge());
            statement.setString(4, entity.getSex());
            statement.setString(5, entity.getPhone());
            statement.setString(6, entity.getEmail());
            statement.setString(7, entity.getSpecialization());
            statement.setString(8, entity.getExperience());
            statement.setString(9,entity.getExamine());
            statement.executeUpdate();
        }
    }
    /**
     * 批量插入数据
     */
    @Override
    public void insertBatch(List<coach> entities) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            for (coach entity : entities) {
                statement.setString(1, entity.getID());
                statement.setString(2, entity.getName());
                statement.setInt(3,entity.getAge());
                statement.setString(4, entity.getSex());
                statement.setString(5, entity.getPhone());
                statement.setString(6, entity.getEmail());
                statement.setString(7, entity.getSpecialization());
                statement.setString(8, entity.getExperience());
                statement.setString(9,entity.getExamine());
            }
            statement.executeBatch();
        }
    }
    /**
     * 更新数据
     */
    @Override
    public void update(coach entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Update)) {
            statement.setString(1, entity.getName());
            statement.setInt(2,entity.getAge());
            statement.setString(3, entity.getSex());
            statement.setString(4, entity.getPhone());
            statement.setString(5, entity.getEmail());
            statement.setString(6, entity.getSpecialization());
            statement.setString(7, entity.getExperience());
            statement.setString(8,entity.getExamine());
            statement.setString(9, entity.getID());
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
        String sql = "DELETE FROM coach WHERE " + condition;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
    /**
     * 根据ID查询数据
     */
    @Override
    public coach selectById(String ID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT)) {
            statement.setString(1, ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetTocoach(resultSet);
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
    public List<coach> selectAll() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<coach> coachs = new ArrayList<>();
            while (resultSet.next()) {
                coachs.add(mapResultSetTocoach(resultSet));
            }
            return coachs;
        }
    }
    /**
     * 分页查询数据
     */
    @Override
    public List<coach> selectByPage(int page, int pagesize) throws Exception {
        int a = ((page - 1) * pagesize);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PAGE)) {
            statement.setInt(1, a);
            statement.setInt(2, pagesize);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<coach> coachs = new ArrayList<>();
                while (resultSet.next()) {
                    coachs.add(mapResultSetTocoach(resultSet));
                }
                return coachs;
            }
        }
    }
    /**
     * 将ResultSet映射为coach对象
     */
    private coach mapResultSetTocoach(ResultSet resultSet) throws SQLException {
        coach coach = new coach();
        coach.setID(resultSet.getString("ID"));
        coach.setName(resultSet.getString("Name"));
        coach.setAge(resultSet.getInt("Age"));
        coach.setSex(resultSet.getString("Sex"));
        coach.setPhone(resultSet.getString("Phone"));
        coach.setEmail(resultSet.getString("Email"));
        coach.setSpecialization(resultSet.getString("Specialization"));
        coach.setExperience(resultSet.getString("Experience"));
        coach.setExamine(resultSet.getString("examine"));
        return coach;
    }
    /**
     * 根据指定的条件进行模糊查询数据库中的数据实体
     * @param condition 查询条件
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    public List<coach> fuzzySelectByCondition(String condition) throws Exception {
        String sql = "SELECT * FROM coach WHERE Name LIKE ? OR Sex LIKE ? OR Phone LIKE ? OR Age LIKE ? OR Email LIKE ? OR Specialization LIKE ? OR Experience LIKE ? OR examine LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, "%" + condition + "%");
            statement.setString(2, "%" + condition + "%");
            statement.setString(3, "%" + condition + "%");
            statement.setString(4, "%" + condition + "%");
            statement.setString(5, "%" + condition + "%");
            statement.setString(6, "%" + condition + "%");
            statement.setString(7, "%" + condition + "%");
            statement.setString(8, "%" + condition + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                List<coach> coachs = new ArrayList<>();
                while (resultSet.next()) {
                    coachs.add(mapResultSetTocoach(resultSet));
                }
                return coachs;
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
        String sql = "SELECT COUNT(*) FROM coach";
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
     * 根据examine查询examine为是的id和name
     * @param examineValue examine的值
     * @return 课程列表
     * @throws Exception 查询异常
     */
    public List<coach> selectByExamine(String examineValue) throws Exception {
        String sql = "SELECT * FROM coach WHERE examine=?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, examineValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<coach> coaches = new ArrayList<>();
                while (resultSet.next()) {
                    coaches.add(mapResultSetTocoach(resultSet));
                }
                return coaches;
            }
        }
    }
}
