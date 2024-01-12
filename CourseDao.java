package JDBC.course;

import JDBC.Druid;
import JDBC.course.Course;
import com.example.fitness_club_management_system.TDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理课程的增删改查
 */
public class CourseDao implements TDao<Course,String> {
    private Connection connection;
    /**
     * 构造函数，接收一个Connection对象
     * @param connection 数据库连接对象
     */
    public CourseDao(Connection connection) {
        this.connection = connection;
    }
    // 创建表的SQL语句
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS course(" +
            "ID CHAR(11) PRIMARY KEY," +
            "Name VARCHAR(50) NOT NULL," +
            "CoachID CHAR(11) NOT NULL,"+
            "Type VARCHAR(50) ," +
            "Duration int," +
            "Description VARCHAR(50),"+
            "examine VARCHAR(50),"+
            "price int NOT NULL,"+
            "number int)";
    // 插入数据的SQL语句
    private static final String Insert = "INSERT INTO Course(ID,Name,CoachID,Type,Duration,Description,examine,price,number)" +
            "VALUES(?,?,?,?,?,?,?,?,?)";
    // 更新数据的SQL语句
    private static final String Update = "UPDATE Course SET Name=?, CoachID=?, Type=?, Duration=?, Description=?, examine=?, price=?, number=? WHERE ID=?";    //根据指定的条件和更新参数更新数据库中的数据
    private static final String updateCondition = "UPDATE Course SET ";
    // 删除数据的SQL语句
    private static final String Delete = "DELETE FROM Course WHERE ID=?";
    // 根据ID查询数据的SQL语句
    private static final String SELECT = "SELECT * FROM Course WHERE ID=?";
    // 查询所有数据的SQL语句
    private static final String SELECT_ALL = "SELECT * FROM Course";
    // 根据条件查询数据的SQL语句
    private static final String SELECT_condition = "SELECT * FROM Course WHERE ";
    // 分页查询数据的SQL语句
    private static final String SELECT_PAGE = "SELECT * FROM Course Limit ?,?";
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
    public void insert(Course entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            statement.setString(1, entity.getID());
            statement.setString(2, entity.getName());
            statement.setString(3,entity.getCoachID());
            statement.setString(4, entity.getType());
            statement.setInt(5, entity.getDuration());
            statement.setString(6, entity.getDescription());
            statement.setString(7, entity.getExamine());
            statement.setInt(8, entity.getPrice());
            statement.setInt(9, entity.getNumber());
            statement.executeUpdate();
        }
    }
    /**
     * 批量插入数据
     */
    @Override
    public void insertBatch(List<Course> entities) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            for (Course entity : entities) {
                statement.setString(1, entity.getName());
                statement.setString(2,entity.getCoachID());
                statement.setString(3, entity.getType());
                statement.setInt(4, entity.getDuration());
                statement.setString(5, entity.getDescription());
                statement.setString(6, entity.getExamine());
                statement.setInt(7, entity.getPrice());
                statement.setInt(8, entity.getNumber());
                statement.setString(9, entity.getID());
            }
            statement.executeBatch();
        }
    }
    /**
     * 更新数据
     */
    @Override
    public void update(Course entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Update)) {
            // 注意：将ID放在最后，用于WHERE条件匹配
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCoachID());
            statement.setString(3, entity.getType());
            statement.setInt(4, entity.getDuration());
            statement.setString(5, entity.getDescription());
            statement.setString(6, entity.getExamine());
            statement.setInt(7, entity.getPrice());
            statement.setInt(8, entity.getNumber());
            statement.setString(9, entity.getID()); // 设置ID作为WHERE条件

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
        String sql = "DELETE FROM CourseWHERE " + condition;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
    /**
     * 根据ID查询数据
     */
    @Override
    public Course selectById(String ID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT)) {
            statement.setString(1, ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToCourse(resultSet);
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
    public List<Course> selectAll() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(mapResultSetToCourse(resultSet));
            }
            return courses;
        }
    }
    /**
     * 分页查询数据
     */
    @Override
    public List<Course> selectByPage(int page, int pagesize) throws Exception {
        int a = ((page - 1) * pagesize);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PAGE)) {
            statement.setInt(1, a);
            statement.setInt(2, pagesize);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
                return courses;
            }
        }
    }
    /**
     * 将ResultSet映射为coach对象
     */
    private Course mapResultSetToCourse(ResultSet resultSet) throws SQLException {
        Course course= new Course();
        course.setID(resultSet.getString("ID"));
        course.setName(resultSet.getString("Name"));
        course.setCoachID(resultSet.getString("CoachID"));
        course.setType(resultSet.getString("Type"));
        course.setDuration(resultSet.getInt("Duration"));
        course.setDescription(resultSet.getString("Description"));
        course.setExamine(resultSet.getString("examine"));
        course.setPrice(resultSet.getInt("price"));
        course.setNumber(resultSet.getInt("number"));
        return course;
    }
    /**
     * 根据指定的条件进行模糊查询数据库中的数据实体
     * @param condition 查询条件
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    public List<Course> fuzzySelectByCondition(String condition) throws Exception {
        String sql = "SELECT * FROM course WHERE Name LIKE ? OR Type LIKE ? OR Duration LIKE ? OR Description LIKE ? OR examine LIKE ? OR price LIKE ? OR number LIKE ?";
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
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
                return courses;
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
        String sql = "SELECT COUNT(*) FROM course";
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
     * 根据教练ID查询课程
     * @param coachID 教练ID
     * @return 课程列表
     * @throws Exception 查询异常
     */
    public List<Course> selectByCoachID(String coachID) throws Exception {
        String sql = "SELECT * FROM Course WHERE CoachID=?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, coachID);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
                return courses;
            }
        }
    }
    /**
     * 根据价格范围查询课程
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 课程列表
     * @throws Exception 查询异常
     */
    public List<Course> selectByPriceRange(int minPrice, int maxPrice) throws Exception {
        String sql = "SELECT * FROM Course WHERE price >= ? AND price <= ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, minPrice);
            statement.setInt(2, maxPrice);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
                return courses;
            }
        }
    }
    /**
     * 根据examine查询examine为是的id和name
     * @param examineValue examine的值
     * @return 课程列表
     * @throws Exception 查询异常
     */
    public List<Course> selectByExamine(String examineValue) throws Exception {
        String sql = "SELECT * FROM Course WHERE examine=?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, examineValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Course> courses = new ArrayList<>();
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
                return courses;
            }
        }
    }

}
