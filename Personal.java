package JDBC.personal;


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
 * 私教管理工具类
 */
public class Personal {
    private int itemsPerPage = 20; // 每页显示的数据条数
    @FXML
    private TableColumn<personally,String> CoachID;

    @FXML
    private TableColumn<personally,String> ID;

    @FXML
    private TableColumn<personally,String> MemberID;

    @FXML
    private RadioButton NButton;

    @FXML
    private RadioButton YButton;

    @FXML
    private TableColumn<personally,String> begin;

    @FXML
    private DatePicker beginField;

    @FXML
    private TextField coachField;

    @FXML
    private TableColumn<personally,String> end;

    @FXML
    private DatePicker endField;

    @FXML
    private TableColumn<personally,String> examine;

    @FXML
    private TextField idField;

    @FXML
    private TextField memberField;

    @FXML
    private Pagination pageTable;

    @FXML
    private TableView<personally> personalTableView;

    @FXML
    private TableColumn<personally,String> price;

    @FXML
    private TextField priceField;

    @FXML
    private TableColumn<personally,String> time;

    @FXML
    private TextField timeField;

    @FXML
    private ToggleGroup yn;
    private personally selected; // 存储当前选中的私教对象
    /**
     * 初始化方法，界面加载时自动调用
     */
    @FXML
    private void initialize() {
        initPagination();
        loadPageData(0);

        personalTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selected = newValue;
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
            personalDao personaldao = new personalDao(connection);
            return (int) Math.ceil((double) personaldao.getTotalRecords() / itemsPerPage);
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
            personalDao personaldao = new personalDao(connection);
            java.util.List<personally> pageData = personaldao.selectByPage(pageIndex + 1, itemsPerPage);
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新表格视图数据
     * @param personallies 私教信息列表
     */
    private void updateTableView(List<personally> personallies) {
        personalTableView.getItems().setAll(personallies);
        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        CoachID.setCellValueFactory(new PropertyValueFactory<>("CoachID"));
        MemberID.setCellValueFactory(new PropertyValueFactory<>("MemberID"));
        begin.setCellValueFactory(new PropertyValueFactory<>("begin_"));
        end.setCellValueFactory(new PropertyValueFactory<>("end_"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        time.setCellValueFactory(new PropertyValueFactory<>("time_"));
        examine.setCellValueFactory(new PropertyValueFactory<>("examine"));
        ObservableList<personally> personallies1 = FXCollections.observableArrayList(personallies);
        personalTableView.setItems(personallies1);
    }
    /**
     * 处理 "添加" 按钮的点击事件，将用户输入的私教信息添加到数据库中
     */
    @FXML
    void add() {
        // 创建一个新的私教对象
        personally newpersonal = new personally();

        // 从输入框中获取用户输入的信息，并设置到新的私教对象中
        newpersonal.setID(idField.getText());
        newpersonal.setCoachID(coachField.getText());
        newpersonal.setMemberID(memberField.getText());
        newpersonal.setBegin_(beginField.getValue());
        newpersonal.setEnd_(endField.getValue());
        newpersonal.setPrice(Double.valueOf(priceField.getText()));
        newpersonal.setTime_(timeField.getText());
        if (YButton.isSelected()) {
            newpersonal.setExamine("是");
        } else if (NButton.isSelected()) {
            newpersonal.setExamine("否");
        }

        // 将新的会员对象保存到数据库中
        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            personaldao.insert(newpersonal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 清空输入框并刷新表格
        clearFields();
        refreshTableView();
    }
    /**
     * 处理 "删除" 按钮的点击事件，删除用户选择的私教信息
     */
    @FXML
    void btndel(ActionEvent event) {
        List<personally> selected = new ArrayList<>(personalTableView.getSelectionModel().getSelectedItems());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的私教信息吗？");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                deleteSelected(selected);
                refreshTableView();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    /**
     * 删除用户选择的私教信息
     *
     * @param selected 用户选择的会员信息列表
     * @throws Exception 如果删除操作发生异常，则抛出异常
     */
    private void deleteSelected(List<personally> selected) throws Exception {
        try (Connection connection = Druid.getConnection()) {
            personalDao personaldao = new personalDao(connection);
            for (personally personally : selected) {
                personaldao.delete(personally.getID());
            }
        }
    }
    /**
     * 处理 "修改" 按钮的点击事件，将用户选择的私教信息显示在输入框中，允许用户进行修改
     */
    @FXML
    void change() {
        personally selected = personalTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            idField.setText(selected.getID());
            coachField.setText(selected.getCoachID());
            memberField.setText(selected.getMemberID());
            beginField.setValue(selected.getBegin_());
            endField.setValue(selected.getEnd_());
            priceField.setText(String.valueOf(selected.getPrice()));
            timeField.setText(selected.getTime_());
            if (selected.getExamine().equals("是")) {
                YButton.setSelected(true);
            } else if (selected.getExamine().equals("否")) {
                NButton.setSelected(true);
            }
            // 禁用私教输入框，不允许修改主键
            idField.setDisable(true);
        }
    }
    /**
     * 审核方法，用于获取符合条件的个人课程信息并更新表格视图数据
     */
    @FXML
    void examined() {
        try (Connection connection=Druid.getConnection()){
            personalDao personaldao=new personalDao(connection);
            // 获取符合条件的个人课程信息
            List<personally> personallies = personaldao.selectByExamine("否"); // 这里假设是传入的参数为 "是"
            // 更新表格视图数据
            updateTableView(personallies);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常
        }
    }
    /**
     * 处理 "保存更改" 按钮的点击事件，保存用户对私教信息的修改到数据库中
     */
    @FXML
    void save() {
        if (selected != null) {
            selected.setCoachID(coachField.getText());
            selected.setMemberID(memberField.getText());
            selected.setBegin_(beginField.getValue());
            selected.setEnd_(endField.getValue());
            selected.setPrice(Double.valueOf(priceField.getText()));
            selected.setTime_(timeField.getText());
            if (YButton.isSelected()) {
                selected.setExamine("男");
            } else if (NButton.isSelected()) {
                selected.setExamine("女");
            }
            // 更新数据库中的数据
            try (Connection connection = Druid.getConnection()) {
                personalDao personaldao = new personalDao(connection);
                personaldao.update(selected); // 调用更新方法将修改后的私教信息保存到数据库
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 清空输入框并刷新表格
            clearFields();
            refreshTableView();
            idField.setDisable(false); // 重新打开私教号输入框
        }
    }
    /**
     * 处理 "查询" 按钮的点击事件，打开查询界面执行查询操作
     */
    @FXML
    public void search() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("search4.fxml"));
            Parent root = loader.load();

            Search4 controller = loader.getController(); // 获取查询数据界面的 Controller

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("查询数据");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

            controller.search_4(); // 执行查询操作，将查询结果展示在表格视图上
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 清空输入框中的内容
     */
    private void clearFields() {
        idField.clear();
        coachField.clear();
        memberField.clear();
        beginField.setValue(null);
        endField.setValue(null);
        priceField.clear();
        timeField.clear();
        YButton.setSelected(false);
        NButton.setSelected(false);
    }
    /**
     * 刷新表格视图，根据当前页数重新加载数据
     */
    private void refreshTableView() {
        int currentPageIndex = pageTable.getCurrentPageIndex();
        int newTotalPages = calculatePageCount();
        pageTable.setPageCount(newTotalPages);
        loadPageData(currentPageIndex);
        if (personalTableView.getItems().isEmpty() && currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);
        }
    }
}
