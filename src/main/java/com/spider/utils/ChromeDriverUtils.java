package com.spider.utils;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;

/**
 * Created by wq on 2017/5/7.
 */
public class ChromeDriverUtils {

    static ChromeDriver driver = null;
    static Object lock = new Object();

    public static ChromeDriver getChromeDriver() {
        if (null==driver) {
            synchronized (lock) {
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.addExtensions(new File("User-Agent-Switcher_v2.0.0.4.crx"));
                driver = new ChromeDriver(options);
                return driver;
            }
        }
        return driver;
    }
}
