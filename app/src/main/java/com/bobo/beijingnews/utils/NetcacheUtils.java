package com.bobo.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 微信公众号IT波 on 2020/3/22. Copyright © Leon. All rights reserved.
 * Functions: 网络缓存工具类
 */
public class NetcacheUtils {

    /**
     * 代表请求图片成功的常量 2020年3月22创建
     */
    public static final int SUCESS = 20032201;

    /**
     * 代表请求图片失败的常量 2020年3月22创建
     */
    public static final int FAIL = 20032202;

    private Handler handler;

    // 线程池
    private ExecutorService service;

    public NetcacheUtils(Handler handler) {
        this.handler = handler;

        // 创建线程池
        service = Executors.newFixedThreadPool(10);
    }

    /**
     * 联网请求得到图片
     * @param imageUrl
     * @param position
     */
    public void getBitmapFromNet(String imageUrl, int position) {

        // 开辟子线程做网络请求这个方法可以使用 出于性能上的考虑后面采用了线程池
        // new Thread(new  MyRunnable(imageUrl, position)).start();

        // 采用线程池
        service.execute(new  MyRunnable(imageUrl, position));

    }

    class MyRunnable implements Runnable{

        private String imageUrl;
        private int position;

        public MyRunnable(String imageUrl, int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            // 在子线程中请求网络图片
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // 设置请求方法 如： GET  POST
                connection.setRequestMethod("GET");

                // 设置网络请求超时时间5秒 5000毫秒
                connection.setConnectTimeout(5000);

                // 读取超时时间5秒 5000毫秒
                connection.setReadTimeout(5000);

                // 开始联网请求（这句代码可写可不写）
                connection.connect();

                // 得到返回码 一般 200代表成功 有时候0代表成功
                int code = connection.getResponseCode();

                if (code == 200) {
                    // 网络请求成功得到输入流
                    InputStream is = connection.getInputStream();

                    // 将输入流转换为Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    // 显示在控件上，发消息把Bitmap和position发出去
                    Message msg = Message.obtain();
                    msg.what = SUCESS;

                    // 如果您只需要存储一个几个整数值。arg1和arg2是较低成本的替代使用方法-android
                    msg.arg1 = position;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                    // 在内存中缓存一份

                    // 在本地（磁盘中）缓存一份

                }

            } catch (IOException e) {
                e.printStackTrace();
                // 显示在控件上，发消息把Bitmap和position发出去
                Message msg = Message.obtain();
                msg.what = FAIL;

                // 如果您只需要存储一个几个整数值。arg1和arg2是较低成本的替代使用方法-android
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        }
    }
}
