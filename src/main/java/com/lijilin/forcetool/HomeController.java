package com.lijilin.forcetool;

import com.dustinredmond.fxalert.FXAlert;
import com.lijilin.forcetool.config.AppConfig;
import com.lijilin.forcetool.entity.ForceData;
import com.lijilin.forcetool.entity.ForceTxtData;
import com.lijilin.forcetool.entity.TimeForcePair;
import com.lijilin.forcetool.entity.TreeViewData;
import com.lijilin.forcetool.factory.JavaFxControllerFactory;
import com.lijilin.forcetool.handler.*;
import com.lijilin.forcetool.utils.DataFillUtil;
import com.lijilin.forcetool.utils.RandomDataGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {


    @FXML
    private DatePicker checkTime;

    @FXML
    private TextField maxForce;

    @FXML
    private ChoiceBox<String> mode;

    @FXML
    private TextField randomTime;

    @FXML
    private TextField testCount;

    @FXML
    private TextField uid;

    @FXML
    private TextField yieldForce;


    @FXML
    private TreeView<TreeViewData> stencilTreeView;

    @FXML
    private LineChart<Number, Number> chartLine;

    /**
     * 当前选择的参考报告数据
     */
    private List<ForceData> selectForceData;

    /**
     * 当前选择的模版数据
     */
    private ForceTxtData modelForceTxtData;

    /**
     * 随机生成的数据
     */
    private ForceTxtData randForceTxtData;


    @FXML
    private TextField savePath;
    @FXML
    private Text tabModelMxaForce;

    @FXML
    private Text tabModelTime;

    @FXML
    private Text tabModelUid;

    @FXML
    private Text tabModelYieldForce;

    @FXML
    private Text tabRandMxaForce;

    @FXML
    private Text tabRandTime;

    @FXML
    private Text tabRandUid;

    @FXML
    private Text tabRandYieldForce;


    @FXML
    private Text tabModelCheckTime;

    @FXML
    private Text tabRandCheckTime;

    @FXML
    private Spinner<Double> timeNoise;

    @FXML
    private Spinner<Double> forceNoise;

    /**
     * 初始化数据
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chartLine.setCreateSymbols(false);
        JavaFxControllerFactory.registerController(JavaFxControllerFactory.HOME_KEY, this);
        //试验机型号初始化
        DataFillUtil.initModel(mode);
        savePath.setText(AppConfig.getInstance().getSavePath());
        //绑定UID事件
        uid.textProperty().addListener(new UidTextFieldChangeHandler(uid));
        maxForce.textProperty().addListener(new MaxFroceTextFieldChangeHandler(maxForce));
        testCount.textProperty().addListener(new TestCountTextFieldChangeHandler(testCount));
        yieldForce.textProperty().addListener(new YieldForceTextFieldChangeHandler(yieldForce));
        randomTime.textProperty().addListener(new RandomTimeTextFieldChangeHandler(randomTime));
        checkTime.valueProperty().addListener(new CheckTimeForceDatePickerChangeHandler(checkTime));
        mode.setOnAction(new ModelChoiceBoxHandler());

        //初始化试验力模版数据
        // 给 TreeView 添加鼠标点击事件监听器
        stencilTreeView.setOnMouseClicked(new TreeViewMouseClickHandler(stencilTreeView));
        DataFillUtil.initStencilTreeView(stencilTreeView);

        initNoise();

    }


    private void initNoise()
    {
        timeNoise.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10, 0.1, 0.1));
        timeNoise.setEditable(true);
        forceNoise.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.000, 10, 0.01, 0.01));
        forceNoise.setEditable(true);
    }

    /**
     * 创建的随机数据
     *
     * @param event
     */
    @FXML
    void randomDataClcik(ActionEvent event) {
        if (modelForceTxtData == null) {
            FXAlert.showWarning("请先选择一份参考的模版");
            return;
        }

        if (randForceTxtData == null) {
            FXAlert.showWarning("随机数据的基本信息没有填写！");
            return;
        }

        if (randForceTxtData.getUid().equals("")) {
            FXAlert.showWarning("编号未填写！");
            return;
        }
        if (randForceTxtData.getTestCount() == 0) {
            FXAlert.showWarning("共试件数量未填写！");
            return;
        }

        double[] referenceTime = new double[selectForceData.size()];
        double[] referenceForce = new double[selectForceData.size()];

        for (int i = 0; i < selectForceData.size(); i++) {
            referenceTime[i] = selectForceData.get(i).getTimeX();
            referenceForce[i] = selectForceData.get(i).getForceY();
        }


        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(referenceTime, referenceForce);

//        double[] time = randomDataGenerator.generateRandomTime();

        String maxForce1 = randForceTxtData.getMaxForce();

        double mf = 0;

        if (!maxForce1.equals("")) {
            mf = Double.parseDouble(maxForce1);
        }
        double[][] doubles = randomDataGenerator.generateRandomTimeAndForce(mf,forceNoise.getValue()/10,timeNoise.getValue(),System.currentTimeMillis());

        TimeForcePair timeForcePair = randomDataGenerator.separateTimeAndForce(doubles);
//        double[] force = randomDataGenerator.generateRandomForce(mf);

//        Arrays.sort(time);
//        Arrays.sort(force);
        double[] maxTimeAndForce = randomDataGenerator.getMaxTimeAndForce(doubles);
        //最大力值
//        double mxForce = Arrays.stream(force).max().getAsDouble();
//        double mxTime = Arrays.stream(time).max().getAsDouble();

        double mxTime=maxTimeAndForce[0];
        double mxForce=maxTimeAndForce[1];
        maxForce.setText(mxForce + "");
        randomTime.setText(mxTime + "");

        //计算屈服力
        double prop = modelForceTxtData.getProp();
        double yForce = mxForce * prop;

        yieldForce.setText(DataFillUtil.format2(yForce) + "");



        //保存数据
//        randForceTxtData.setRdTime(time);
//        randForceTxtData.setrDForce(force);
        randForceTxtData.setRdTime(timeForcePair.times);
        randForceTxtData.setrDForce(timeForcePair.forces);


        // 查找要删除的系列
        XYChart.Series<Number, Number> seriesToRemove = null;
        for (XYChart.Series<Number, Number> series : chartLine.getData()) {
            if (series.getName().equals(randForceTxtData.getUid())) {
                seriesToRemove = series;
                break;
            }
        }

        // 如果找到了要删除的系列，就从图表中删除它
        if (seriesToRemove != null) {
            chartLine.getData().remove(seriesToRemove);
        }

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName(randForceTxtData.getUid());

//        for (int i = 0; i < time.length; i++) {
//            System.out.println(i+"==time=="+ time[i]+"===force==="+force[i]);
//            final XYChart.Data<Number, Number> d1 = new XYChart.Data<>(time[i], force[i]);
//            series1.getData().add(d1);
//        }
        for (int i = 0; i < timeForcePair.times.length; i++) {
            System.out.println(i+"==time=="+ timeForcePair.times[i]+"===force==="+timeForcePair.forces[i]);
            final XYChart.Data<Number, Number> d1 = new XYChart.Data<>(timeForcePair.times[i], timeForcePair.forces[i]);
            series1.getData().add(d1);
        }


        chartLine.getData().addAll(series1);
    }

    /**
     * 显示设置页面
     *
     * @param actionEvent
     */
    @FXML
    public void showSetting(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * 刷新参考模版树
     */
    public void initStencilTreeView() {
        DataFillUtil.initStencilTreeView(stencilTreeView);
    }

    /**
     * 显示选择的模版的基本数据
     */
    public void showTabModelData() {
        if (modelForceTxtData == null) {
            return;
        }

        tabModelMxaForce.setText(modelForceTxtData.getMaxForce());
        tabModelYieldForce.setText(modelForceTxtData.getYieldForce());
        tabModelTime.setText(modelForceTxtData.getTime());
        tabModelUid.setText(modelForceTxtData.getUid());
        tabModelCheckTime.setText(modelForceTxtData.getData());
    }

    /**
     * 显示选择的模版的基本数据
     */
    public void showTabRandomData() {
        if (randForceTxtData == null) {
            return;
        }

        tabRandMxaForce.setText(randForceTxtData.getMaxForce());
        tabRandYieldForce.setText(randForceTxtData.getYieldForce());
        tabRandTime.setText(randForceTxtData.getTime());
        tabRandUid.setText(randForceTxtData.getUid());
        tabRandCheckTime.setText(randForceTxtData.getData());
    }

    /**
     * 导出数据
     *
     * @param event
     */
    @FXML
    void exportDataBtn(ActionEvent event) {


        //先获取到模版的路径
        if (modelForceTxtData == null) {
            FXAlert.showWarning("未选择模版数据");
            randForceTxtData.setNumber(0);
            return;
        }
        if (modelForceTxtData.getPath() == null) {
            FXAlert.showWarning("模版数据的路径有错！");
            return;
        }
        if (randForceTxtData == null) {
            FXAlert.showWarning("没有随机生成的数据");
            return;
        }
        if (randForceTxtData.getTime() == null || randForceTxtData.getrDForce() == null) {
            FXAlert.showWarning("没有随机生成的数据");
            return;
        }
        if (savePath.getText().equals("")) {
            FXAlert.showWarning("请先选择保存的路径");
            return;
        }
        if (randForceTxtData.equals(""))
        {
            FXAlert.showWarning("屈服力没有数值");
            return;
        }
        if (randForceTxtData.getData().equals(""))
        {
            FXAlert.showWarning("检测日期没有选择");
            return;
        }

        boolean export = DataFillUtil.export(modelForceTxtData.getPath(), randForceTxtData);
        if (export) {
            FXAlert.showInfo(randForceTxtData.getUid()+"导出数据导出成功！");
            creat();
            //编号+1
            // 拆分前缀和后缀数字
            String prefix = randForceTxtData.getUid().substring(0, randForceTxtData.getUid().length() - 2);
            String suffixString = randForceTxtData.getUid().substring(randForceTxtData.getUid().length() - 2);

            // 解析后缀数字，将其增加 1
            int suffix = Integer.parseInt(suffixString);
            suffix++;

            // 格式化新的后缀数字为两位数
            String newSuffixString = String.format("%02d", suffix);

            // 合并前缀和新的后缀为一个字符串
            String newString = prefix + newSuffixString;
            uid.setText(newString);

            randForceTxtData.setNumber(randForceTxtData.getNumber() + 1);

            DataFillUtil.initStencilTreeView(stencilTreeView);

            randForceTxtData.setRdTime(null);
            randForceTxtData.setrDForce(null);

            checkEnd();
        }


    }

    public void checkEnd()
    {
        if (randForceTxtData.getNumber() >= randForceTxtData.getTestCount()) {
            FXAlert.showWarning("总式样已经的数据报告已经全部导出完成！");

            randForceTxtData.setNumber(0);
            uid.setText("");
            testCount.setText("");
            maxForce.setText("");
            yieldForce.setText("");
            randomTime.setText("");
            checkTime.setValue(null);

            chartLine.getData().clear();

            File directory = new File(randForceTxtData.getPath());

            try {
                Desktop.getDesktop().open(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

    }


    public void creat()
    {
        maxForce.setText("");
        yieldForce.setText("");
        randomTime.setText("");

    }

    /**
     * 选择保存的路径
     *
     * @param event
     */
    @FXML
    void selectSavePathBtn(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("请选择模版数据的位置");
        File file = directoryChooser.showDialog(stage);
        if (file == null) {
            return;
        }
        randForceTxtData.setPath(file.getPath() + "\\");
        savePath.setText(file.getPath() + "\\");
    }


    public LineChart<Number, Number> getChartLine() {
        return chartLine;
    }

    public void setSelectForceData(List<ForceData> selectForceData) {
        this.selectForceData = selectForceData;
    }


    public TextField getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(TextField maxForce) {
        this.maxForce = maxForce;
    }

    public TextField getRandomTime() {
        return randomTime;
    }

    public void setRandomTime(TextField randomTime) {
        this.randomTime = randomTime;
    }


    public Text getTabModelMxaForce() {
        return tabModelMxaForce;
    }

    public Text getTabModelTime() {
        return tabModelTime;
    }

    public Text getTabModelUid() {
        return tabModelUid;
    }

    public Text getTabModelYieldForce() {
        return tabModelYieldForce;
    }

    public Text getTabRandMxaForce() {
        return tabRandMxaForce;
    }

    public Text getTabRandTime() {
        return tabRandTime;
    }

    public Text getTabRandUid() {
        return tabRandUid;
    }

    public Text getTabRandYieldForce() {
        return tabRandYieldForce;
    }


    public ForceTxtData getModelForceTxtData() {
        return modelForceTxtData;
    }

    public ForceTxtData getRandForceTxtData() {
        if (randForceTxtData == null) {
            randForceTxtData = new ForceTxtData();
            randForceTxtData.setPath(savePath.getText() + "\\");
        }
        return randForceTxtData;
    }

    public void setModelForceTxtData(ForceTxtData modelForceTxtData) {
        this.modelForceTxtData = modelForceTxtData;
    }

    public void setRandForceTxtData(ForceTxtData randForceTxtData) {
        this.randForceTxtData = randForceTxtData;
    }

    /**
     * 清空最大力值 屈服力 随机时间
     */
    public void clearText()
    {
        maxForce.setText("");
        yieldForce.setText("");
        randomTime.setText("");
    }

}

