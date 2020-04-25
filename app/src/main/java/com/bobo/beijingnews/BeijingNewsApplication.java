package com.bobo.beijingnews;

import android.app.Application;
import android.content.Context;

import com.bobo.beijingnews.volley.VolleyManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.xutils.x;

/**
 * Created by 公众号IT波 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions:代表整个软件 默认单例
 * 本项目用到了 初始化xUtils3 一定要在这个类中初始化初始化xUtils3
 */
public class BeijingNewsApplication extends Application {

    //所有组件被创建之前调用这个方法
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化xUtils3
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能.
        x.Ext.setDebug(true);

        // 初始化Volloey
        VolleyManager.init(this);

        // 初始化ImageLoader
        initImageLoader(getApplicationContext());
    }

    /**
     * 初始化ImageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
