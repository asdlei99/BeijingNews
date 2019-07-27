package com.bobo.beijingnews;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 求知自学网 on 2019/7/20. Copyright © Leon. All rights reserved.
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
    }
}
