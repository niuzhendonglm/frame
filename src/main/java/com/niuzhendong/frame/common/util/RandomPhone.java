package com.niuzhendong.frame.common.util;

import java.util.Random;

/**
 * 随机电话号码
 *
 * @author niuzhendong
 * @package_name com.jsbc.lottery.util
 * @project_name lottery-lizhi-2019
 * @date 2019/11/13
 */
public class RandomPhone {


    public static void main(String[] args) {
        //创建随机对象
        Random random = new Random();

        //循环输出号码
        System.out.println("随机号码");
        for (int i = 0; i < 9; i++) {
            int num;
            System.out.print("1" + (random.nextInt(7) + 3));
            do {
                num = random.nextInt();
                if (num > 100000000 && num < 1000000000) {
                    System.out.println(num);
                }
            } while (!(num > 100000000 && num < 1000000000));
        }
    }

    /**
     * 获取一个手机号码
     * @return
     */
    public static String getOne() {
        //创建随机对象
        Random random = new Random();

        StringBuffer sb = new StringBuffer();
        int num;
        sb.append("1" + (random.nextInt(7) + 3));
        do {
            num = random.nextInt();
            if (num > 100000000 && num < 1000000000) {
                sb.append(num);
                return sb.toString();
            }
        } while (!(num > 100000000 && num < 1000000000));
        return getOne();
    }
}
