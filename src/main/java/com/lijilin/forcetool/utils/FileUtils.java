package com.lijilin.forcetool.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileUtils
 * @Description
 * @Author Administrator
 * @Time 2023/4/8 10:31
 * @Version 1.0
 */
public class FileUtils {

    public static void checkCreatFile(String filePath) {
        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                System.out.println("File created successfully!");
            } catch (IOException e) {
                System.err.println("Failed to create file: " + e.getMessage());
            }
        } else {
            System.out.println("File already exists!");
        }
    }

    public static void creatFile(String path)
    {
        // 创建一个 File 对象，表示要检查的文件夹
        File folder = new File(path);

        // 如果文件夹不存在，就创建它
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (!success) {
                System.out.println("Failed to create folder: " + path);
            }
        }
    }

    public static void save(String filePath, String data) {
        try {
            PrintWriter pw = new PrintWriter(new File(filePath));

            // 先清空文件内容
            pw.write("");
            pw.flush();

            // 再写入新数据
            pw.write(data);
            pw.write(System.lineSeparator()); // 写入一个换行符

            pw.close();

            System.out.println("Data saved successfully!");
        } catch (Exception e) {
            System.err.println("Failed to save data to file: " + e.getMessage());
        }
    }


    public static List<String> read(String filePath)
    {
        List<String> list=new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
