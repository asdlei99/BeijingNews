package com.bobo.beijingnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import  com.bobo.beijingnews.R;

/**
 * Created by 求知自学网 on 2019/11/1. Copyright © Leon. All rights reserved.
 * Fun: 自定义下拉刷新的list view
 *      1.自定义下ListView实现下拉刷新，RefreshListview,必须重写带有两个参数的构造方法。
 *      2.在构造方法里面，把头文件添加。
 *      自定义progressbar（进度条）
 *      3.下拉刷新控件隐藏和显示的原理
 *      View.setPadding(0,-控件高，0，0);//完全的隐藏状态
 *      View.setPadding(0,0，0，0);//完全显示
 *      View.setPadding(0,控件的高，0，0);//完全（两倍）显示
 *      状态
 *      4.拖到实现隐藏和显示头部控件
 *      a,重写onTouchEvent()
 *      b,计算滑动距离
 *      float distanceY = endY - startY;
 *      int paddingTop = - 控件高 + distanceY
 *      View.setPadding(0,paddingTop，0，0);//动态显示下拉刷新控件
 *      c,设置效果
 */
public class RefreshListview extends ListView{

    //自定义下拉刷新和顶部轮播图（先不加入） TODO: 57 15:00
    private LinearLayout headerView;


    //自定义控件一般要实现三个构造方法一
    public RefreshListview(Context context) {
        this(context,null);
    }

    //自定义控件一般要实现三个构造方法二
    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    //自定义控件一般要实现三个构造方法三  直接在这里初始化操作
    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initHeaderView(context);
    }

    //初始化自定下拉刷新布局
    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context,R.layout.refresh_headerview,null);

    }
}
