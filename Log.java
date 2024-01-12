package com.example.fitness_club_management_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 控制登录界面的逻辑。
 */
public class Log {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private final String realname="guanliyuan666";
    private final String realpassword="abc336699";

    /**
     * 登录管理员账户。
     */
    @FXML
    void login() {
        try{
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.equals(realname) && password.equals(realpassword)) {
                // 获取当前窗口
                Stage stage = (Stage) usernameField.getScene().getWindow();

                // 加载管理员目录界面
                FXMLLoader loader = new FXMLLoader(getClass().getResource("gym.fxml"));
                Parent root = loader.load();

                // 创建新的窗口
                Stage systemStage = new Stage();
                systemStage.setScene(new Scene(root));
                systemStage.setTitle("管理员目录");
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
     * 登录会员账户。
     */
    @FXML
    void loginmember() {
        try {
            // 获取当前窗口
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // 加载会员登录界面
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logmember.fxml"));
            Parent root = loader.load();

            // 创建新的窗口
            Stage systemStage = new Stage();
            systemStage.setScene(new Scene(root));
            systemStage.setTitle("会员登录");
            systemStage.initModality(Modality.APPLICATION_MODAL);
            systemStage.show();

            // 关闭登录窗口
            stage.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
