package JDBC.member;

import JDBC.Druid;
import JDBC.member.member;
import JDBC.member.memberDAO;
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
public class Search {
    @FXML
    private TableColumn<member, String> IDCol;

    @FXML
    private TableColumn<member, String> addressCol;

    @FXML
    private TableColumn<member, String> ageCol;

    @FXML
    private TableColumn<member, String> emailCol;

    @FXML
    private TableColumn<member, String> nameCol;

    @FXML
    private TableColumn<member, String> passwordCol;

    @FXML
    private TableColumn<member, String> phoneCol;

    @FXML
    private TableView<member> resultTable;

    @FXML
    private TextField searchField;
    @FXML
    private TextField SID;
    @FXML
    private TableColumn<member, String> sexCol;
    @FXML
    private TextField searchpage;

    /**
     * 搜索数据方法，根据用户输入的关键字进行模糊查询，并展示查询结果
     */
    public void search_() {
        String keyword = searchField.getText(); // 获取用户输入的关键字

        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            List<member> searchResults = memberDao.fuzzySelectByCondition(keyword); // 根据条件进行模糊查询

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 展示查询结果方法，将查询结果展示在表格视图上
     *
     * @param members 查询结果列表
     */
    private void showSearchResults(List<member> members) {
        ObservableList<member> memberList = FXCollections.observableArrayList(members);

        // 设置每一列与 member 对象的哪个属性关联
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("Age"));
        sexCol.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        resultTable.setItems(memberList); // 将会员列表设置为表格视图的数据源
    }

    /**
     * 根据ID进行搜索
     */
    @FXML
    public void searchid() {
        String id = SID.getText(); // 获取用户输入的ID

        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            member result = memberDao.selectById(id); // 根据ID进行查询

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

    /**
     * 根据页码进行搜索
     */
    @FXML
    public void searchbypege() {
        String pageStr = searchpage.getText(); // 获取用户输入的页码

        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);

            int page = Integer.parseInt(pageStr); // 将字符串转换为整数
            int pageSize = 10; // 每页显示的记录数

            List<member> searchResults = memberDao.selectByPage(page, pageSize); // 分页查询数据

            showSearchResults(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
