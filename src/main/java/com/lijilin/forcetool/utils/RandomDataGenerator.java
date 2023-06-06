package com.lijilin.forcetool.utils;

import com.lijilin.forcetool.entity.TimeForcePair;

import java.util.Arrays;
import java.util.Random;

/**
 * @ClassName RandomDataGenerator
 * @Description
 * @Author Administrator
 * @Time 2023/4/5 15:04
 * @Version 1.0
 */
public class RandomDataGenerator
{
    /**
     * 参考时间数据
     */
    private double[] referenceTime;
    /**
     * 参考力值数据
     */
    private double[] referenceForce;
    /**
     * 生成的随机数据点数
     */
    private int numPoints; //
    /**
     * 随机数生成器
     */
    private Random random;


    public RandomDataGenerator(double[] referenceTime, double[] referenceForce, int numPoints)
    {
        this.referenceTime = referenceTime;
        this.referenceForce = referenceForce;
        this.numPoints = numPoints;
        this.random = new Random();
    }

    public RandomDataGenerator(double[] referenceTime, double[] referenceForce)
    {
        this.referenceTime = referenceTime;
        this.referenceForce = referenceForce;
        this.numPoints = referenceTime.length; // 将numPoints设置为参考时间数组的长度
        this.random = new Random();
    }

    /**
     * 生成随机时间
     *
     * @return
     */
    public double[] generateRandomTime()
    {
        double[] randomTime = new double[numPoints]; // 随机时间数据

        for (int i = 0; i < numPoints; i++)
        {
            // 随机生成时间（在参考时间范围内）
            int index = random.nextInt(referenceTime.length - 1); // 随机选择参考时间数据的下标
            double t1 = referenceTime[index];
            double t2 = referenceTime[index + 1];
            double t = t1 + random.nextDouble() * (t2 - t1); // 在两个相邻参考时间点之间生成随机时间
            randomTime[i] = t;
        }
        for (int i = 0; i < numPoints; i++)
        {
            randomTime[i] = Math.round(randomTime[i] * 100.0) / 100.0;
        }
        return randomTime;
    }

