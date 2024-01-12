package JDBC.course;

import JDBC.Druid;
import JDBC.course.coursemember;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * searchclassDao类是一个用于数据库操作的类，用于对coursemember表进行增删改查操作。
 */
public class searchclassDao {
    private Connection connection;

    /**
     * 构造函数，接收一个Connection对象
     *
     * @param connection 数据库连接对象
     */
    public searchclassDao(Connection connection) {
        this.connection = connection;
    }

    // 创建表的SQL语句
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS coursemember(" +
            "ID int PRIMARY KEY," +
            "CourseID CHAR(11) NOT NULL," +
            "memberID CHAR(11) NOT NULL," +
            "examine CHAR(1))";
    // 插入数据的SQL语句
    private static final String Insert = "INSERT INTO coursemember(ID,CourseID,memberID,examine)" +
            "VALUES(?,?,?,?)";
    // 更新数据的SQL语句
    private static final String Update = "UPDATE coursemember SET CourseID=?,memberID=?,examine=? WHERE ID=?";
    // 删除数据的SQL语句
    private static final String Delete = "DELETE FROM coursemember WHERE ID=?";
    // 查询所有数据的SQL语句
    private static final String SELECT_ALL = "SELECT * FROM coursemember";
    // 根据CourseID查询数据的SQL语句
    private static final String SELECT_BY_COURSE_ID = "SELECT * FROM coursemember WHERE CourseID=?";
    // 根据MemberID查询数据的SQL语句
    private static final String SELECT_BY_MEMBER_ID = "SELECT * FROM coursemember WHERE memberID=?";

    /**
     * 获取数据库连接
     */
    private Connection getConnection() throws Exception {
        return Druid.getConnection();
    }

    /**
     * 创建表
     *
     * @throws Exception
     */
    public void createTabel() throws Exception {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE);
        }
    }

    /**
     * 插入数据
     *
     * @param entity 要插入的coursemember对象
     * @throws Exception
     */
    public void insert(coursemember entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            statement.setInt(1, entity.getID());
            statement.setString(2, entity.getCourseID());
            statement.setString(3, entity.getMemberID());
            statement.setString(4, entity.getExamine());
            statement.executeUpdate();
        }
    }

    /**
     * 更新数据
     *
     * @param entity 要更新的coursemember对象
     * @throws Exception
     */
    public void update(coursemember entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Update)) {
            statement.setString(1, entity.getCourseID());
            statement.setString(2, entity.getMemberID());
            statement.setString(3, entity.getExamine());
            statement.setInt(4, entity.getID());
            statement.executeUpdate();
        }
    }

    /**
     * 删除数据
     *
     * @param ID 要删除的数据的ID
     * @throws Exception
     */
    public void delete(String ID) throws Exception {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(Delete)) {
            statement.setString(1, ID);
            statement.executeUpdate();
        }
    }

    /**
     * 查询所有数据
     *
     * @return 返回包含所有coursemember对象的List集合
     * @throws Exception
     */
    public List<coursemember> selectAll() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<coursemember> coursemembers = new ArrayList<>();
            while (resultSet.next()) {
                coursemembers.add(mapResultSetTocoursemember(resultSet));
            }
            return coursemembers;
        }
    }

    /**
     * 将ResultSet映射为coursemember对象
     *
     * @param resultSet 查询结果的ResultSet对象
     * @return 返回映射后的coursemember对象
     * @throws SQLException
     */
    private coursemember mapResultSetTocoursemember(ResultSet resultSet) throws SQLException {
        coursemember coursemember = new coursemember();
        coursemember.setID(resultSet.getInt("ID"));
        coursemember.setCourseID(resultSet.getString("CourseID"));
        coursemember.setMemberID(resultSet.getString("MemberID"));
        coursemember.setExamine(resultSet.getString("examine"));
        return coursemember;
    }

    /**
     * 根据CourseID查询数据
     *
     * @param courseID 要查询的CourseID
     * @return 返回包含查询到的coursemember对象的List集合
     * @throws Exception
     */
    public List<coursemember> selectByCourseID(String courseID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_BY_COURSE_ID)) {
            statement.setString(1, courseID);
            ResultSet resultSet = statement.executeQuery();

            List<coursemember> coursemembers = new ArrayList<>();
            while (resultSet.next()) {
                coursemembers.add(mapResultSetTocoursemember(resultSet));
            }

            return coursemembers;
        }
    }

    /**
     * 根据MemberID查询数据
     *
     * @param memberID 要查询的MemberID
     * @return 返回包含查询到的coursemember对象的List集合
     * @throws Exception
     */
    public List<coursemember> selectByMemberID(String memberID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_BY_MEMBER_ID)) {
            statement.setString(1, memberID);
            ResultSet resultSet = statement.executeQuery();

            List<coursemember> coursemembers = new ArrayList<>();
            while (resultSet.next()) {
                coursemembers.add(mapResultSetTocoursemember(resultSet));
            }

            return coursemembers;
        }
    }

    /**
     * 根据MemberID查询CourseID
     *
     * @param memberID 要查询的MemberID
     * @return 返回包含查询到的CourseID的List集合
     * @throws Exception
     */
    public List<String> selectCourseIDsByMemberID(String memberID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT CourseID FROM coursemember WHERE memberID=? AND examine='是'")) {
            statement.setString(1, memberID);
            ResultSet resultSet = statement.executeQuery();

            List<String> courseIDs = new ArrayList<>();
            while (resultSet.next()) {
                courseIDs.add(resultSet.getString("CourseID"));
            }

            return courseIDs;
        }
    }

    /**
     * 获取数据条数
     *
     * @return 返回数据库中的数据条数
     * @throws Exception
     */
    public int getCount() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT COUNT(*) FROM coursemember");
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        }
    }

    /**
     * 根据审核状态查询数据
     *
     * @param examine 要查询的审核状态
     * @return 返回包含查询到的coursemember对象的List集合
     * @throws Exception
     */
    public List<coursemember> selectByExamine(String examine) throws Exception {
        String sql = "SELECT * FROM coursemember WHERE examine=?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(examine));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<coursemember> coursemembers = new ArrayList<>();
                while (resultSet.next()) {
                    coursemembers.add(mapResultSetTocoursemember(resultSet));
                }
                return coursemembers;
            }
        }
    }
}
