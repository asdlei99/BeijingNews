package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.utils.LogUtil;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 图组菜单页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {
    
    private TextView textView;
    
    public PhotosMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("图组详情页面数据被初始化了...");
        textView.setText("图组详情页面的内容");
    }
}
