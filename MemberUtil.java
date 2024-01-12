package JDBC.member;

import JDBC.Druid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * 会员管理工具类
 */
public class MemberUtil {
    private int itemsPerPage = 20; // 每页显示的数据条数
    @FXML
    private TableColumn<member, String> Address;

    @FXML
    private TableColumn<member, String> Age;

    @FXML
    private TableColumn<member, String> Email;

    @FXML
    private TableColumn<member, String> ID;

    @FXML
    private TableColumn<member, String> Name;

    @FXML
    private TableColumn<member, String> Phone;

    @FXML
    private TableColumn<member, String> Sex;

    @FXML
    private TextField addressField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField nameField;

    @FXML
    private Pagination pageTable;

    @FXML
    private TableColumn<member, String> password;

    @FXML
    private TextField passwordField;

    @FXML
    private ToggleGroup rsex;

    @FXML
    private TextField IDField;

    @FXML
    private TableView<member> memberTableView;

    @FXML
    private TextField telField;
    private member selectedmember; // 存储当前选中的会员对象
    /**
     * 初始化方法，界面加载时自动调用
     */
    @FXML
    private void initialize() {
        initPagination();
        loadPageData(0);

        memberTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedmember = newValue;
        });
    }
    /**
     * 初始化分页组件
     */
    private void initPagination() {
        pageTable.setPageCount(calculatePageCount());
        pageTable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    /**
     * 计算总页数
     *
     * @return 总页数
     */
    private int calculatePageCount() {
        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            return (int) Math.ceil((double) memberDao.getTotalRecords() / itemsPerPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 加载指定页的数据
     * @param pageIndex 页索引
     */
    private void loadPageData(int pageIndex) {
        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            List<member> pageData = memberDao.selectByPage(pageIndex + 1, itemsPerPage);
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新表格视图数据
     * @param members 会员信息列表
     */
    private void updateTableView(List<member> members) {
        memberTableView.getItems().setAll(members);
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        Sex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        ObservableList<member> memberList = FXCollections.observableArrayList(members);
        memberTableView.setItems(memberList);
    }
    /**
     * 处理 "添加" 按钮的点击事件，将用户输入的会员信息添加到数据库中
     */
    @FXML
    void add() {
        // 创建一个新的会员对象
        member newmember = new member();

        // 从输入框中获取用户输入的信息，并设置到新的会员对象中
        newmember.setID(IDField.getText());
        newmember.setName(nameField.getText());
        if (maleRadioButton.isSelected()) {
            newmember.setSex("男");
        } else if (femaleRadioButton.isSelected()) {
            newmember.setSex("女");
        }
        newmember.setAge(Integer.parseInt(ageField.getText()));
        newmember.setPhone(telField.getText());
        newmember.setEmail(emailField.getText());
        newmember.setAddress(addressField.getText());
        newmember.setPassword(passwordField.getText());

        // 将新的会员对象保存到数据库中
        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            memberDao.insert(newmember);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 清空输入框并刷新表格
        clearFields();
        refreshTableView();
    }
    /**
     * 处理 "删除" 按钮的点击事件，删除用户选择的会员信息
     */
    @FXML
    void btnDel(ActionEvent event) {
        List<member> selectedmembers = new ArrayList<>(memberTableView.getSelectionModel().getSelectedItems());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的会员信息吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                deleteSelected(selectedmembers);
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 删除用户选择的会员信息
     *
     * @param selectedmembers 用户选择的会员信息列表
     * @throws Exception 如果删除操作发生异常，则抛出异常
     */
    private void deleteSelected(List<member> selectedmembers) throws Exception {
        try (Connection connection = Druid.getConnection()) {
            memberDAO memberDao = new memberDAO(connection);
            for (member member : selectedmembers) {
                memberDao.delete(member.getID());
            }
        }
    }
    /**
     * 处理 "修改" 按钮的点击事件，将用户选择的会员信息显示在输入框中，允许用户进行修改
     */
    @FXML
    void change() {
        member selectedmember = memberTableView.getSelectionModel().getSelectedItem();
        if (selectedmember != null) {
            IDField.setText(selectedmember.getID());
            nameField.setText(selectedmember.getName());
            if (selectedmember.getSex().equals("男")) {
                maleRadioButton.setSelected(true);
            } else if (selectedmember.getSex().equals("女")) {
                femaleRadioButton.setSelected(true);
            }
            ageField.setText(String.valueOf(selectedmember.getAge()));
            telField.setText(selectedmember.getPhone());
            emailField.setText(selectedmember.getEmail());
            addressField.setText(selectedmember.getAddress());
            passwordField.setText(selectedmember.getPassword());
            // 禁用主键输入框，不允许修改主键
            IDField.setDisable(true);
        }
    }
    /**
     * 处理 "查询" 按钮的点击事件，打开查询界面执行查询操作
     */
    @FXML
    void handleSearchButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("search.fxml"));
            Parent root = loader.load();

            Search controller = loader.getController(); // 获取查询数据界面的 Controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("查询数据");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            controller.search_(); // 执行查询操作，将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 处理 "保存更改" 按钮的点击事件，保存用户对会员信息的修改到数据库中
     */
    @FXML
    void saveChanges(ActionEvent event) {
        if (selectedmember != null) {
            selectedmember.setName(nameField.getText());
            selectedmember.setAge(Integer.parseInt(ageField.getText()));
            selectedmember.setPhone(telField.getText());
            if (maleRadioButton.isSelected()) {
                selectedmember.setSex("男");
            } else if (femaleRadioButton.isSelected()) {
                selectedmember.setSex("女");
            }
            selectedmember.setEmail(emailField.getText());
            selectedmember.setAddress(addressField.getText());
            selectedmember.setPassword(passwordField.getText());
            // 更新数据库中的数据
            try (Connection connection = Druid.getConnection()) {
                memberDAO memberDao = new memberDAO(connection);
                memberDao.update(selectedmember); // 调用更新方法将修改后的学生信息保存到数据库
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 清空输入框并刷新表格
            clearFields();
            refreshTableView();
            IDField.setDisable(false); // 重新打开会员号输入框
        }
    }
    /**
     * 清空输入框中的内容
     */
    private void clearFields() {
        IDField.clear();
        nameField.clear();
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        telField.clear();
        ageField.clear();
        emailField.clear();
        addressField.clear();
        passwordField.clear();
    }
    /**
     * 刷新表格视图，根据当前页数重新加载数据
     */
    private void refreshTableView() {
        int currentPageIndex = pageTable.getCurrentPageIndex();
        int newTotalPages = calculatePageCount();
        pageTable.setPageCount(newTotalPages);
        loadPageData(currentPageIndex);
        if (memberTableView.getItems().isEmpty() && currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);
        }
    }
}
