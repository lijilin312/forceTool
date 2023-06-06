package com.lijilin.forcetool.config;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @ClassName AppConfig
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 10:18
 * @Version 1.0
 */
public class AppConfig {


    /**
     * 试验机型号
     */
//    private List<String> model;

    private Map<String, String> modelMap = new HashMap<>();
    /**
     * 设置的路径
     */
    private String settingPath;
    /**
     * 保存的默认路径
     */
    private String savePath;

    // 私有化构造方法
    private AppConfig() {

    }

    // 使用静态内部类实现线程安全的单例模式
    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    // 获取单例实例
    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 加载配置
     *
     * @throws IOException
     */
    public void loadPropertiesFile() throws IOException {


        Properties properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("application.properties");
        properties.load(new InputStreamReader(in, "UTF-8"));

        String property = properties.getProperty("model");

        String[] split = property.split(",");
        modelMap.clear(); // 清空 model 列表
        for (String s : split) {

            String[] sp = s.split("\\^");

            modelMap.put(sp[0], sp[1]);
        }
        settingPath = properties.getProperty("settingPath");
        savePath = properties.getProperty("" +
                "savePath");
    }

    // 线程安全的访问和修改 model 列表的方法


    public Map<String, String> getModelMap() {
        return modelMap;
    }

    public String getSettingPath() {
        return settingPath;
    }

    public String getSavePath() {
        return savePath;
    }
}
