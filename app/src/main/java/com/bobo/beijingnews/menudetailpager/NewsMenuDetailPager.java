package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.beijingnews.R;

import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.menudetailpager.tabdetailpager.TabDetailPager;
import com.bobo.beijingnews.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 新闻菜单页面
 */
public class NewsMenuDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    /**页签页面的数据集合*/
    private List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> children;

    /**页签页面的集合*/
    private ArrayList<TabDetailPager> tabDetailPagers;

    public NewsMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.children = detailPagerData.getChildren();
    }

    @Override
    public View initView() {

        View view = View.inflate(context,R.layout.newsmenu_detail_pager,null);

        //xUtils3 实例化控件
        x.view().inject(NewsMenuDetailPager.this,view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了...");

        //准备新闻详情页签页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context,children.get(i)));
        }

        //设置适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());
    }

    //内部类 viewpager的适配器
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0: tabDetailPagers.size();
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            TabDetailPager tabDetailPager = tabDetailPagers.get(position);

            View rootView = tabDetailPager.rootView;

            tabDetailPager.initData();//初始化数据

            container.addView(rootView);

            return rootView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
