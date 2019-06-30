package com.bobo.beijingnews;


import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bobo.beijingnews.R;

/**
 * Created by 求知自学网 on 2019/6/30 Copyright © Leon. All rights reserved.
 * Functions: app的启动页  https://geekpark.site/
 */
public class SplashActivity extends Activity {

    private RelativeLayout rl_splash_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //实例化RelativeLayout 让整个页面都做动画
        rl_splash_root = (RelativeLayout)findViewById(R.id.rl_splash_root);

        //设置沉浸式状态栏
        StBarUtil.setOccupationHeight(this,null);

        //①渐变动画  缩放动画 旋转动画
        AlphaAnimation aa = new AlphaAnimation(0,1);//透明度从0到1
        aa.setDuration(500);//动画的持续时间
        //如果“后填充”为真，则此动画所执行的转换在完成时将保持不变。默认为false
        aa.setFillAfter(true);

        //渐变动画  ②缩放动画 旋转动画   从0到1缩放 缩放中心点是自身的中心
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1,ScaleAnimation.
                RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        sa.setDuration(500);//设置动画的时长
        //如果“后填充”为真，则此动画所执行的转换在完成时将保持不变。默认为false
        sa.setFillAfter(true);

        //渐变动画  缩放动画  ③旋转动画   从0度旋转到360 度 旋转围绕自己的中心点
        RotateAnimation ra = new RotateAnimation(0,360,RotateAnimation.
                RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(500);//设置动画的时长
        //如果“后填充”为真，则此动画所执行的转换在完成时将保持不变。默认为false
        ra.setFillAfter(true);


        //让 渐变动画  缩放动画  旋转动画 三个动画同时播放
        AnimationSet set = new AnimationSet(false);
        //添加三个动画没有先后顺序
        set.addAnimation(ra);
        set.addAnimation(aa);
        set.addAnimation(sa);
        set.setDuration(2000);//这个也可以设置动画的时间  后设置的会覆盖前面设置的最终起作用

        //播放这三个动画
        rl_splash_root.startAnimation(set);

        //对动画的播放事件监听
        set.setAnimationListener(new MyAnimationListener());
    }

    //内部类 实现 动画侦听器从动画接收通知
    class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {
            //动画开始播放

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            //动画播放结束
            Toast.makeText(SplashActivity.this,"动画播放结束",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            //动画重复播放

        }
    }
}
