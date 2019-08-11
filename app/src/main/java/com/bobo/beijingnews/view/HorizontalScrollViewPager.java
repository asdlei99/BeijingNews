package com.bobo.beijingnews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 求知自学网 on 2019/8/11. Copyright © Leon. All rights reserved.
 * Functions: TabDetailPager（一个页面创建了N多个实例） 下的banner（viewpager做的一个类创建了N多个实例）
 * 解决： （第一个banner）左划和SlidingMenu 事件冲突的问题
 */
public class HorizontalScrollViewPager extends ViewPager {

    /**记录X轴方向滑动距离 起始坐标*/
    private float startX;

    /**记录Y轴方向滑动距离 起始坐标*/
    private float startY;

    public HorizontalScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 解决： （第一个banner）左划和SlidingMenu 事件冲突的问题
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        /**
         *  banner ① 竖直方向滑动 getParent().requestDisallowInterceptTouchEvent(false);
         *
         *  ② 水平方向滑动：
         *
         *      2.1 当滑动到viewPager 的第0个页面，并且是从左往右滑动
         *      getParent().requestDisallowInterceptTouchEvent(false);
         *
         *      2.2 当滑动到viewPager的最后一个页面，并且是重右到左滑动
         *      getParent().requestDisallowInterceptTouchEvent(false);
         *
         *      2.3 其他情况
         *      getParent().requestDisallowInterceptTouchEvent(true);
         */



        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                //获取父类并请求不接受（不要）拦截触摸事件 自己干
                getParent().requestDisallowInterceptTouchEvent(true);

                //1.记录起始坐标
                startX = ev.getX();
                startY = ev.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                //2.用户滑动后 新的坐标
                float endX = ev.getX();
                float endY = ev.getY();

                //3.计算偏移量 x方向 和 y方向
                float distanceX = endX - startX;
                float distanceY = endY - startY;

                //4.判断滑动方向
                if (Math.abs(distanceX) > Math.abs(distanceY)){//横向滑动 水平方向滑动

                    //2.1 当滑动到viewPager 的第0个页面，并且是从左往右滑动(distanceX > 0)
                    if (getCurrentItem() == 0 && distanceX > 0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if (getCurrentItem() == (getAdapter().getCount() - 1) && distanceX < 0){
                        //2.2 当滑动到viewPager的最后一个页面，并且是重右到左滑动(distanceX < 0)
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else{
                        //2.3 其他情况
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }

                }else{//竖直（垂直）方向滑动
                    //父类处理 自己不处理
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;

            case MotionEvent.ACTION_UP :
                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
