package com.bobo.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;

/**
 * Created by 求知自学网 on 2019/7/28. Copyright © Leon. All rights reserved.
 * Functions:页签详情页面
 */
public class TabDetailPager extends MenuDetailBasePager{

    private TextView textView;

    //页面的数据
    private NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
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
        textView.setText(childrenData.getTitle());
    }
}
