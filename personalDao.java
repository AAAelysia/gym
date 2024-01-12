package JDBC.personal;

import JDBC.Druid;
import JDBC.personal.personally;
import com.example.fitness_club_management_system.TDao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * 对私教进行增删改查
 */
public class personalDao implements TDao<personally,String> {
    private Connection connection;
    /**
     * 构造函数，接收一个Connection对象
     * @param connection 数据库连接对象
     */
    public personalDao(Connection connection) {
        this.connection = connection;
    }
    // 创建表的SQL语句
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS personalcourse(" +
            "ID CHAR(20) PRIMARY KEY," +
            "CoachID CHAR(20)," +
            "MemberID CHAR(20),"+
            "FOREIGN KEY (CoachID) REFERENCES coach(ID),"+
            "FOREIGN KEY (MemberID) REFERENCES member(ID),"+
            "begin_ date," +
            "end_ date," +
            "price double,"+
            "time_ CHAR(10),"+
            "examine VARCHAR(1) NOT NULL)";
    // 插入数据的SQL语句
    private static final String Insert = "INSERT INTO personalcourse(ID,CoachID,MemberID,begin_,end_,price,time_,examine)" +
            "VALUES(?,?,?,?,?,?,?,?)";
    // 更新数据的SQL语句
    private static final String Update = "UPDATE personalcourse SET CoachID=?,MemberID=?,begin_=?,end_=?,price=?,time_=?,examine=? WHERE ID=?";
    //根据指定的条件和更新参数更新数据库中的数据
    private static final String updateCondition = "UPDATE personalcourse SET ";
    // 删除数据的SQL语句
    private static final String Delete = "DELETE FROM personalcourse WHERE ID=?";
    // 根据ID查询数据的SQL语句
    private static final String SELECT = "SELECT * FROM personalcourse WHERE ID=?";
    // 查询所有数据的SQL语句
    private static final String SELECT_ALL = "SELECT * FROM personalcourse";
    // 根据条件查询数据的SQL语句
    private static final String SELECT_condition = "SELECT * FROM personalcourse WHERE ";
    // 分页查询数据的SQL语句
    private static final String SELECT_PAGE = "SELECT * FROM personalcourse Limit ?,?";
    // 根据CourseID查询数据的SQL语句
    private static final String SELECT_BY_COURSEID = "SELECT * FROM personalcourse WHERE CoachID=?";
    // 根据MemberID查询数据的SQL语句
    private static final String SELECT_BY_MEMBERID = "SELECT * FROM personalcourse WHERE MemberID=?";
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
    public void insert(personally entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            statement.setString(1, entity.getID());
            statement.setString(2, entity.getCoachID());
            statement.setString(3,entity.getMemberID());
            statement.setString(4, String.valueOf(entity.getBegin_()));
            statement.setString(5, String.valueOf(entity.getEnd_()));
            statement.setString(6, String.valueOf(entity.getPrice()));
            statement.setString(7, entity.getTime_());
            statement.setString(8, entity.getExamine());
            statement.executeUpdate();
        }
    }
    /**
     * 批量插入数据
     */
    @Override
    public void insertBatch(List<personally> entities) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Insert)) {
            for (personally entity : entities) {
                statement.setString(1, entity.getID());
                statement.setString(2, entity.getCoachID());
                statement.setString(3,entity.getMemberID());
                statement.setString(4, String.valueOf(entity.getBegin_()));
                statement.setString(5, String.valueOf(entity.getEnd_()));
                statement.setString(6, String.valueOf(entity.getPrice()));
                statement.setString(7, entity.getTime_());
                statement.setString(8, entity.getExamine());
            }
            statement.executeBatch();
        }
    }
    /**
     * 更新数据
     */
    @Override
    public void update(personally entity) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(Update)) {
            statement.setString(1, entity.getCoachID());
            statement.setString(2,entity.getMemberID());
            statement.setString(3, String.valueOf(entity.getBegin_()));
            statement.setString(4, String.valueOf(entity.getEnd_()));
            statement.setString(5, String.valueOf(entity.getPrice()));
            statement.setString(6, entity.getTime_());
            statement.setString(7, entity.getExamine());
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
        String sql = "DELETE FROM personalcourse WHERE " + condition;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * 根据ID查询数据
     */
    @Override
    public personally selectById(String ID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT)) {
            statement.setString(1, ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetTopersonal(resultSet);
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
    public List<personally> selectAll() throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            List<personally> personallies = new ArrayList<>();
            while (resultSet.next()) {
                personallies.add(mapResultSetTopersonal(resultSet));
            }
            return personallies;
        }
    }
    /**
     * 分页查询数据
     */
    @Override
    public List<personally> selectByPage(int page, int pagesize) throws Exception {
        int a = ((page - 1) * pagesize);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PAGE)) {
            statement.setInt(1, a);
            statement.setInt(2, pagesize);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<personally> personallies = new ArrayList<>();
                while (resultSet.next()) {
                    personallies.add(mapResultSetTopersonal(resultSet));
                }
                return personallies;
            }
        }
    }
    /**
     * 将ResultSet映射为personally对象
     */
    private personally mapResultSetTopersonal(ResultSet resultSet) throws SQLException {
        personally personal = new personally();
        personal.setID(resultSet.getString("ID"));
        personal.setCoachID(resultSet.getString("CoachID"));
        personal.setMemberID(resultSet.getString("MemberID"));
        personal.setBegin_(LocalDate.parse(resultSet.getString("begin_")));
        personal.setEnd_(LocalDate.parse(resultSet.getString("end_")));
        personal.setPrice(Double.valueOf(resultSet.getString("price")));
        personal.setTime_(resultSet.getString("time_"));
        personal.setExamine(resultSet.getString("examine"));
        return personal;
    }
    /**
     * 根据指定的条件进行模糊查询数据库中的数据实体
     * @param condition 查询条件
     * @return 数据实体集合
     * @throws SQLException 查询数据异常
     */
    public List<personally> fuzzySelectByCondition(String condition) throws Exception {
        String sql = "SELECT * FROM personalcourse WHERE CoachID LIKE ? OR MemberID LIKE ? OR begin_ LIKE ? OR end_ LIKE ? OR price LIKE ? OR time_ LIKE ? OR examine LIKE ?";
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
                List<personally> personallies = new ArrayList<>();
                while (resultSet.next()) {
                    personallies.add(mapResultSetTopersonal(resultSet));
                }
                return personallies;
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
        String sql = "SELECT COUNT(*) FROM personalcourse";
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
     * 根据审核状态查询个人课程信息的方法
     *
     * @param examine 审核状态
     * @return 符合条件的个人课程信息列表
     * @throws Exception 查询异常
     */
    public List<personally> selectByExamine(String examine) throws Exception {
        String sql = "SELECT * FROM personalcourse WHERE examine=?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(examine));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<personally> personallies = new ArrayList<>();
                while (resultSet.next()) {
                    personallies.add(mapResultSetTopersonal(resultSet));
                }
                return personallies;
            }
        }
    }
    /**
     * 根据CourseID查询数据
     */
    public List<personally> selectByCourseID(String courseID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_BY_COURSEID)) {
            statement.setString(1, courseID);
            ResultSet resultSet = statement.executeQuery();

            List<personally> personalcourses = new ArrayList<>();
            while (resultSet.next()) {
                personalcourses.add(mapResultSetTopersonal(resultSet));
            }

            return personalcourses;
        }
    }
    /**
     * 根据MemberID查询数据
     */
    public List<personally> selectByMemberID(String memberID) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_BY_MEMBERID)) {
            statement.setString(1, memberID);
            ResultSet resultSet = statement.executeQuery();

            List<personally> personallies = new ArrayList<>();
            while (resultSet.next()) {
                personallies.add(mapResultSetTopersonal(resultSet));
            }

            return personallies;
        }
    }
    /**
     * 根据end_查询数据
     */
    public List<personally> selectByEndDate(LocalDate endDate) throws Exception {
        String sql = "SELECT * FROM personalcourse WHERE end_ < ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                List<personally> personallies = new ArrayList<>();
                while (resultSet.next()) {
                    personallies.add(mapResultSetTopersonal(resultSet));
                }
                return personallies;
            }
        }
    }
    /**
     * 根据会员ID查询教练ID的方法
     *
     * @param memberId 会员ID
     * @return 符合条件的教练ID列表
     * @throws Exception 查询异常
     */
    public List<String> selectCoachIdByMemberId(String memberId) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT CoachID FROM personalcourse WHERE MemberID=? AND examine='是'")) {
            statement.setString(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            List<String> coachIDs = new ArrayList<>();
            while (resultSet.next()) {
                coachIDs.add(resultSet.getString("CoachID"));
            }
            return coachIDs;
        }

    }

}
