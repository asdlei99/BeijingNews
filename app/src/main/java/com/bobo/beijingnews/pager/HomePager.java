package com.bobo.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.bobo.beijingnews.base.BasePager;

/**
 * Created by 求知自学网 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions: 首页
 */
public class HomePager extends BasePager {

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        //1.设置标题
        tv_title.setText("主页面");

        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);

        //4.绑定数据
        textView.setText("主页面内容");
    }
}
