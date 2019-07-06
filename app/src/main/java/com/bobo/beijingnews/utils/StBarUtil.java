package com.bobo.beijingnews.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.bobo.beijingnews.R;


/**
 * Created by 求知自学网 on 2019/5/19. Copyright © Leon. All rights reserved.
 * Functions: 用来处理 （例如）HomeFragmnet 沉浸式导航栏的问题
 */
public class StBarUtil {


    /**
     * 不用在style的xml文件中设置透明了这个方法会直接抹去状态栏 方法二
     * @param activity activity 中传this fragment中传 宿主activity
     * @param itemView  fragment 中打气筒的view  activity传null
     */
    public final static void setOccupationHeight(Activity activity,View itemView){

        //想要设置沉浸式状态栏的activity中都创建一个view 高度为状态栏高度 设置成自己想要的颜色
        View view;
        if (itemView != null){
            view = itemView.findViewById(R.id.Occupation);
        }else{
            view = activity.findViewById(R.id.Occupation);
        }


        if(Build.VERSION.SDK_INT >= 21){

            //想要设置沉浸式状态栏的activity中都创建一个view 高度20dp 设置成自己想要的颜色
            if (view != null){//避免空指针异常
                //动态的设置view的高度==状态栏的高度
                view.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
                params.height = getStatusBarHeight(activity);
                view.setLayoutParams(params);
            }
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }else {
            //低版本不适配沉浸式状态栏所以要隐藏
            if (view != null){
                view.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获得状态栏的高度
     */
    private static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
