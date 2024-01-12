package com.example.fitness_club_management_system;

import JDBC.Druid;
import JDBC.member.member;
import JDBC.member.memberDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import JDBC.personal.personalDao;
import JDBC.course.searchclassDao;
import javafx.stage.Stage;
import java.util.List;

/**
 * 个人信息页面的控制器类。
 */
public class Person {

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField sexField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField CoachField;

    @FXML
    private TextField classField;

    // 添加一个成员变量用于存储当前用户的信息
    private member currentUser;

    // 数据库操作对象
    private memberDAO memberDao;
    private personalDao personaldao;
    private searchclassDao searchclassdao;

    /**
     * 初始化方法，会在界面加载后自动调用。
     */
    public void initialize() {
        try {
            // 获取登录页面传递的用户名和密码
            Logmember logmember = new Logmember();
            String username = logmember.username;
            String password = logmember.password;

            // 初始化数据库操作对象
            memberDao = new memberDAO(Druid.getConnection());
            personaldao = new personalDao(Druid.getConnection());
            searchclassdao = new searchclassDao(Druid.getConnection());

            // 根据邮箱和密码查询用户信息
            currentUser = memberDao.selectByPhoneOrEmailAndPassword(username, password);
            String memberID = currentUser.getID();

            // 查询用户的私人教练和参加的课程ID列表
            List<String> CoachID = personaldao.selectCoachIdByMemberId(memberID);
            List<String> courseID = searchclassdao.selectCourseIDsByMemberID(memberID);

            // 将用户信息显示在界面上
            displayUserInfo(currentUser, CoachID, courseID);
            idField.setEditable(false);
            CoachField.setEditable(false);
            classField.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将用户信息显示在界面上。
     *
     * @param user 用户对象
     * @param a    私人教练ID列表
     * @param b    参加的课程ID列表
     */
    private void displayUserInfo(member user, List<String> a, List<String> b) {
        idField.setText(user.getID());
        nameField.setText(user.getName());
        ageField.setText(String.valueOf(user.getAge()));
        sexField.setText(user.getSex());
        phoneField.setText(user.getPhone());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        CoachField.setText(a.toString());
        classField.setText(b.toString());
    }

    /**
     * 编辑按钮的点击事件处理方法。
     */
    @FXML
    private void change() {
        // 启用文本框，允许编辑
        enableTextFields(true);
    }

    /**
     * 保存按钮的点击事件处理方法。
     */
    @FXML
    private void save() {
        // 更新当前用户的信息
        currentUser.setName(nameField.getText());
        currentUser.setAge(Integer.parseInt(ageField.getText()));
        currentUser.setSex(sexField.getText());
        currentUser.setPhone(phoneField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setAddress(addressField.getText());

        // 保存到数据库
        try {
            memberDao.update(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 禁用文本框，不允许编辑
        enableTextFields(false);
    }

    /**
     * 启用或禁用文本框。
     *
     * @param enable 是否启用文本框
     */
    private void enableTextFields(boolean enable) {
        nameField.setEditable(enable);
        ageField.setEditable(enable);
        sexField.setEditable(enable);
        phoneField.setEditable(enable);
        emailField.setEditable(enable);
        addressField.setEditable(enable);
    }

    /**
     * 申请课程按钮的点击事件处理方法。
     */
    public void reply() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reply.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("申请课程"); // 使用文件名作为标题，首字母大写
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
