package com.bobo.beijingnews.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.fragment.ContentFragment;
import com.bobo.beijingnews.fragment.LeftmenuFragment;
import com.bobo.beijingnews.utils.DensityUtil;
import com.bobo.beijingnews.utils.StBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by 求知自学网 on 2019/7/7 Copyright © Leon. All rights reserved.
 * Functions: 程序的主页面
 *
 * SlidingMenu 的使用
 * 1.下载:https://github.com/jfeinstein10/SlidingMenu (停止维护)
 * 2.到入库Import Module SlidingMenu-master 文件夹中的 library
 * 3.关联库  Module 中 + 选第三个
 * 4.当前的MainActivity 继承 SlidingFragmentActivity  构造方法修饰符改成public
 */
public class MainActivity extends SlidingFragmentActivity {

    //ContentFragment 的tag 标识
    public static final String MAIN_CONTENT_TAG = "main_content_tag";
    //LeftmenuFragment 的 tag 标识
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //1.设置主页面 侧滑菜单栏右边的页面是主页面
        setContentView(R.layout.activity_main);

        /**
         * SlidingFragmentActivity 要这样才能消掉 状态栏 消掉状态栏后左右两部分自己添加
         * https://blog.csdn.net/weixin_33682790/article/details/86934571
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);//显示状态栏
        }


        //2.设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);

        //3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);设置第二菜单 右侧划菜单

        //4.设置显示的模式：左侧菜单+主页，左侧菜单+主页+右侧菜单(LEFT_RIGHT)，主页面+右侧菜单
        slidingMenu.setMode(SlidingMenu.LEFT);

        //5.设置滑动的模式:边缘(才能)滑动，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //6.设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(MainActivity.this,200));

        //初始化fragment
        initFragment();
    }

    //初始化fragment
    private void initFragment() {

        //1.得到fragmentmanger
        FragmentManager fm = getSupportFragmentManager();

        //2.开启事务
        FragmentTransaction ft = fm.beginTransaction();

        //3.替换 将帧布局 替换成 fragment  ctrl+ait+c 抽取常量快捷键
        ft.replace(R.id.fl_main_content,new ContentFragment(), MAIN_CONTENT_TAG);
        ft.replace(R.id.fl_leftmenu,new LeftmenuFragment(), LEFTMENU_TAG);

        //4.提交
        ft.commit();
    }


}
