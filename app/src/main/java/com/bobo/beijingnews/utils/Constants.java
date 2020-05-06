package com.bobo.beijingnews.utils;

/**
 * Created by 求知自学网 on 2019/7/21. Copyright © Leon. All rights reserved.
 * Functions: 全局常量类，配置联网请求的地址
 */
public class Constants {

    // 本地（自己电脑上）搭建服务器 模拟器请求可以用这个地址
    //public static final String BASE_URL = "https://10.0.2.2:8080/web_home";

    /**联网请求的主路径*/
    public static final String BASE_URL = "https://geekpark.site";

    /**新闻中心的请求地址*/
    public static final String NEWSCENTER_PAGER_URL = BASE_URL+"/static/api/news/categories.json";

}