    public double[][] generateRandomTimeAndForce4(double maxForceValue, double noiseFactor, long seed)
    {
        random = new Random(seed);

        double[][] randomData = new double[numPoints][2];

        double maxForce = Arrays.stream(referenceForce).max().orElse(0.0);
        if (maxForceValue > 0)
        {
            maxForce = maxForceValue;
        }

        System.out.println("参考的最大力值: " + maxForce);

        double timeRange = referenceTime[numPoints - 1] - referenceTime[0];

        for (int i = 0; i < numPoints; i++)
        {
            double t = referenceTime[i];
            double noise = 2 * random.nextDouble() - 1;
            t += noise * noiseFactor * timeRange;

            if (t > referenceTime[numPoints - 1])
            {
                t = referenceTime[numPoints - 1];
            } else if (t < referenceTime[0])
            {
                t = referenceTime[0];
            }
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            double f = referenceForce[i] / maxForce; // 归一化处理
            noise = 2 * random.nextDouble() - 1;
            f += noise * noiseFactor;
            f *= maxForce; // 将生成的力值乘以maxForceValue

            if (f > maxForce)
            {
                f = maxForce;
            } else if (f < 0)
            {
                f = 0;
            }
            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    public double[][] generateRandomTimeAndForce2(double maxForceValue, double noiseFactor)
    {
        random = new Random(System.currentTimeMillis()); // 使用新的种子创建随机数生成器
        double[][] randomData = new double[numPoints][2];

        double maxForce = (maxForceValue > 0) ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);
        System.out.println("参考的最大力值: " + maxForce);

        double timeRange = referenceTime[numPoints - 1] - referenceTime[0]; // 参考时间数据的范围

        for (int i = 0; i < numPoints; i++)
        {
            // 在参考时间数据点周围添加一个小的随机扰动
            double t = referenceTime[i];
            double noise = 2 * random.nextDouble() - 1; // 生成一个在[-1, 1]范围内的随机数
            t += noise * noiseFactor * timeRange; // 使用噪声因子来控制扰动的大小

            if (t > referenceTime[numPoints - 1])
            {
                t = referenceTime[numPoints - 1];
            } else if (t < referenceTime[0])
            {
                t = referenceTime[0];
            }
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            // 在参考力值数据点周围添加一个小的随机扰动
            double f = referenceForce[i];
            noise = 2 * random.nextDouble() - 1; // 生成一个在[-1, 1]范围内的随机数
            f += noise * noiseFactor * maxForce; // 使用噪声因子来控制扰动的大小

            if (f > maxForce)
            {
                f = maxForce;
            } else if (f < 0)
            {
                f = 0;
            }
            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    public double[][] generateRandomTimeAndForce1(double maxForceValue, double noiseFactor)
    {

        double[][] randomData = new double[numPoints][2];

        double maxForce = (maxForceValue > 0) ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);
        System.out.println("参考的最大力值: " + maxForce);

        for (int i = 0; i < numPoints; i++)
        {
            // 使用参考时间数据作为生成的时间数据
            double t = referenceTime[i];
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            // 在参考力值数据点周围添加一个小的随机扰动
            double f = referenceForce[i];
            double noise = 2 * random.nextDouble() - 1; // 生成一个在[-1, 1]范围内的随机数
            f += noise * noiseFactor * maxForce; // 使用噪声因子来控制扰动的大小

            if (f > maxForce)
            {
                f = maxForce;
            } else if (f < 0)
            {
                f = 0;
            }
            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    public double[][] generateRandomTimeAndForce(double maxForceValue, double noiseFactor)
    {
        double[][] randomData = new double[numPoints][2];

        double maxForce = (maxForceValue > 0) ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);
        System.out.println("参考的最大力值: " + maxForce);

        for (int i = 0; i < numPoints; i++)
        {
            int index = random.nextInt(numPoints - 1); // 随机索引选择的范围限制在参考数组的长度内
            double t1 = referenceTime[index];
            double t2 = referenceTime[index + 1];
            double t = t1 + random.nextDouble() * (t2 - t1);
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            double f1 = referenceForce[index];
            double f2 = referenceForce[index + 1];
            double f = f1 + (f2 - f1) * (t - t1) / (t2 - t1);

            double noise = 2 * random.nextDouble() - 1;
            f += noise * noiseFactor * maxForce;

            if (f > maxForce)
            {
                f = maxForce;
            } else if (f < 0)
            {
                f = 0;
            }
            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }
    /**
     * 生成随机时间和力
     *
     * @param maxForceValue
     * @param noiseFactor 小的随机扰动因子，例如0.1
     * @return an array of time-force pairs
     */
//    public double[][] generateRandomTimeAndForce(double maxForceValue, double noiseFactor)
//    {
//        double[][] randomData = new double[numPoints][2];
//
//        double maxForce = (maxForceValue > 0) ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);
//        System.out.println("参考的最大力值: " + maxForce);
//
//        for (int i = 0; i < numPoints; i++)
//        {
//            int index = random.nextInt(referenceTime.length - 1);
//            double t1 = referenceTime[index];
//            double t2 = referenceTime[index + 1];
//            double t = t1 + random.nextDouble() * (t2 - t1);
//            randomData[i][0] = Math.round(t * 100.0) / 100.0;
//
//            double f1 = referenceForce[index];
//            double f2 = referenceForce[index + 1];
//            double f = f1 + (f2 - f1) * (t - t1) / (t2 - t1);
//
//            // 添加一个小的随机扰动
//            double noise = 2 * random.nextDouble() - 1; // 生成一个在[-1, 1]范围内的随机数
//            f += noise * noiseFactor * maxForce; // 使用噪声因子来控制扰动的大小
//
//            if (f > maxForce) {
//                f = maxForce;
//            } else if (f < 0) { // 确保力值不会小于0
//                f = 0;
//            }
//            randomData[i][1] = Math.round(f * 100.0) / 100.0;
//        }
//
//        return randomData;
//    }

    /**
     * 生成随机时间和力
     *
     * @param maxForceValue
     * @return an array of time-force pairs
     */
    public double[][] generateRandomTimeAndForce(double maxForceValue)
    {
        double[][] randomData = new double[numPoints][2]; // 随机时间和力数据

        double maxForce = (maxForceValue > 0) ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);
        System.out.println("参考的最大力值: " + maxForce);

        for (int i = 0; i < numPoints; i++)
        {
            // 随机生成时间（在参考时间范围内）
            int index = random.nextInt(referenceTime.length - 1); // 随机选择参考时间数据的下标
            double t1 = referenceTime[index];
            double t2 = referenceTime[index + 1];
            double t = t1 + random.nextDouble() * (t2 - t1); // 在两个相邻参考时间点之间生成随机时间
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            // 计算对应的力
            double f1 = referenceForce[index];
            double f2 = referenceForce[index + 1];
            double f = f1 + (f2 - f1) * (t - t1) / (t2 - t1);

            // 如果力大于最大力，将其设置为最大力
            if (f > maxForce)
            {
                f = maxForce;
            }
            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    public double[][] generateRandomTimeAndForce(double maxForceValue, double noiseFactor, long seed)
    {
        random = new Random(seed);

        double[][] randomData = new double[numPoints][2];

        double maxForce = maxForceValue > 0 ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);

        double timeRange = referenceTime[referenceTime.length - 1] - referenceTime[0];

        for (int i = 0; i < numPoints; i++)
        {
            double t = referenceTime[i];
            double noise = 2 * random.nextDouble() - 1;
            t += noise * noiseFactor * timeRange;

            if (t > referenceTime[referenceTime.length - 1])
            {
                t = referenceTime[referenceTime.length - 1];
            } else if (t < referenceTime[0])
            {
                t = referenceTime[0];
            }
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            // linear interpolation for force value
            double f = 0.0;
            for (int j = 0; j < referenceTime.length - 1; j++)
            {
                if (t >= referenceTime[j] && t <= referenceTime[j + 1])
                {
                    double proportion = (t - referenceTime[j]) / (referenceTime[j + 1] - referenceTime[j]);
                    f = referenceForce[j] + proportion * (referenceForce[j + 1] - referenceForce[j]);
                    break;
                }
            }

            // scale to maxForceValue and add noise
            f = f * maxForce / Arrays.stream(referenceForce).max().orElse(0.0);
            noise = 2 * random.nextDouble() - 1;
            f += noise * noiseFactor * maxForce;

            // clamp to [0, maxForceValue]
            f = Math.min(Math.max(f, 0), maxForce);

            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    /**
     * 
     * @param maxForceValue 最大力值
     * @param noiseFactor 力值噪音
     * @param timeNoiseFactor 时间噪音
     * @param seed  用于保证每次生成的数据的随机性
     * @return
     */
    public double[][] generateRandomTimeAndForce(double maxForceValue, double noiseFactor, double timeNoiseFactor, long seed)
    {
        random = new Random(seed);

        double[][] randomData = new double[numPoints][2];

        double maxForce = maxForceValue > 0 ? maxForceValue : Arrays.stream(referenceForce).max().orElse(0.0);

        double timeRange = referenceTime[referenceTime.length - 1] - referenceTime[0];

        for (int i = 0; i < numPoints; i++)
        {
            // Add time noise
            double t = referenceTime[i] + timeNoiseFactor * (random.nextDouble() - 0.5) * timeRange;
            double noise = 2 * random.nextDouble() - 1;
            double vTime = referenceTime[referenceTime.length - 1];
            if (vTime<=0)
            {
                vTime=getMaxTime();
            }
            if (t > vTime)
            {
                t = vTime;
            } else if (t < referenceTime[0])
            {
                t = referenceTime[0];
            }
            randomData[i][0] = Math.round(t * 100.0) / 100.0;

            // linear interpolation for force value
            double f = 0.0;
            for (int j = 0; j < referenceTime.length - 1; j++)
            {
                if (t >= referenceTime[j] && t <= referenceTime[j + 1])
                {
                    double proportion = (t - referenceTime[j]) / (referenceTime[j + 1] - referenceTime[j]);
                    f = referenceForce[j] + proportion * (referenceForce[j + 1] - referenceForce[j]);
                    break;
                }
            }

            // scale to maxForceValue and add noise
            f = f * maxForce / Arrays.stream(referenceForce).max().orElse(0.0);
            noise = 2 * random.nextDouble() - 1;
            f += noise * noiseFactor * maxForce;

            // clamp to [0, maxForceValue]
            f = Math.min(Math.max(f, 0), maxForce);

            randomData[i][1] = Math.round(f * 100.0) / 100.0;
        }

        return randomData;
    }

    /**
     * 获取最大时间和力值
     * [time, force]
     *
     * @param timeAndForceData
     * @return
     */
    public double[] getMaxTimeAndForce(double[][] timeAndForceData)
    {
        double maxTime = -1;
        double maxForce = -1;

        for (double[] pair : timeAndForceData)
        {
            if (pair[0] > maxTime)
            {
                maxTime = pair[0];
            }
            if (pair[1] > maxForce)
            {
                maxForce = pair[1];
            }
        }

        return new double[]{maxTime, maxForce};
    }

    public TimeForcePair separateTimeAndForce(double[][] timeAndForceData)
    {
        double[] times = new double[timeAndForceData.length];
        double[] forces = new double[timeAndForceData.length];

        for (int i = 0; i < timeAndForceData.length; i++)
        {
            times[i] = timeAndForceData[i][0];
            forces[i] = timeAndForceData[i][1];
        }

        return new TimeForcePair(times, forces);
    }

    public double getMaxTime()
    {
        double maxTime = Arrays.stream(referenceTime).max().orElse(Double.NaN);
        return maxTime;
    }
}
