package com.ddjf.image.test;

import java.io.File;

/**
 * @author moyongfeng
 * @email: mo.yf@belle.com.cn
 * @date 2018/8/13 20:35
 * @copyright yougou.com
 * @description:
 */
public class TestMethod {
    public TestMethod() { ///xx/weblogic60b2_win.exe
        try {
            SiteInfoBean bean = new SiteInfoBean("http://localhost:9999/resources/css/styles/login/login_new/welcome.jpg",
                    "D:\\temp", "welcome.jpg", 2);
            //SiteInfoBean bean = new SiteInfoBean("http://localhost:8080/down.zip","L:\\temp",
//            "weblogic60b2_win.exe", 5);
            SiteFileFetch fileFetch = new SiteFileFetch(bean);
            fileFetch.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestMethod();
//        System.out.println(File.separator);
//        String path = "c:/ddd/ddd/a.jpg";
//        System.out.println(path.substring(0,path.lastIndexOf("/")));
    }
}
