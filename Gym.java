package com.example.fitness_club_management_system;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gym类用于展示健身俱乐部不同模块的界面。
 */
public class Gym {

    /**
     * 显示指定页面。
     *
     * @param pageName 页面文件名，应放在resource目录下。
     * @param title 页面标题，首字母大写。
     */
    public void showPage(String pageName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示会员模块界面。
     */
    public void showPage1() {
        showPage("member.fxml", "会员");
    }

    /**
     * 显示教练模块界面。
     */
    public void showPage2() {
        showPage("coach.fxml", "教练");
    }

    /**
     * 显示器材模块界面。
     */
    public void showPage3() {
        showPage("equipment.fxml", "器材");
    }

    /**
     * 显示私教模块界面。
     */
    public void showPage4() {
        showPage("personal.fxml", "私教");
    }

    /**
     * 显示课程模块界面。
     */
    public void showPage5() {
        showPage("course.fxml", "课程");
    }
}
