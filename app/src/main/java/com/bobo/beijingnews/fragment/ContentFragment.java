package com.bobo.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.base.BaseFragment;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.utils.StBarUtil;

/**
 * Created by 求知自学网 on 2019/7/7. Copyright © Leon. All rights reserved.
 * Functions: 右边主体内容的fragment
 */
public class ContentFragment extends BaseFragment {

    //装5个fragment的 容器
    private ViewPager viewpager;

    //相当于 选项卡（tabhost）
    private RadioGroup rg_main;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("正文fragment视图被初始化了");

        //利用打气筒加载布局 https://www.cnblogs.com/tangs/articles/5913719.html
        View view = View.inflate(context, R.layout.content_fragmnet,null);
        viewpager = (ViewPager)view.findViewById(R.id.viewpager);
        rg_main = (RadioGroup)view.findViewById(R.id.rg_main);

        //设置沉浸式状态栏
        StBarUtil.setOccupationHeight(context,view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文fragment数据被初始化了");

        //设置默认选中首页
        rg_main.check(R.id.rb_home);
    }

}
