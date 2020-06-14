package com.bobo.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobo.beijingnews.base.BasePager;
import com.bobo.beijingnews.utils.LogUtil;
import com.joanzapata.pdfview.PDFView;

/**
 * Created by 求知自学网 on 20设置中心9/7/20. Copyright © Leon. All rights reserved.
 * Functions: 设置中心
 */
public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("设置中心数据被加载了");

        // 设置中心.设置标题
        tv_title.setText("会议运营管理");

        // 帧布局先移除之前所有的子控件
        fl_content.removeAllViews();

        // 加载PDFView
        PDFView pdfView = new PDFView(context, null);

        // 使用Java代码设置宽高布局
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pdfView.setLayoutParams(layoutParams);

        // computation_revision
        pdfView.fromAsset("conference_operations_management.pdf")   //设置pdf文件地址
                .defaultPage(1)         //设置默认显示第1页
                // .onPageChange(this)     //设置翻页监听
                // .onLoad(this)           //设置加载监听
                // .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( false )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 , 3 , 4 , 5  )  //把2 , 3 , 4 , 5 过滤掉
                .load();

        fl_content.addView(pdfView);


        // 设置中心.设置标题
        // tv_title.setText("设置中心");

        // 2.联网请求，得到数据，创建视图
        // TextView textView = new TextView(context);
        // textView.setGravity(Gravity.CENTER);
        // textView.setTextColor(Color.RED);
        // textView.setTextSize(25);

        //3.把子视图添加到BasePager的FrameLayout中
        // fl_content.addView(textView);

        //4.绑定数据
        // textView.setText("设置中心内容");
    }
}
