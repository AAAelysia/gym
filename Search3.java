package JDBC.course;

import JDBC.Druid;
import JDBC.course.Course;
import JDBC.course.CourseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.List;

/**
 * 搜索类，用于处理用户的搜索请求并展示搜索结果
 */
public class Search3 {

    @FXML
    private TableColumn<Course, String> IDCol;

    @FXML
    private TextField SID;

    @FXML
    private TableColumn<Course, String> numberField;

    @FXML
    private TableColumn<Course, String> coachidCol;

    @FXML
    private TableColumn<Course, String> descriptionCol;

    @FXML
    private TableColumn<Course, String> durationCol;

    @FXML
    private TableColumn<Course, String> nameCol;

    @FXML
    private TableColumn<Course, String> priceCol;

    @FXML
    private TableView<Course> resultTable;

    @FXML
    private TableColumn<Course, String> examineCol;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchcoachid;

    @FXML
    private TextField searchpage;

    @FXML
    private TableColumn<Course, String> typeCol;
    @FXML
    private TextField minpriceField;
    @FXML
    private TextField maxpriceField;
    @FXML
    public void search_3() {
        String keyword = searchField.getText(); // 获取用户输入的关键字

        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);
            List<Course> searchResults = courseDao.fuzzySelectByCondition(keyword); // 根据条件进行模糊查询

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 展示查询结果方法，将查询结果展示在表格视图上
     * @param courses 查询结果列表
     */
    void showSearchResults(List<Course> courses) {
        ObservableList<Course> courseList = FXCollections.observableArrayList(courses);

        // 设置每一列与 coach 对象的哪个属性关联
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coachidCol.setCellValueFactory(new PropertyValueFactory<>("CoachID"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        examineCol.setCellValueFactory(new PropertyValueFactory<>("examine"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        numberField.setCellValueFactory(new PropertyValueFactory<>("number"));

        resultTable.setItems(courseList); // 将会员列表设置为表格视图的数据源
    }
    /**
     * 根据页码搜索课程记录，并将结果展示在表格视图上。
     */
    @FXML
    void searchbypege() {
        String pageStr = searchpage.getText(); // 获取用户输入的页码

        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);

            int page = Integer.parseInt(pageStr); // 将字符串转换为整数
            int pageSize = 10; // 每页显示的记录数

            List<Course> searchResults = courseDao.selectByPage(page, pageSize); // 分页查询数据

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据教练ID搜索课程记录，并将结果展示在表格视图上。
     */
    @FXML
    void searchcoachid() {
        String coachID = searchcoachid.getText(); // 获取用户输入的教练ID

        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);
            List<Course> searchResults = courseDao.selectByCoachID(coachID); // 根据教练ID进行查询

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据价格范围搜索课程记录，并将结果展示在表格视图上。
     */
    @FXML
    void searchbyprice() {
        String minPriceStr = minpriceField.getText(); // 获取用户输入的最低价格
        String maxPriceStr = maxpriceField.getText(); // 获取用户输入的最高价格

        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);

            double minPrice = Double.parseDouble(minPriceStr); // 将字符串转换为double类型
            double maxPrice = Double.parseDouble(maxPriceStr); // 将字符串转换为double类型

            List<Course> searchResults = courseDao.selectByPriceRange((int) minPrice, (int) maxPrice); // 根据价格范围查询数据

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据课程ID进行查询，并将结果展示在表格视图上。
     */
    @FXML
    void searchid() {
        String id = SID.getText(); // 获取用户输入的ID

        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);
            Course result = courseDao.selectById(id); // 根据ID进行查询

            if (result != null) {
                showSearchResults(List.of(result)); // 将查询结果展示在表格视图上
            } else {
                // 如果查询结果为空，清空表格视图
                resultTable.getItems().clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
