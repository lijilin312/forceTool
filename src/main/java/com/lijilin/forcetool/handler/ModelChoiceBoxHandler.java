package com.lijilin.forcetool.handler;

import com.lijilin.forcetool.HomeController;
import com.lijilin.forcetool.config.AppConfig;
import com.lijilin.forcetool.entity.ForceTxtData;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;

import java.util.Map;

/**
 * @ClassName ModelChoiceBoxHandler
 * @Description
 * @Author Administrator
 * @Time 2023/4/8 22:25
 * @Version 1.0
 */
public class ModelChoiceBoxHandler implements EventHandler<ActionEvent> {


    @Override
    public void handle(ActionEvent event) {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) event.getSource(); // 获取 ChoiceBox 对象
        String selectedValue = choiceBox.getValue(); // 获取选中的值
        System.out.println("Selected value: " + selectedValue); // 在控制台中打印选中的值
        HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
        if (controller == null) {
            return;
        }
        ForceTxtData randForceTxtData = controller.getRandForceTxtData();
        Map<String, String> modelMap = AppConfig.getInstance().getModelMap();
        if (modelMap.containsKey(selectedValue)) {
            randForceTxtData.setModel(modelMap.get(selectedValue));
        }
    }
}
