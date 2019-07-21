package com.bobo.beijingnews.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.beijingnews.base.BasePager;

import java.util.ArrayList;

/**
 * Created by 求知自学网 on 2019/7/21. Copyright © Leon. All rights reserved.
 * Functions: ContentFragment页面的viewpager的适配器
 */
public class ContentFragmentAdapter extends PagerAdapter{


    /**存储五个子页面的 集合*/
    private ArrayList<BasePager> basePagers;

    public ContentFragmentAdapter(ArrayList<BasePager> basePagers){
        this.basePagers = basePagers;
    }

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

        /**
         * 调用各个页面的initData方法 初始化数据
         * 在这里掉用 会有预加载的现象 即用的第一个页面的时候 第一和第二个页面都被加载了
         */
        //basePager.initData();

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
