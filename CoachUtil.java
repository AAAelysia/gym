package JDBC.coach;

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

public class CoachUtil {
    private int itemsPerPage = 20; // 每页显示的数据条数
    @FXML
    private TableColumn<coach, String> Experience;

    @FXML
    private TableColumn<coach,String> YN;

    @FXML
    private TextField YField;

    @FXML
    private TableColumn<coach,String> Name;

    @FXML
    private TableColumn<coach,String> Specialization;

    @FXML
    private TableColumn<coach,String> age;

    @FXML
    private TableView<coach> coachTableView;

    @FXML
    private TableColumn<coach,String> email;

    @FXML
    private TextField emailField;

    @FXML
    private TextField experienceField;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private TableColumn<coach,String> id;

    @FXML
    private TextField idField;
    @FXML
    private TextField ageField;
    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField nameField;

    @FXML
    private Pagination pageTable;

    @FXML
    private TableColumn<coach,String> phone;

    @FXML
    private TextField phoneField;

    @FXML
    private ToggleGroup rsex;

    @FXML
    private TableColumn<coach,String> sex;

    @FXML
    private TextField specializationField;
    private coach selectedcoach; // 存储当前选中的教练对象
    /**
     * 初始化方法，界面加载时自动调用
     */
    @FXML
    private void initialize() {
        initPagination();
        loadPageData(0);

        coachTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedcoach = newValue;
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
            coachDao coachDao = new coachDao(connection);
            return (int) Math.ceil((double) coachDao.getTotalRecords() / itemsPerPage);
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
            coachDao coachDao = new coachDao(connection);
            java.util.List<coach> pageData = coachDao.selectByPage(pageIndex + 1, itemsPerPage);
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新表格视图数据
     * @param coachs 教练信息列表
     */
    private void updateTableView(List<coach> coachs) {
        coachTableView.getItems().setAll(coachs);
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        age.setCellValueFactory(new PropertyValueFactory<>("Age"));
        sex.setCellValueFactory(new PropertyValueFactory<>("Sex"));
        phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Specialization.setCellValueFactory(new PropertyValueFactory<>("Specialization"));
        Experience.setCellValueFactory(new PropertyValueFactory<>("Experience"));
        YN.setCellValueFactory(new PropertyValueFactory<>("examine"));
        ObservableList<coach> coachList = FXCollections.observableArrayList(coachs);
        coachTableView.setItems(coachList);
    }
    /**
     * 增加教练对象
     */
    @FXML
    void add() {
        // 创建一个新的教练对象
        coach newcoach = new coach();

        // 从输入框中获取用户输入的信息，并设置到新的教练对象中
        newcoach.setID(idField.getText());
        newcoach.setName(nameField.getText());
        if (maleRadioButton.isSelected()) {
            newcoach.setSex("男");
        } else if (femaleRadioButton.isSelected()) {
            newcoach.setSex("女");
        }
        newcoach.setAge(Integer.parseInt(ageField.getText()));
        newcoach.setPhone(phoneField.getText());
        newcoach.setEmail(emailField.getText());
        newcoach.setSpecialization(specializationField.getText());
        newcoach.setExperience(experienceField.getText());
        newcoach.setExamine(YField.getText());
        // 将新的教练对象保存到数据库中
        try (Connection connection = Druid.getConnection()) {
            coachDao coachDao = new coachDao(connection);
            coachDao.insert(newcoach);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 清空输入框并刷新表格
        clearFields();
        refreshTableView();
    }
    /**
     * 删除选中的教练信息
     */
    @FXML
    void btnDel() {
        // 获取选中的教练信息
        List<coach> selectedcoachs = new ArrayList<>(coachTableView.getSelectionModel().getSelectedItems());

        // 弹出确认对话框
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的教练信息吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                deleteSelected(selectedcoachs); // 删除选中的教练信息
                refreshTableView(); // 刷新表格视图
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 删除选中的教练信息
     *
     * @param selectedcoachs 选中的教练信息列表
     * @throws Exception 异常
     */
    private void deleteSelected(List<coach> selectedcoachs) throws Exception {
        try (Connection connection = Druid.getConnection()) {
            coachDao coachDao = new coachDao(connection);
            for (coach coach : selectedcoachs) {
                coachDao.delete(coach.getID());
            }
        }
    }
    /**
     * 显示选中的教练信息到输入框
     *
     * @param event 事件对象
     */
    @FXML
    void change(ActionEvent event) {
        // 获取选中的教练信息
        coach selectedcoach = coachTableView.getSelectionModel().getSelectedItem();
        if (selectedcoach != null) {
            // 将选中的教练信息显示在输入框中
            idField.setText(selectedcoach.getID());
            nameField.setText(selectedcoach.getName());
            if (selectedcoach.getSex().equals("男")) {
                maleRadioButton.setSelected(true);
            } else if (selectedcoach.getSex().equals("女")) {
                femaleRadioButton.setSelected(true);
            }
            ageField.setText(String.valueOf(selectedcoach.getAge()));
            phoneField.setText(selectedcoach.getPhone());
            emailField.setText(selectedcoach.getEmail());
            specializationField.setText(selectedcoach.getSpecialization());
            experienceField.setText(selectedcoach.getExperience());
            YField.setText(selectedcoach.getExamine());
            // 禁用学号输入框，不允许修改主键
            idField.setDisable(true);
        }
    }
    /**
     * 处理查询按钮点击事件
     */
    @FXML
    void handleSearchButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("search2.fxml"));
            Parent root = loader.load();

            Search2 controller = loader.getController(); // 获取查询数据界面的 Controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("查询数据");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            controller.search_2(); // 执行查询操作，将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 保存修改后的教练信息
     */
    @FXML
    void saveChanges() {
        if (selectedcoach != null) {
            // 更新选中的教练信息
            selectedcoach.setName(nameField.getText());
            selectedcoach.setAge(Integer.parseInt(ageField.getText()));
            selectedcoach.setPhone(phoneField.getText());
            if (maleRadioButton.isSelected()) {
                selectedcoach.setSex("男");
            } else if (femaleRadioButton.isSelected()) {
                selectedcoach.setSex("女");
            }
            selectedcoach.setEmail(emailField.getText());
            selectedcoach.setSpecialization(specializationField.getText());
            selectedcoach.setExperience(experienceField.getText());
            selectedcoach.setExamine(YField.getText());
            // 更新数据库中的数据
            try (Connection connection = Druid.getConnection()) {
                coachDao coachDao = new coachDao(connection);
                coachDao.update(selectedcoach); // 调用更新方法将修改后的教练信息保存到数据库
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
     * 重新加载未审核的教练信息
     *
     * @param event 事件对象
     */
    @FXML
    void replay(ActionEvent event) {
        try (Connection connection=Druid.getConnection()){
            coachDao coachdao=new coachDao(connection);
            // 获取符合条件的个人课程信息
            List<coach> coaches = coachdao.selectByExamine("否"); // 这里假设是传入的参数为 "是"
            // 更新表格视图数据
            updateTableView(coaches);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常
        }
    }
    /**
     * 清空输入框
     */
    private void clearFields() {
        // 清空输入框
        idField.clear();
        nameField.clear();
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        phoneField.clear();
        ageField.clear();
        emailField.clear();
        specializationField.clear();
        experienceField.clear();
        YField.clear();
    }
    /**
     * 刷新表格视图
     */
    private void refreshTableView() {
        int currentPageIndex = pageTable.getCurrentPageIndex();
        int newTotalPages = calculatePageCount();
        pageTable.setPageCount(newTotalPages);
        loadPageData(currentPageIndex);
        if (coachTableView.getItems().isEmpty() && currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);
        }
    }

}
