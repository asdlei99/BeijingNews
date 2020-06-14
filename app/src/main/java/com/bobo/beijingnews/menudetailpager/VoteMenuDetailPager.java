package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.utils.LogUtil;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 投票菜单页面
 */
public class VoteMenuDetailPager extends MenuDetailBasePager {
    
    private TextView textView;
    
    public VoteMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {

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
        webView.loadUrl("file:///android_asset/study/principles_of_planning.html");
        
        return webView;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
