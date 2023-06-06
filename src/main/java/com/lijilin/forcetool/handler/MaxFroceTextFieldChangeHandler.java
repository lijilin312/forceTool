package com.lijilin.forcetool.handler;

import com.lijilin.forcetool.HomeController;
import com.lijilin.forcetool.entity.ForceTxtData;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * @ClassName UidTextFieldChangeHandler
 * @Description
 * @Author Administrator
 * @Time 2023/4/8 14:42
 * @Version 1.0
 */
public class MaxFroceTextFieldChangeHandler implements ChangeListener<String> {

    private TextField textField;


    public MaxFroceTextFieldChangeHandler(TextField textField) {
        this.textField = textField;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Text changed from " + oldValue + " to " + newValue);
        // 执行自定义逻辑
        HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
        if (controller!=null)
        {

            ForceTxtData randForceTxtData = controller.getRandForceTxtData();
            randForceTxtData.setMaxForce(newValue);
            controller.showTabRandomData();
        }
        if (textField.getText().equals(""))
        {
            textField.setStyle("-fx-background-color: white;");
        }else {
            textField.setStyle("-fx-background-color: yellow;");
        }

    }
}
