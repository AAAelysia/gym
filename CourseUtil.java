package JDBC.course;

import JDBC.Druid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * CourseUtil类是一个JavaFX的课程管理界面的控制器类，使用JDBC进行数据库操作。
 * 界面包括表格视图，用于显示课程信息，并提供添加、删除、修改和查询功能。
 */
public class CourseUtil {
    private int itemsPerPage = 20; // 每页显示的数据条数
    @FXML
    private TextField CoachidField;

    @FXML
    private TableColumn<Course, String> Name;

    @FXML
    private TableView<Course> courseTableView;

    @FXML
    private TableColumn<Course, String> coachid;

    @FXML
    private TableColumn<Course, String> description;

    @FXML
    private TextField descriptionField;

    @FXML
    private TableColumn<Course, String> duration;

    @FXML
    private TextField durationField;

    @FXML
    private TableColumn<Course, String> id;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Course, String> number;

    @FXML
    private TextField numberField;

    @FXML
    private Pagination pageTable;

    @FXML
    private TableColumn<Course, String> price;

    @FXML
    private TextField priceField;

    @FXML
    private TableColumn<Course, String> examine;

    @FXML
    private TextField examineField;

    @FXML
    private TableColumn<Course, String> type;

    @FXML
    private TextField typeField;
    private Course selectedCourse; // 存储当前选中的会员对象
    /**
     * 初始化方法，界面加载时自动调用。
     * 初始化分页控件，并加载第一页的数据。
     */
    @FXML
    private void initialize() {
        initPagination();
        loadPageData(0);

        courseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedCourse = newValue;
        });
    }
    /**
     * 初始化分页组件。
     * 设置总页数和当前页索引的监听器。
     */
    private void initPagination() {
        pageTable.setPageCount(calculatePageCount());
        pageTable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            loadPageData(newValue.intValue());
        });
    }
    /**
     * 计算总页数
     * @return 总页数
     */
    private int calculatePageCount() {
        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);
            return (int) Math.ceil((double) courseDao.getTotalRecords() / itemsPerPage);
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
            CourseDao courseDao = new CourseDao(connection);
            java.util.List<Course> pageData = courseDao.selectByPage(pageIndex + 1, itemsPerPage);
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新表格视图数据
     * @param courses 会员信息列表
     */
    private void updateTableView(List<Course> courses) {
        courseTableView.getItems().setAll(courses);
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        coachid.setCellValueFactory(new PropertyValueFactory<>("CoachID"));
        type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        duration.setCellValueFactory(new PropertyValueFactory<>("Duration"));
        description.setCellValueFactory(new PropertyValueFactory<>("Description"));
        examine.setCellValueFactory(new PropertyValueFactory<>("examine"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
        courseTableView.setItems(courseList);
    }
    /**
     * 添加课程。
     * 创建一个新的课程对象，从输入框中获取用户输入的信息，并设置到新的课程对象中。
     * 将新的课程对象保存到数据库中，清空输入框并刷新表格。
     */
    @FXML
    void add() {
        // 创建一个新的课程对象
        Course newcourse = new Course();

        // 从输入框中获取用户输入的信息，并设置到新的课程对象中
        newcourse.setID(idField.getText());
        newcourse.setName(nameField.getText());
        newcourse.setCoachID(CoachidField.getText());
        newcourse.setType(typeField.getText());
        newcourse.setDuration(Integer.parseInt(durationField.getText()));
        newcourse.setDescription(descriptionField.getText());
        newcourse.setExamine(examineField.getText());
        newcourse.setPrice(Integer.parseInt(priceField.getText()));
        newcourse.setNumber(Integer.parseInt(numberField.getText()));

        // 将新的课程对象保存到数据库中
        try (Connection connection = Druid.getConnection()) {
            CourseDao courseDao = new CourseDao(connection);
            courseDao.insert(newcourse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 清空输入框并刷新表格
        clearFields();
        refreshTableView();
    }
    /**
     * 删除选中的课程。
     * 弹出一个确认对话框，如果用户点击了确认按钮，则删除选中的课程信息，并刷新表格。
     */
    @FXML
    void btnDel() {
        List<Course> selectedcourse = new ArrayList<>(courseTableView.getSelectionModel().getSelectedItems());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的课程信息吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                deleteSelected(selectedcourse);
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 删除选中的课程。
     *
     * @param selectedcourse 要删除的课程列表
     * @throws Exception 删除过程中的异常
     */
    private void deleteSelected(List<Course> selectedcourse) throws Exception {
        try (Connection connection = Druid.getConnection()) {
            CourseDao CourseDao = new CourseDao(connection);
            for (Course course : selectedcourse) {
                CourseDao.delete(course.getID());
            }
        }
    }
    /**
     * 修改选中的课程。
     * 将选中的课程的信息显示在输入框中，并禁用课程编号的输入框。
     */
    @FXML
    void change() {
        Course selectedcourse = courseTableView.getSelectionModel().getSelectedItem();
        if (selectedcourse != null) {
            idField.setText(selectedcourse.getID());
            nameField.setText(selectedcourse.getName());
            CoachidField.setText(selectedcourse.getCoachID());
            typeField.setText(selectedcourse.getType());
            durationField.setText(String.valueOf(selectedcourse.getDuration()));
            descriptionField.setText(selectedcourse.getDescription());
            examineField.setText(selectedcourse.getExamine());
            priceField.setText(String.valueOf(selectedcourse.getPrice()));
            numberField.setText(String.valueOf(selectedcourse.getNumber()));
            // 禁用学号输入框，不允许修改主键
            idField.setDisable(true);
        }
    }
    /**
     * 搜索按钮的事件处理程序。
     * 加载并显示查询数据的界面。
     */
    @FXML
    void handleSearchButton() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("search3.fxml"));
            Parent root = loader.load();

            Search3 controller = loader.getController(); // 获取查询数据界面的 Controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("查询数据");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            controller.search_3(); // 执行查询操作，将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存修改的课程信息。
     * 获取用户在输入框中修改后的课程信息，更新数据库中的数据，并刷新表格。
     */
    @FXML
    void saveChanges() {
        if (selectedCourse != null) {
            selectedCourse.setName(nameField.getText());
            selectedCourse.setCoachID(CoachidField.getText());
            selectedCourse.setType(typeField.getText());
            selectedCourse.setDuration(Integer.parseInt(durationField.getText()));
            selectedCourse.setDescription(descriptionField.getText());
            selectedCourse.setExamine(examineField.getText());
            selectedCourse.setPrice(Integer.parseInt(priceField.getText()));
            selectedCourse.setNumber(Integer.parseInt(numberField.getText()));

            // 更新数据库中的数据
            try (Connection connection = Druid.getConnection()) {
                CourseDao courseDao = new CourseDao(connection);
                courseDao.update(selectedCourse); // 调用更新方法将修改后的课程信息保存到数据库
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 清空输入框并刷新表格
            clearFields();
            refreshTableView();
            idField.setDisable(false); // 重新打开课程号输入框
        }
    }

    /**
     * 打开班级管理界面。
     * 加载并显示班级管理界面。
     */
    @FXML
    public void searchclass() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("searchclass.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("班级管理");
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 省略其他私有辅助方法

    /**
     * 清空输入框中的数据。
     */
    private void clearFields() {
        idField.clear();
        nameField.clear();
        CoachidField.clear();
        typeField.clear();
        durationField.clear();
        descriptionField.clear();
        examineField.clear();
        priceField.clear();
        numberField.clear();
    }

    /**
     * 刷新表格视图中的数据。
     * 更新分页控件的总页数和当前页索引，并重新加载当前页的数据。
     * 如果当前页没有数据且不是第一页，则加载前一页的数据。
     */
    private void refreshTableView() {
        int currentPageIndex = pageTable.getCurrentPageIndex();
        int newTotalPages = calculatePageCount();
        pageTable.setPageCount(newTotalPages);
        loadPageData(currentPageIndex);
        if (courseTableView.getItems().isEmpty() && currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);
        }
    }
}
