package com.example.fitness_club_management_system;

import JDBC.Druid;
import JDBC.member.member;
import JDBC.member.memberDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

/**
 * 控制会员登录界面的逻辑。
 */
public class Logmember {
    public static String username;
    public static String password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private memberDAO memberDao; // 会员数据库操作对象

    /**
     * 初始化方法，在界面加载时调用。
     */
    @FXML
    void initialize() {
        try {
            Connection connection = Druid.getConnection(); // 获取数据库连接
            memberDao = new memberDAO(connection); // 创建会员数据库操作对象
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录会员账户。
     */
    @FXML
    void login() {
        try{
            String username = usernameField.getText();
            String password = passwordField.getText();
            Logmember.username=username;
            Logmember.password=password;
            member member = memberDao.selectByPhoneOrEmailAndPassword(username, password);
            if (member != null) {
                // 获取当前窗口
                Stage stage = (Stage) usernameField.getScene().getWindow();

                // 加载会员个人中心界面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("person.fxml"));
                Parent root = loader.load();

                // 创建新的窗口
                Stage systemStage = new Stage();
                systemStage.setScene(new Scene(root));
                systemStage.setTitle("会员个人中心");
                systemStage.initModality(Modality.APPLICATION_MODAL);
                systemStage.show();

                // 关闭登录窗口
                stage.close();
            }else {
                // 用户名或密码错误，弹出提示框
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("用户名或密码不正确");
                alert.setContentText("请重新输入");
                alert.showAndWait();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 切换到管理员登录界面。
     */
    @FXML
    void loginG() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // 加载管理员登录界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("log.fxml"));
            Parent root = loader.load();

            // 创建新的窗口
            Stage systemStage = new Stage();
            systemStage.setScene(new Scene(root));
            systemStage.setTitle("管理员登录");
            systemStage.initModality(Modality.APPLICATION_MODAL);
            systemStage.show();

            // 关闭登录窗口
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
