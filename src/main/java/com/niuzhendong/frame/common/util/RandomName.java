package com.niuzhendong.frame.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 随机生成人名
 *
 * @author niuzhendong
 * @package_name com.jsbc.lottery.util
 * @project_name lottery-lizhi-2019
 * @date 2019/11/13
 */
public class RandomName {
    public static void main(String[] args) throws IOException {
        for (int i = 50; i-- > 0; ) {
            String name = firstName() + secondName(true);
            System.out.println(name);
        }
    }

    public static String getOne() throws IOException {
        //创建随机对象
        Random random = new Random();
        int i = random.nextInt(2);
        if(i==1) {
            return firstName() + secondName(true);
        }
        return firstName() + secondName(false);
    }

    /*
     * 随机返回a和b其中一个数
     */
    public static int randomAB(int a, int b) {
        return (int) ((Math.random() * Math.abs(a - b)) + Math.min(a, b));
    }

    /**
     * 生成姓氏
     *
     * @throws IOException
     */
    private static String firstName() throws IOException {
        List<String> fistNames = loadBaiJiaXing("/asserts/百家姓");
        return fistNames.get(randomAB(0, fistNames.size()));
    }

    /**
     * 读取姓氏文件，获取姓氏
     *
     * @return
     * @throws IOException
     */
    private static List<String> loadBaiJiaXing(String path) throws IOException {
        //使用类加载器来加载文件
        InputStream in = RandomName.class.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        //文件读取
        String line = null;
        //结果集合
        List<String> result = new ArrayList<>(200);
        while ((line = br.readLine()) != null) {
            line = line.trim();
            //使用空白字符分割
            String[] names = line.split("\\s+");
            result.addAll(Arrays.asList(names));
        }
        return result;
    }

    /**
     * @return
     * @throws IOException
     * @生成名字
     */
    private static String secondName(boolean male) throws IOException {
        if (male) {
            List<String> names = loadNames("/asserts/男性");
            return names.get(randomAB(0, names.size()));
        } else {
            List<String> names = loadNames("/asserts/女性");
            return names.get(randomAB(0, names.size()));
        }
    }

    /**
     * 读取百家姓文件，获取名字
     *
     * @return
     * @throws IOException
     */
    private static List<String> loadNames(String path) throws IOException {
        InputStream in = RandomName.class.getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
        //文件读取
        String line = null;
        //结果集合
        List<String> result = new ArrayList<>(200);
        while ((line = br.readLine()) != null) {
            line = line.trim();
            //使用空白字符分割
            String[] names = line.split("\\s+");
            result.addAll(Arrays.asList(names));
        }
        return result;
    }

}