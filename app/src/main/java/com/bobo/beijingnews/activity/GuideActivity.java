package com.bobo.beijingnews.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.SplashActivity;
import com.bobo.beijingnews.utils.CacheUtils;
import com.bobo.beijingnews.utils.DensityUtil;
import com.bobo.beijingnews.utils.LELog;
import com.bobo.beijingnews.utils.StBarUtil;

import java.util.ArrayList;

/**
 * Created by 求知自学网 on 2019/7/20 Copyright © Leon. All rights reserved.
 * Functions: 用户首次打开app的引导页 只出现一次
 */
public class GuideActivity extends Activity {

    //显示可左划内容的 容器
    private ViewPager viewpager;

    //左划到底跳转到主页面的按钮
    private Button btn_start_main;

    //装3个灰（选中为红）点的线性布局
    private LinearLayout ll_point_group;

    //展示(3张引导图)的image的 image view
    private ArrayList<ImageView> imageViews;

    //红点
    private ImageView iv_red_point;

    //两个灰色点间距 = 第一个点距离左边的距离 - 第0个点距离左边的距离
    private int leftmax;

    private int widthdpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //设置沉浸式状态栏
        StBarUtil.setOccupationHeight(this,null);

        //实例化各个子控件
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btn_start_main = (Button)findViewById(R.id.btn_start_main);
        ll_point_group = (LinearLayout)findViewById(R.id.ll_point_group);
        iv_red_point = (ImageView)findViewById(R.id.iv_red_point);

        //准备数据(3张引导图)
        int[] ids = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

        //dp转成像素 灰点的宽高
        widthdpi = DensityUtil.dip2px(this,10);

        imageViews = new ArrayList<>();

        //循环（批量创建）image view 和选择器的点
        for (int i = 0; i < ids.length; i++) {
            //创建image view
            ImageView imageView = new ImageView(this);

            //设置背景
            imageView.setBackgroundResource(ids[i]);

            //该模式下，图片会被等比缩放直到完全填充整个ImageView，并居中显示。该模式也是最常用的模式了
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //添加到imageViews 集合中
            imageViews.add(imageView);

            //创建选择器（指示器）小点
            ImageView point = new ImageView(this);

            //设置背景为灰色 成为灰色小点
            point.setBackgroundResource(R.drawable.point_normal);
            //用Java代码设置布局  LayoutParams(int width, int height) 注意单位要设置dp转成像素
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthdpi,widthdpi);

            //设置灰色点之间的间距 第一个（数组中下标为0）的不要设置
            if (i != 0){
                //设置左边距 10dp
                params.leftMargin = widthdpi;
            }

            point.setLayoutParams(params);

            //添加到线性布局中
            ll_point_group.addView(point);
        }

        //设置viewpager的适配器
        viewpager.setAdapter(new MyPagerAdapter());

        //根据view的生命周期，当视图执行到onLayout或者onDraw的时候，视图的宽高边距都有了
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());

        //得到viewpager屏幕滑动的百分比
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 设置按钮的点击事件
        btn_start_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1.保存进入过主页面的持久化变量为true
                CacheUtils.putBoolean(GuideActivity.this, SplashActivity.START_MAIN,true);

                //2.跳转到主页面
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);

                //3.关闭引导（本）页面
                finish();
            }
        });
    }

    //监听view pager 屏幕滑动的内部类
    class MyOnPageChangeListener implements  ViewPager.OnPageChangeListener{

        /**
         * 页面滚动会回调这个方法
         * @param position 当前滑动也的位置
         * @param postionOffset 页面滑动的百分比
         * @param positionOffetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float postionOffset, int positionOffetPixels) {

            // 两点间滑动距离 = 屏幕滑动百分比 * 间距
            //int leftmargin = (int) (postionOffset * leftmax);

            String pe = String.valueOf(postionOffset);
            //LELog.showLogWithLineNum(5,"position:"+position+"百分比："+pe+"像素："
              //      +positionOffetPixels);

            // 两点间滑动距离对应的坐标 = 原来的起始位置 + 两点间移动的距离
            int leftmargin = (int)(position * leftmax + (postionOffset * leftmax));

            //不想滑动的时候红点慢慢移动 直接跳转到下一个点 这样搞
            //int leftmargin = (int)(position * leftmax);


            //params.leftMargin = 两点间滑动距离对应的坐标
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();

            //不想滑动的时候红点慢慢移动 直接跳转到下一个点 这样搞
//            if (postionOffset == 0.0f){
//                params.leftMargin = leftmargin;
//            }

            params.leftMargin = leftmargin;
            iv_red_point.setLayoutParams(params);
        }

        /**
         * 当页面被选中的时候回调这个方法
         * @param position 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            //用户选中最后一页的时候让“开始体验”按钮显示
            if (position == imageViews.size() - 1){
                //最后一个页面显示
                btn_start_main.setVisibility(View.VISIBLE);
            }else{
                //其他页面隐藏
                btn_start_main.setVisibility(View.GONE);
            }
        }

        /**
         * 当viewpager页面滑动状态发生变化的时候 回调这个方法
         * @param state 拖拽 惯性 静止
         */
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //内部类监听layout变化 控件的宽高变化等
    class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {

            //移除这个监听因为这个方法会调用很多次
            iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            //间距 = 第一个点距离左边的距离 - 第0个点距离左边的距离
            leftmax = ll_point_group.getChildAt(1).getLeft() - ll_point_group.
                    getChildAt(0).getLeft();

            //LELog.showLogWithLineNum(5,"onGlobalLayout"+leftmax);
        }
    }

    //这里用了内部类自定义viewpager的适配器
    class MyPagerAdapter extends PagerAdapter{

        /**
         * 返回数据的总个数
         * @return 总个数
         */
        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        /**
         * 作用，getview
         * @param container ViewPager
         * @param position  要创建页面的位置
         * @return 返回和创建当前页面有关系的值
         */
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imageView = imageViews.get(position);

            //添加到容器中container（ViewPager）
            container.addView(imageView);

            //这样这里返回 imageView 或 position 都可以 isViewFromObject根据这里返回的不同写法也不同
            return position;
            //return imageView;
        }

        /**
         * 判断
         * @param view 当前创建的视图
         * @param object 上面instantiateItem返回的结果值
         * @return
         */
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            //上面instantiateItem返回的结果值 如果是imageView 可以↓：
             //return view == object;

            //上面instantiateItem返回的结果值 如果是position
            return view == imageViews.get((int)object);
        }

        /**
         * 销毁页面
         * @param container ViewPager
         * @param position 要销毁的页面的位置
         * @param object 要销毁的页面
         */
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
            //上面instantiateItem返回的结果值 如果是imageView 可以↓：
            //container.removeView((View) object);

            //上面instantiateItem返回的结果值 如果是position
            container.removeView(imageViews.get(position));
        }
    }
}
