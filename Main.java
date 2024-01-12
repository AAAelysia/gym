package com.example.fitness_club_management_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 主类，用于启动应用程序。
 */
public class Main extends Application {
    /**
     * 启动应用程序的入口方法。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动 JavaFX 应用程序
        launch(args);
    }

    /**
     * JavaFX应用程序的启动方法。
     *
     * @param stage 舞台对象
     * @throws IOException 当加载FXML文件发生错误时抛出
     */
    @Override
    public void start(Stage stage) throws IOException {
        // 加载FXML文件并获取根节点
        Parent parent = new FXMLLoader(Main.class.getResource("log.fxml")).load();

        // 创建场景并将根节点添加到场景中
        Scene scene = new Scene(parent);

        // 设置舞台的场景和标题，并显示舞台
        stage.setScene(scene);
        stage.setTitle("管理员登录");
        stage.show();
    }
}
