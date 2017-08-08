package com.spider.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/5/10.
 */
public class UrlUtils {
    /**
     * 从url中解析参数
     * @param url
     * @return
     */
    public static Map<String, String> parseUrl(String url) {
        Map<String, String> mapRequest = new HashMap<>();
        int index = url.indexOf("?");
        url = url.substring(index + 1, url.length());
        String[] arrSplit = url.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

}
