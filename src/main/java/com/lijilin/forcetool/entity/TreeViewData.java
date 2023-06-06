package com.lijilin.forcetool.entity;

import javafx.scene.input.MouseEvent;

/**
 * @ClassName TreeViewData
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 11:40
 * @Version 1.0
 */
public class TreeViewData {

    private String name;

    private String path;


    public TreeViewData(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public TreeViewData() {

    }

    public TreeViewData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return  name;
    }

    // 自定义的鼠标点击事件处理方法
    public void onMouseClicked(MouseEvent event) {
        // 在这里处理鼠标点击事件
        System.out.println("Mouse clicked on: " + this.name);
    }
}
