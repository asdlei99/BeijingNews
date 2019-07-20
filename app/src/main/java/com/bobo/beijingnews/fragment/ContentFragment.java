package com.bobo.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.base.BaseFragment;
import com.bobo.beijingnews.base.BasePager;
import com.bobo.beijingnews.pager.GovaffairPager;
import com.bobo.beijingnews.pager.HomePager;
import com.bobo.beijingnews.pager.NewsCenterPager;
import com.bobo.beijingnews.pager.SettingPager;
import com.bobo.beijingnews.pager.SmartServicePager;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.utils.StBarUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 求知自学网 on 2019/7/7. Copyright © Leon. All rights reserved.
 * Functions: 右边主体内容的fragment
 */
public class ContentFragment extends BaseFragment {


    //装5个fragment的 容器
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;

    //相当于 选项卡（tabhost）
    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    /**存储五个子页面的 集合*/
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.e("正文fragment视图被初始化了");

        //利用打气筒加载布局 https://www.cnblogs.com/tangs/articles/5913719.html
        View view = View.inflate(context, R.layout.content_fragmnet,null);
        viewpager = (ViewPager)view.findViewById(R.id.viewpager);
        rg_main = (RadioGroup)view.findViewById(R.id.rg_main);

        //设置沉浸式状态栏 先在mainActivity中让状态栏小时
        // 再把（5个子页面）通用导航栏 背景图改高
        //StBarUtil.setOccupationHeight(context,view);

        //1.把视图注入到框架中，让ContentFragment.this 和 view关联起来
        x.view().inject(ContentFragment.this,view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文fragment数据被初始化了");

        //初始化5个子页面,并且放入到集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HomePager(context));//首页
        basePagers.add(new NewsCenterPager(context));//新闻中心
        basePagers.add(new SmartServicePager(context));//智慧服务
        basePagers.add(new GovaffairPager(context));//政要指南
        basePagers.add(new SettingPager(context));//设置中心

        //设置默认选中首页
        rg_main.check(R.id.rb_home);

        //设置view pager的适配器
        viewpager.setAdapter(new ContentFragmentAdapter());
    }

    class ContentFragmentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return basePagers == null ? 0 : basePagers.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            //获取各个页面的实例
            BasePager basePager = basePagers.get(position);

            //获取各个页面的视图
            View rootView = basePager.rootView;

            //调用各个页面的initData方法 初始化数据
            basePager.initData();

            //添加到view pager中
            container.addView(rootView);

            //返回视图
            return rootView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        /**
         * 销毁页面
         * @param container ViewPager
         * @param position 要销毁的页面的位置
         * @param object 要销毁的页面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //从viewpager中移除 要销毁的页面
            container.removeView((View)object);
        }
    }

}
