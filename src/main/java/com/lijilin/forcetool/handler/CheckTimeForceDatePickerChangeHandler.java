package com.lijilin.forcetool.handler;

import com.lijilin.forcetool.HomeController;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName UidTextFieldChangeHandler
 * @Description
 * @Author Administrator
 * @Time 2023/4/8 14:42
 * @Version 1.0
 */
public class CheckTimeForceDatePickerChangeHandler implements ChangeListener<LocalDate> {

    private DatePicker picker;


    public CheckTimeForceDatePickerChangeHandler(DatePicker textField) {
        this.picker = textField;
    }

    @Override
    public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        // 在此处添加处理代码
        HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
        if (newValue!=null)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy'/'MM'/'dd");
            String format = newValue.format(formatter);
            System.out.println("日期已更改为：" + format);
            controller.getRandForceTxtData().setData(format);

        }else {
            controller.getRandForceTxtData().setData("");
        }
        controller.showTabRandomData();

    }

}
