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
import com.bobo.beijingnews.activity.MainActivity;
import com.bobo.beijingnews.adapter.ContentFragmentAdapter;
import com.bobo.beijingnews.base.BaseFragment;
import com.bobo.beijingnews.base.BasePager;
import com.bobo.beijingnews.pager.GovaffairPager;
import com.bobo.beijingnews.pager.HomePager;
import com.bobo.beijingnews.pager.NewsCenterPager;
import com.bobo.beijingnews.pager.SettingPager;
import com.bobo.beijingnews.pager.SmartServicePager;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.utils.StBarUtil;
import com.bobo.beijingnews.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
    private NoScrollViewPager viewpager;

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
        viewpager = (NoScrollViewPager)view.findViewById(R.id.viewpager);
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
        viewpager.setAdapter(new ContentFragmentAdapter(basePagers));

        //RadioGroup 选中状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //监听某个页面被选中，初始化对应页面的数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        //默认调用第一个页面（主页）的initData方法 初始化数据
        basePagers.get(0).initData();

        //默认设置滑动的模式:边缘(才能)滑动，全屏滑动，*不可以滑动
        isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
    }

    //内部类实现 viewpager 页面改变的接口
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        /**
         * 当页面滚动的时候会调用这个方法
         * @param i 页面位置
         * @param v 滑动的百分比
         * @param i1 滑动的像素
         */
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        /**
         * 在某个页面被选中的时候调用这个方法
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            //调用各个页面的initData方法 初始化数据
            basePagers.get(position).initData();
        }

        /**
         * 页面滚动状态更改
         * @param i  拖拽2 惯性 静止:0
         */
        @Override
        public void onPageScrollStateChanged(int i) {
            //LogUtil.e("页面滚动状态更改:"+i);
        }
    }

    //内部类实现RadioGroup 选中状态改变的监听的接口
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * @param group  RadioGroup
         * @param checkedId 被选中的RadioButton的id
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                case R.id.rb_home://首页RadioButton的id
                    viewpager.setCurrentItem(0);
                    //设置滑动的模式:边缘(才能)滑动，全屏滑动，*不可以滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_newscenter://新闻中心RadioButton的id
                    viewpager.setCurrentItem(1);
                    //设置滑动的模式:边缘(才能)滑动，*全屏滑动，不可以滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                    break;
                case R.id.rb_smartservice://智慧服务RadioButton的id
                    viewpager.setCurrentItem(2);
                    //设置滑动的模式:边缘(才能)滑动，全屏滑动，*不可以滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_govaffair://政要指南RadioButton的id
                    viewpager.setCurrentItem(3);
                    //设置滑动的模式:边缘(才能)滑动，全屏滑动，*不可以滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
                case R.id.rb_setting://设置中心RadioButton的id
                    //iewpager.setCurrentItem(4,fales); 两个参数时切换是否能显示动画
                    viewpager.setCurrentItem(4);
                    //设置滑动的模式:边缘(才能)滑动，全屏滑动，*不可以滑动
                    isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
                    break;
            }
        }
    }

    //（通过主页面）根据传入的参数设置SlidingMenu是否可以滑动
    private void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    //内部类viewpager的适配器 后来（抽取）改外部类
    //class ContentFragmentAdapter extends PagerAdapter{
    //}
}
