package com.bobo.beijingnews.base;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.utils.StBarUtil;

/**
 * Created by 求知自学网 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions: 右边主体内容的fragment 中的 五个页面的基类(注意没有继承fragment自己写的类)
 */
public class BasePager {

    /**
     * 上下文
     * 将来子类要用 权限适当给一些 不要private
     */
    protected Context context;

    /**
     * 视图，代表各个不同的页面
     */
    public View rootView;

    /**
     * 显示标题的文本框
     */
    protected TextView tv_title;

    /**
     * 点击侧划的按钮 只有部分子页面需要显示
     */
    protected ImageButton ib_menu;

    /**
     *加载各个子页面的 帧布局
     */
    protected FrameLayout fl_content;

    /**沉浸式状态栏占位view*/
    protected View occupation;

    public BasePager(Context context) {

        this.context = context;

        //构造方法一执行，视图就被初始化了
        rootView = initView();
    }

    /**
     * 用于初始化公共部分的视图，并且初始化加载子视图的FrameLayout（帧布局）
     * @return 视图
     */
    private View initView(){

        //基（父）类的页面
        View view = View.inflate(context, R.layout.base_pager,null);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        ib_menu = (ImageButton)view.findViewById(R.id.ib_menu);
        fl_content = (FrameLayout)view.findViewById(R.id.fl_content);
        occupation = (View)view.findViewById(R.id.occupation);

        Activity activity = findActivity(context);
        if (activity != null){
            //动态的设置沉浸式状态栏占位view的高度==状态栏的高度
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) occupation.getLayoutParams();
            params.height = StBarUtil.getStatusBarHeight(activity);
            occupation.setLayoutParams(params);
        }

        return view;
    }

    /**
     * 初始化数据，当子类需要初始化数据，或者绑定数据，联网请求数据并且绑定的时候，重写该方法
     */
    public void initData(){

    }


    /**
     * 根据上下文对象Context找到对应的Activity
     * @return Activity
     */
    public Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

}
