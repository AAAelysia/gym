package JDBC.course;

import JDBC.Druid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 班级搜索控制器类
 */
public class Searchclass {
    @FXML
    private TextField YNField;
    @FXML
    private TableColumn<coursemember, String> YN;
    @FXML
    private TextField courseField;

    @FXML
    private TableView<coursemember> courseTableview;

    @FXML
    private TableColumn<coursemember, String> courseid;

    @FXML
    private TableColumn<coursemember, String> id;

    @FXML
    private TextField idField;

    @FXML
    private TextField memberField;

    @FXML
    private TableColumn<coursemember, String> memberid;
    private coursemember selected; // 存储当前选中的班级对象

    /**
     * 初始化方法，界面加载时自动调用
     */
    @FXML
    private void initialize() {
        List<coursemember> coursememberList = new ArrayList<>();
        coursememberList = update();
        updateTableView(coursememberList);
        courseTableview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = newValue;
        });
    }

    /**
     * 更新数据
     *
     * @return 会员信息列表
     */
    private List<coursemember> update() {
        try (Connection connection = Druid.getConnection()) {
            searchclassDao searchclassDao = new searchclassDao(connection);
            return searchclassDao.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新表格视图数据
     *
     * @param coursemembers 会员信息列表
     */
    private void updateTableView(List<coursemember> coursemembers) {
        courseTableview.getItems().setAll(coursemembers);
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        courseid.setCellValueFactory(new PropertyValueFactory<>("CourseID"));
        memberid.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        YN.setCellValueFactory(new PropertyValueFactory<>("examine"));
        ObservableList<coursemember> coursememberList = FXCollections.observableArrayList(coursemembers);
        courseTableview.setItems(coursememberList);
    }

    /**
     * 创建班级按钮点击事件处理方法
     *
     * @param event 事件对象
     */
    @FXML
    void build(ActionEvent event) {
        // 创建一个新的班级对象
        coursemember newcoursemember = new coursemember();

        // 从输入框中获取用户输入的信息，并设置到新的班级对象中
        newcoursemember.setID(Integer.parseInt(idField.getText()));
        newcoursemember.setCourseID(courseField.getText());
        newcoursemember.setMemberID(memberField.getText());
        newcoursemember.setExamine(YNField.getText());
        // 将新的会员对象保存到数据库中
        try (Connection connection = Druid.getConnection()) {
            searchclassDao coursememberDao = new searchclassDao(connection);
            coursememberDao.insert(newcoursemember);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 清空输入框并刷新表格
        clearFields();
        refreshTableView();
    }

    /**
     * 修改按钮点击事件处理方法
     */
    @FXML
    void change() {
        coursemember selectedcoursemember = courseTableview.getSelectionModel().getSelectedItem();
        if (selectedcoursemember != null) {
            idField.setText(String.valueOf(selectedcoursemember.getID()));
            courseField.setText(selectedcoursemember.getCourseID());
            memberField.setText(String.valueOf(selectedcoursemember.getMemberID()));
            YNField.setText(selectedcoursemember.getExamine());
            // 禁用学号输入框，不允许修改主键
            idField.setDisable(true);
        }
    }

    /**
     * 删除按钮点击事件处理方法
     */
    @FXML
    void del() {
        List<coursemember> selectedcoursemembers = new ArrayList<>(courseTableview.getSelectionModel().getSelectedItems());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的课程信息吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                deleteSelected(selectedcoursemembers);
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 删除选中的班级信息
     *
     * @param selectedcoursemembers 选中的班级信息列表
     * @throws Exception 异常
     */
    private void deleteSelected(List<coursemember> selectedcoursemembers) throws Exception {
        try (Connection connection = Druid.getConnection()) {
            searchclassDao coursememberDao = new searchclassDao(connection);
            for (coursemember coursemember : selectedcoursemembers) {
                coursememberDao.delete(String.valueOf(coursemember.getID()));
            }
        }
    }

    /**
     * 保存按钮点击事件处理方法
     */
    @FXML
    void save() {
        if (selected != null) {
            selected.setCourseID(courseField.getText());
            selected.setMemberID(memberField.getText());
            selected.setExamine(YNField.getText());
            // 更新数据库中的数据
            try (Connection connection = Druid.getConnection()) {
                searchclassDao coursememberDao = new searchclassDao(connection);
                coursememberDao.update(selected); // 调用更新方法将修改后的学生信息保存到数据库
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 清空输入框并刷新表格
            clearFields();
            refreshTableView();
            idField.setDisable(false); // 重新打开会员号输入框
        }
    }

    /**
     * 搜索课程按钮点击事件处理方法
     *
     * @param event 事件对象
     */
    @FXML
    void searchC(ActionEvent event) {
        String courseID = courseField.getText(); // 获取用户输入的课程ID

        try (Connection connection = Druid.getConnection()) {
            searchclassDao searchclassdao = new searchclassDao(connection);
            List<coursemember> searchResults = searchclassdao.selectByCourseID(courseID); // 根据课程ID进行查询

            updateTableView(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        refreshTableView();
    }

    /**
     * 搜索会员按钮点击事件处理方法
     */
    @FXML
    void searchM() {
        String memberID = memberField.getText(); // 获取用户输入的会员ID

        try (Connection connection = Druid.getConnection()) {
            searchclassDao searchclassdao = new searchclassDao(connection);
            List<coursemember> searchResults = searchclassdao.selectByMemberID(memberID); // 根据会员ID进行查询

            updateTableView(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        refreshTableView();
    }

    /**
     * 回复按钮点击事件处理方法
     */
    @FXML
    void reply() {
        try (Connection connection = Druid.getConnection()) {
            searchclassDao searchclassdao = new searchclassDao(connection);
            List<coursemember> searchResults = searchclassdao.selectByExamine("否"); // 根据

            updateTableView(searchResults); // 将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 清空输入框内容
     */
    private void clearFields() {
        idField.clear();
        courseField.clear();
        memberField.clear();
        YNField.clear();
    }

    /**
     * 刷新表格视图数据
     */
    private void refreshTableView() {
        List<coursemember> coursememberList = new ArrayList<>();
        coursememberList = update();
        updateTableView(coursememberList);
    }
}
