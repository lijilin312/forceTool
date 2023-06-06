package com.lijilin.forcetool;

import com.lijilin.forcetool.config.AppConfig;
import com.lijilin.forcetool.utils.FileUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("试验力时间曲线生成工具");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/force-64.png")));
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void init() throws Exception {
        AppConfig.getInstance().loadPropertiesFile();
        //初始化设置
        String settingPath = AppConfig.getInstance().getSettingPath();
        FileUtils.checkCreatFile(settingPath);
        FileUtils.creatFile(AppConfig.getInstance().getSavePath());
    }

    public static void main(String[] args) {
        launch();
    }
}