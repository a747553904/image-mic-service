package com.ddjf.image.test;

/**
 * @author moyongfeng
 * @email: mo.yf@belle.com.cn
 * @date 2018/8/13 20:39
 * @copyright yougou.com
 * @description:
 */
public class Utility {
    public Utility() {
    }

    public static void sleep(int nSecond) {
        try {
            Thread.sleep(nSecond);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void log(String sMsg) {
        System.err.println(sMsg);
    }

    public static void log(int sMsg) {
        System.err.println(sMsg);
    }
}