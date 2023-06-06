package com.lijilin.forcetool.entity;

import java.util.Arrays;

/**
 * @ClassName 力值数据
 * @Description
 * @Author Administrator
 * @Time 2023/3/24 10:00
 * @Version 1.0
 */
public class ForceData {

    /**
     * 时间
     */
    private Double timeX=0.0;
    /**
     * 力值
     */
    private Double forceY=0.0;

    public ForceData() {

    }
    public ForceData(double timeX, double forceY) {
        this.timeX = timeX;
        this.forceY = forceY;
    }

    public ForceData(String str) {

        String[] split = str.split(",");
        if (split.length<=0)
        {
            return;
        }
        try {
            String s1 = split[0].trim();
            if (s1!=null && !s1.isEmpty())
            {
                timeX= Double.parseDouble(s1);
            }
            if (split.length>1)
            {
                String s2 = split[1].trim();
                if (s2!=null && !s2.isEmpty())
                {
                    try {
                        forceY= Double.parseDouble(s2);
                    }catch (Exception e)
                    {
                        System.out.println("出错了"+s1+"-"+s2);
                        e.printStackTrace();
                    }

                }
            }
        }catch (Exception e)
        {
            System.out.println("报错了"+ Arrays.toString(split));
        }



    }

    public Double getTimeX() {
        return timeX;
    }

    public void setTimeX(Double timeX) {
        this.timeX = timeX;
    }

    public Double getForceY() {
        return forceY;
    }

    public void setForceY(Double forceY) {
        this.forceY = forceY;
    }

    @Override
    public String toString() {
        return "ForceData{" +
                "timeX=" + timeX +
                ", forceY=" + forceY +
                '}';
    }
}
