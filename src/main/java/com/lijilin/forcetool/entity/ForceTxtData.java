package com.lijilin.forcetool.entity;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName 生成的总的数据 包含 编号  时间 等数据
 * @Description
 * @Author Administrator
 * @Time 2023/4/8 11:32
 * @Version 1.0
 */
public class ForceTxtData {

    /**
     * 编号
     */
    private String uid="";
    /**
     * 试验机型号
     */
    private String model="";
    /**
     * 检测时间
     */
    private String data="";
    /**
     * 耗费时间
     */
    private String time="";
    /**
     * 最大力
     */
    private String maxForce="";
    /**
     * 屈服力
     */
    private String yieldForce="";
    /**
     * 测试样本数量
     */
    private int testCount;

    /**
     * 随机生成的数据
     */
    private  double[] rdTime=null;

    private double[] rDForce=null;
    /**
     * 路劲
     */
    private String path;
    /**
     * 已经导出的次数
     */
    private int number;

    /**
     * 屈服力宇最大力的比例
     */
    private double prop;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMaxForce() {
        return maxForce;
    }

    public void setMaxForce(String maxForce) {
        this.maxForce = maxForce;
    }

    public String getYieldForce() {
        return yieldForce;
    }

    public void setYieldForce(String yieldForce) {
        this.yieldForce = yieldForce;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double[] getRdTime() {
        return rdTime;
    }

    public void setRdTime(double[] rdTime) {
        this.rdTime = rdTime;
    }

    public double[] getrDForce() {
        return rDForce;
    }

    public void setrDForce(double[] rDForce) {
        this.rDForce = rDForce;
    }

    public int getTestCount() {
        return testCount;
    }

    public void setTestCount(int testCount) {
        this.testCount = testCount;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getProp() {
        return prop;
    }

    public void setProp(double prop) {
        this.prop = prop;
    }
}
