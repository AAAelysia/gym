package JDBC.personal;

import JDBC.Druid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
/**
 * 搜索类，用于处理用户的搜索请求并展示搜索结果
 */
public class Search4 {

    @FXML
    private TableColumn<personally,String> CoachID;

    @FXML
    private TableColumn<personally,String> ID;

    @FXML
    private TableColumn<personally,String> MemberID;

    @FXML
    private TextField SID;

    @FXML
    private TableColumn<personally,String> begin;

    @FXML
    private TableColumn<personally,String> end;

    @FXML
    private TableColumn<personally,String> examine;

    @FXML
    private TableColumn<personally,String> price;

    @FXML
    private TableView<personally> resultTable;

    @FXML
    private TextField searchField;

    @FXML
    private TextField searchM;

    @FXML
    private DatePicker searchday;

    @FXML
    private TextField searchpage;

    @FXML
    private TableColumn<personally,String> time;

    @FXML
    public void initialize() {
        searchday.setValue(LocalDate.now()); // 将searchday的值设置为今天的日期
    }

    /**
     * 根据关键字进行模糊查询个人信息
     */
    @FXML
    public void search_4() {
        String keyword = searchField.getText(); // 获取用户输入的关键字

        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            List<personally> searchResults = personaldao.fuzzySelectByCondition(keyword); // 根据条件进行模糊查询

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示查询结果方法，将查询结果展示在表格视图上
     * @param personallies 查询结果列表
     */
    void showSearchResults(List<personally> personallies) {
        ObservableList<personally> personallies1 = FXCollections.observableArrayList(personallies);

        // 设置每一列与 member 对象的哪个属性关联
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        CoachID.setCellValueFactory(new PropertyValueFactory<>("CoachID"));
        MemberID.setCellValueFactory(new PropertyValueFactory<>("MemberID"));
        begin.setCellValueFactory(new PropertyValueFactory<>("begin_"));
        end.setCellValueFactory(new PropertyValueFactory<>("end_"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        time.setCellValueFactory(new PropertyValueFactory<>("time_"));
        examine.setCellValueFactory(new PropertyValueFactory<>("examine"));

        resultTable.setItems(personallies1); // 将会员列表设置为表格视图的数据源
    }

    /**
     * 根据页码进行分页查询个人信息
     */
    @FXML
    void searchbypege() {
        String pageStr = searchpage.getText(); // 获取用户输入的页码

        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);

            int page = Integer.parseInt(pageStr); // 将字符串转换为整数
            int pageSize = 10; // 每页显示的记录数

            List<personally> searchResults = personaldao.selectByPage(page, pageSize); // 分页查询数据

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据选择的日期进行查询个人信息
     */
    @FXML
    void searchcan(ActionEvent event) {
        LocalDate endDate = searchday.getValue(); // 获取用户选择的日期

        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            List<personally> searchResults = personaldao.selectByEndDate(endDate); // 根据结束日期进行查询
            if (searchResults != null) {
                showSearchResults(searchResults); // 将查询结果展示在表格视图上
            } else {
                // 如果查询结果为空，清空表格视图
                resultTable.getItems().clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据教练ID进行查询个人信息
     */
    @FXML
    void searchid(ActionEvent event) {
        String coachID = SID.getText(); // 获取用户输入的教练ID

        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            List<personally> searchResults = personaldao.selectByCourseID(coachID); // 根据教练ID进行查询
            if(searchResults!=null){
                showSearchResults(searchResults); // 将查询结果展示在表格视图上
            }else {
                // 如果查询结果为空，清空表格视图
                resultTable.getItems().clear();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据会员ID进行查询个人信息
     */
    @FXML
    void searchm(ActionEvent event) {
        String memberID = searchM.getText(); // 获取用户输入的会员ID

        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            List<personally> searchResults = personaldao.selectByMemberID(memberID); // 根据会员ID进行查询
            if(searchResults!=null){
                showSearchResults(searchResults); // 将查询结果展示在表格视图上
            }else {
                // 如果查询结果为空，清空表格视图
                resultTable.getItems().clear();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
