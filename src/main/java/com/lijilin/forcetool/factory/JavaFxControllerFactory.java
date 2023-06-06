package com.lijilin.forcetool.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName JavaFxControllerFactory
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 13:26
 * @Version 1.0
 */
public class JavaFxControllerFactory {

    /**
     * home的Controller
     */
    public static final int HOME_KEY=1;
    public static final int SETTING_KEY=2;


    private static final Map<Integer, Object> controllers = new HashMap<>();

    /**
     * 注册 JavaFX Controller
     * @param controllerName Controller 名称
     * @param controllerInstance Controller 实例
     */
    public static void registerController(Integer controllerName, Object controllerInstance) {
        controllers.put(controllerName, controllerInstance);
    }

    /**
     * 获取 JavaFX Controller
     * @param controllerName Controller 名称
     * @return Controller 实例
     */
    public static Object getController(int controllerName) {
        return controllers.get(controllerName);
    }

    /**
     * 判断 JavaFX Controller 是否已注册
     * @param controllerName Controller 名称
     * @return 是否已注册
     */
    public static boolean isControllerRegistered(int controllerName) {
        return controllers.containsKey(controllerName);
    }
}
