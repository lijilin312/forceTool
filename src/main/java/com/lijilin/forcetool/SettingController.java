package com.lijilin.forcetool;

import com.lijilin.forcetool.config.AppConfig;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import com.lijilin.forcetool.utils.DataFillUtil;
import com.lijilin.forcetool.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable
{


    @FXML
    private TextField modePath;

    @FXML
    void modleButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择模版数据的位置");
        File file = directoryChooser.showDialog(stage);
        if (file == null)
        {
            return;
        }
        modePath.setText(file.getPath());

    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        JavaFxControllerFactory.registerController(JavaFxControllerFactory.SETTING_KEY, this);
        modePath.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("文本字段中的文本已更改： " + newValue);
            StringBuilder builder=new StringBuilder("modePath-");
            builder.append(newValue);
            //持久化设置
            String settingPath = AppConfig.getInstance().getSettingPath();
            FileUtils.save(settingPath,builder.toString());
            // 在此处添加您想要执行的代码
            HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
            if (controller!=null)
            {
                controller.initStencilTreeView();
            }
        });
    }

    public TextField getModePath()
    {
        return modePath;
    }
}
