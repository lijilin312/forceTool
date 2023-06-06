package com.lijilin.forcetool.utils;

import com.lijilin.forcetool.SettingController;
import com.lijilin.forcetool.config.AppConfig;
import com.lijilin.forcetool.entity.ForceData;
import com.lijilin.forcetool.entity.ForceTxtData;
import com.lijilin.forcetool.entity.TreeViewData;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


/**
 * @ClassName 数据填充的帮助诶
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 10:41
 * @Version 1.0
 */
public class DataFillUtil {


    /**
     * 填充试验机型号
     */
    public static void initModel(ChoiceBox<String> mode) {
        Map<String, String> modelMap = AppConfig.getInstance().getModelMap();
        mode.getItems().addAll(modelMap.keySet());
        mode.getSelectionModel().selectFirst();
    }

    /**
     * 初始化试验力模版数据
     *
     * @param stencilTreeView
     */
    public static void initStencilTreeView(TreeView<TreeViewData> stencilTreeView) {
//        URL data = DataFillUtil.class.getClassLoader().getResource("data");
        //模版数据
        TreeViewData rootData = new TreeViewData("试验力测试参考数据");
        TreeItem<TreeViewData> rootItem = new TreeItem<>(rootData);
        stencilTreeView.setRoot(rootItem);
        rootItem.setExpanded(true);
        // Set tree view to be editable
        stencilTreeView.setEditable(true);
        SettingController controller = (SettingController) JavaFxControllerFactory.getController(JavaFxControllerFactory.SETTING_KEY);

        String settingPath = AppConfig.getInstance().getSettingPath();
        List<String> read = FileUtils.read(settingPath);
        if (read.isEmpty())
        {
            return;
        }
        String s = read.get(0);
        if (s.contains("modePath"))
        {
            String[] split = s.split("-");
            DataFillUtil.loadTreeView(rootItem, new File(split[1]));
        }

//        DataFillUtil.loadTreeView(rootItem,new File("F:\\外快\\力值修改工具\\数据上传模板"));

    }

    /**
     * 加载树形文件
     *
     * @param parent
     * @param file
     */
    public static void loadTreeView(TreeItem<TreeViewData> parent, File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File childFile : files) {
                TreeItem<TreeViewData> childItem = new TreeItem<>(new TreeViewData(childFile.getName(), childFile.getPath()));
                parent.getChildren().add(childItem);
                if (childFile.isDirectory()) {
                    loadTreeView(childItem, childFile);
                }
            }
        }
    }

    /**
     * 查找元素出现的位置
     *
     * @param arr
     * @param element
     * @return
     */
    public static int findElement(double[] arr, double element) {
        return IntStream.range(0, arr.length)
                .filter(i -> arr[i] == element)
                .findFirst()
                .orElse(-1);
    }

    public static int firstIndexOfElement(double[] arr, double element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element) {
                return i;
            }
        }
        return -1; // 如果元素不在数组中，返回 -1
    }





    public static void main(String[] args) {

    }

    /**
     * 时间解码
     */
    public static String timeDecode(String time) {
        String code = time.replace("\\", "");
        Pattern pattern = Pattern.compile("(\\d{4})'c4'ea(\\d{1,2})'d4'c2(\\d{1,2})'c8'd5");
        Matcher matcher = pattern.matcher(code);
        StringBuilder timeBuild=new StringBuilder();
        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            timeBuild.append(year);
            timeBuild.append("年");
            timeBuild.append(month);
            timeBuild.append("月");
            timeBuild.append(day);
            timeBuild.append("日");
        }

        return timeBuild.toString();
    }
    /**
     * 时间加码处理
     * @param originalString
     * @return
     */
    public static String timeEncode(String originalString) {
        String patternStr = "(\\d{4})/(\\d{1,2})/(\\d{1,2})";
        String replaceStr = "$1\\'c4\\'ea$2\\'d4\\'c2$3\\'c8\\'d5";

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(originalString);
        String convertedStr = matcher.replaceAll(replaceStr);
        convertedStr = convertedStr.replaceAll("'", "\\\\'");

        System.out.println(convertedStr);

        return convertedStr;
    }

    /**
     * 导出保存数据
     * @return
     */
    public static boolean export(String modelPath, ForceTxtData randForceData)
    {
        File file = new File(modelPath);
        BufferedReader reader = null;
        StringBuilder builder=new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String lineText = null;
            int linNumber = 1;
            while ((lineText = reader.readLine()) != null) {
                if (linNumber==21)
                {
                    break;
                }
                //解析编号
                if (linNumber == 3) {

                    lineText=randForceData.getUid()+"\\par";
                }
                //型号
                if (linNumber == 8) {
                    lineText=randForceData.getModel()+"\\par";
                }
                //检测的时间
                if (linNumber == 10) {

                    lineText=timeEncode(randForceData.getData())+"\\par";

                }
                //最大力
                if (linNumber == 11) {
                    lineText=randForceData.getMaxForce()+"\\par";
                }
                //屈服力
                if (linNumber == 12) {
                    lineText=randForceData.getYieldForce()+"\\par";
                }
                //检测耗时
                if (linNumber == 13) {
                    lineText=randForceData.getTime()+"\\par";
                }
                builder.append(lineText+"\n");
                linNumber++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //开始处理生成的数据和时间
        double[] rdTime = randForceData.getRdTime();
        double[] doubles = randForceData.getrDForce();
        for (int i = 0; i < rdTime.length; i++) {
            String sr=rdTime[i]+","+doubles[i]+"\\par"+"\n";
            builder.append(sr);
        }
        builder.append(",\\par\n" +
                "\\par\n" +
                "}");

        System.out.println(builder);;
        //进行保存
        String fileName=randForceData.getPath()+randForceData.getUid()+".txt";

        try {
            File file1 = new File(fileName);
            FileWriter writer = new FileWriter(file1);
            writer.write(builder.toString());
            writer.close();
            System.out.println("Data has been written to file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 保留2位小数
     */
    public static double format2(double num)
    {
        try
        {
            DecimalFormat df = new DecimalFormat("#.##"); // 创建 DecimalFormat 对象，格式为保留两位小数
            double result = Double.parseDouble(df.format(num)); // 将原始数字格式化为保留两位小数的字符串，并将其解析为 double 类型
            return result;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
     return 0.0;
    }

}
