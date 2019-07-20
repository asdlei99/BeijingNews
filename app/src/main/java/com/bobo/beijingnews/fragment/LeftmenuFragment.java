package com.bobo.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobo.beijingnews.base.BaseFragment;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.utils.StBarUtil;

/**
 * Created by 求知自学网 on 2019/7/7. Copyright © Leon. All rights reserved.
 * Functions: 左侧菜单的fragment
 */
public class LeftmenuFragment extends BaseFragment {

    private TextView textView;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtil.e("左侧菜单视图被初始化了");
        textView = new TextView(context);
        textView.setTextSize(23);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        //设置沉浸式状态栏
        //StBarUtil.setOccupationHeight(this,null);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单数据被初始化了");
        textView.setText("左侧菜单页面");
    }
}
