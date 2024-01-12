package com.example.fitness_club_management_system;

import JDBC.Druid;
import JDBC.member.member;
import JDBC.member.memberDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import JDBC.coach.coach;
import JDBC.coach.coachDao;
import JDBC.course.Course;
import JDBC.course.CourseDao;
import javafx.scene.control.cell.PropertyValueFactory;
import JDBC.course.coursemember;
import JDBC.course.searchclassDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import JDBC.personal.personalDao;
import JDBC.personal.personally;

/**
 * 申请课程类，用于管理reply。fxml文件
 */
public class Reply {

    @FXML
    private TableColumn<Course, String> Cname;

    @FXML
    private TextField CourseField;

    @FXML
    private TextField coachField;

    @FXML
    private TableView<coach> coachTableview;

    @FXML
    private TableColumn<coach, String> coachiD;

    @FXML
    private TableColumn<Course, String> courseID;

    @FXML
    private TableView<Course> courseTableview;

    @FXML
    private TableColumn<Course, String> name;

    // 当前用户
    private member currentUser;

    /**
     * 初始化方法，会在界面加载后自动调用。
     */
    @FXML
    void initialize() {
        try {
            // 初始化数据库操作对象
            CourseDao courseDao = new CourseDao(Druid.getConnection());
            List<Course> courses = new ArrayList<>();
            courses = courseDao.selectByExamine("是");
            coachDao coachdao = new coachDao(Druid.getConnection());
            List<coach> coaches = new ArrayList<>();
            coaches = coachdao.selectByExamine("是");

            // 设置表格列的属性值
            courseID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            name.setCellValueFactory(new PropertyValueFactory<>("Name"));
            coachiD.setCellValueFactory(new PropertyValueFactory<>("ID"));
            Cname.setCellValueFactory(new PropertyValueFactory<>("Name"));

            // 将数据设置到表格中
            courseTableview.setItems(FXCollections.observableArrayList(courses));
            coachTableview.setItems(FXCollections.observableArrayList(coaches));

            // 获取当前登录用户信息
            memberDAO memberDao = new memberDAO(Druid.getConnection());
            Logmember logmember = new Logmember();
            String username = logmember.username;
            String password = logmember.password;
            currentUser = memberDao.selectByPhoneOrEmailAndPassword(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约课程按钮的点击事件处理方法。
     */
    @FXML
    void reply_1() {
        try {
            // 初始化数据库操作对象
            searchclassDao searchclassdao = new searchclassDao(Druid.getConnection());

            // 创建课程成员对象
            coursemember course = new coursemember();
            course.setID(searchclassdao.getCount() + 1);
            course.setCourseID(CourseField.getText());
            course.setMemberID(currentUser.getID());
            course.setExamine("否");

            // 插入课程成员记录
            searchclassdao.insert(course);

            // 显示预约成功的弹窗
            showSuccessAlert("课程预约成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约私教按钮的点击事件处理方法。
     */
    @FXML
    void reply_2() {
        try {
            // 初始化数据库操作对象
            personalDao personaldao = new personalDao(Druid.getConnection());

            // 创建私教记录对象
            personally personal = new personally();
            personal.setID(String.valueOf(personaldao.getTotalRecords() + 1));
            personal.setCoachID(coachField.getText());
            personal.setMemberID(currentUser.getID());
            LocalDate currentDate = LocalDate.now();
            personal.setBegin_(currentDate);
            personal.setEnd_(currentDate.plusYears(1));
            personal.setPrice(0.0);
            personal.setTime_("");
            personal.setExamine("否");

            // 插入私教记录
            personaldao.insert(personal);

            // 显示预约成功的弹窗
            showSuccessAlert("私教预约成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示预约成功的弹窗。
     *
     * @param message 提示信息
     */
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("预约成功");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
