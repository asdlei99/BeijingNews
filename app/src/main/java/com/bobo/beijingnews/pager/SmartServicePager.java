package com.bobo.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.bobo.beijingnews.base.BasePager;
import com.bobo.beijingnews.utils.LogUtil;

/**
 * Created by 求知自学网 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions: 智慧服务
 */
public class SmartServicePager extends BasePager {

    public SmartServicePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("智慧服务设置中心数据被加载了");

        //1.设置标题
        tv_title.setText("会展项目管理");

        // 帧布局先移除之前所有的子控件
        fl_content.removeAllViews();

        // 加载webview
        WebView webView = new WebView(context);

        // 使用Java代码设置宽高布局
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);

        // 允许js运行
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 加载本地assets文件夹中自己写的H5页面
        webView.loadUrl("file:///android_asset/study/project_management.html");

        // 帧布局再添加webview进来
        fl_content.addView(webView);
    }
}


//    //2.联网请求，得到数据，创建视图
//    TextView textView = new TextView(context);
//    textView.setGravity(Gravity.CENTER);
//    textView.setTextColor(Color.RED);
//    textView.setTextSize(25);
//    //3.把子视图添加到BasePager的FrameLayout中
//    fl_content.addView(textView);
//    //4.绑定数据
//    textView.setText("智慧服务内容");