package com.bobo.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 点击左边菜单 会变化的 新闻中心下的 菜单详情页的 基础页面 注意没有继承自fragment
 */
public abstract class MenuDetailBasePager {

    /**上下文*/
    protected Context context;

    /**代表各个详情页面的视图*/
    public View rootView;

    public MenuDetailBasePager(Context context){
        this.context = context;
        rootView = initView();
    }

    /**由于各个子页面ui风格不一样交由各个子类实现*/
    public abstract View initView();

    /**
     * 子页面需要绑定数据，联网请求数据，等重写此方法
     */
    public void initData(){

    }

}
