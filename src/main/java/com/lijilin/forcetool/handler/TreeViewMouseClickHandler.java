package com.lijilin.forcetool.handler;

import com.lijilin.forcetool.HomeController;
import com.lijilin.forcetool.entity.ForceData;
import com.lijilin.forcetool.entity.ForceTxtData;
import com.lijilin.forcetool.entity.TreeViewData;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import com.lijilin.forcetool.utils.DataFillUtil;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TreeViewMouseClickHandler
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 13:09
 * @Version 1.0
 */
public class TreeViewMouseClickHandler implements EventHandler<MouseEvent> {

    private TreeView<TreeViewData> stencilTreeView;


    public TreeViewMouseClickHandler(TreeView<TreeViewData> stencilTreeView) {
        this.stencilTreeView = stencilTreeView;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        TreeItem<TreeViewData> selectedItem = stencilTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // 判断点击的节点是否为 TreeItem
            if (selectedItem.getValue() != null) {
                System.out.println("ISpant=" + selectedItem.isLeaf());
                // 在这里处理鼠标点击事件
                //如果是叶子节点才处理
                if (selectedItem.isLeaf()) {

                    HomeController homeController = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
                    homeController.clearText();

                    readData(selectedItem.getValue().getPath());

                }
            }
        }
    }


    private void readData(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        ForceTxtData txtData = new ForceTxtData();
        txtData.setPath(path);
        List<ForceData> dataList = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String lineText = null;
            int linNumber = 1;
            while ((lineText = reader.readLine()) != null) {

                //解析编号
                if (linNumber == 3) {
                    String uid = lineText.replace("\\par", "");
                    txtData.setUid(uid);
                }
                //型号
                if (linNumber == 8) {

                }
                //检测的时间
                if (linNumber == 10) {
                    String time = lineText.replace("\\par", "");
                    String s = DataFillUtil.timeDecode(time);
                    txtData.setData(s);
                }
                //最大力
                if (linNumber == 11) {
                    String maxFroce = lineText.replace("\\par", "");
                    txtData.setMaxForce(maxFroce);
                }
                //屈服力
                if (linNumber == 12) {
                    String yieldForce = lineText.replace("\\par", "");
                    txtData.setYieldForce(yieldForce);
                    //计算比例

                    String maxForce = txtData.getMaxForce();
                    //
                    if (yieldForce.equals(""))
                    {
                        yieldForce=0.0+"";
                    }
                    double prop = Double.parseDouble(yieldForce) / Double.parseDouble(maxForce);
                    txtData.setProp(DataFillUtil.format2(prop));
                }
                //检测耗时
                if (linNumber == 13) {
                    String time = lineText.replace("\\par", "");
                    txtData.setTime(time);
                }

                // 这里的lineText就是读取到的一行文件
                if (lineText != null && lineText.endsWith("\\par") && lineText.contains(",")) {
                    String trim = lineText.replace("\\par", "").trim();
                    ForceData forceData = new ForceData(trim);
                    dataList.add(forceData);
                }
                linNumber++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
        if (controller != null) {
            controller.setModelForceTxtData(txtData);
            controller.showTabModelData();
        }
        showLineChat(dataList);
    }

    private void showLineChat(List<ForceData> list) {
        HomeController controller = (HomeController) JavaFxControllerFactory.getController(JavaFxControllerFactory.HOME_KEY);
        controller.setSelectForceData(list);
        LineChart<Number, Number> chartLine = controller.getChartLine();
        System.out.println("点击了生成数据");
        chartLine.getData().clear();
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("数据");
        for (ForceData forceData : list) {
            final XYChart.Data<Number, Number> d1 = new XYChart.Data<>(forceData.getTimeX(), forceData.getForceY());
            series1.getData().add(d1);
        }
        chartLine.getData().addAll(series1);
    }
}
