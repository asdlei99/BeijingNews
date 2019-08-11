package com.bobo.beijingnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 求知自学网 on 2019/7/21. Copyright © Leon. All rights reserved.
 * Functions: 自定义ViewPager 屏蔽滑动
 */
public class NoScrollViewPager extends ViewPager{

    /**
     * 通常在创建实例的时候，用该构造方法
     * @param context
     */
    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     * 在布局文件中使用该类的时候，实例化该类用该构造方法，（自定义）这个方法不能少，少了会崩溃
     * @param context
     * @param attrs
     */
    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重新触摸事件，消耗掉触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 解决 viewpager 使用了tabPageIndicator  左划到头还能左划的bug
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
