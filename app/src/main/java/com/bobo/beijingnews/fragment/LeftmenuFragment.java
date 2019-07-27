package com.bobo.beijingnews.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.activity.MainActivity;
import com.bobo.beijingnews.base.BaseFragment;
import com.bobo.beijingnews.domain.NewsCenterPgerBean;
import com.bobo.beijingnews.pager.NewsCenterPager;
import com.bobo.beijingnews.utils.DensityUtil;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.utils.StBarUtil;

import java.util.List;

/**
 * Created by 求知自学网 on 2019/7/7. Copyright © Leon. All rights reserved.
 * Functions: 左侧菜单的fragment
 */
public class LeftmenuFragment extends BaseFragment {

    /**网络请求回来的本页面数据*/
    List<NewsCenterPgerBean.DataBean> data;

    //显示内容的listView
    private ListView listView;

    //listView的适配器
    private LeftmenuFragmentAdapter adapter;

    /**记录用户上次点击的listview的位置*/
    private int prePosition;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LogUtil.e("左侧菜单视图被初始化了");

        //显示内容的listView 注意：没有通过打气筒和XML创建直接Java代码创建😄
        listView = new ListView(context);

        //设置内边距 左 上 右 下
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);

        //去掉listview的分割线(将分割线的高度设置为0)
        listView.setDividerHeight(0);

        //取消用户按下listview的item置灰
        listView.setCacheColorHint(Color.TRANSPARENT);

        //取消用户选中item变色
        listView.setSelector(android.R.color.transparent);

        //设置item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //1.记录点击的位置点击过后变成红色
                prePosition = position;
                adapter.notifyDataSetChanged();//刷新适配器getCount() getView()

                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity)context;
                //toggle 你是关 它就帮你开 你是开他就帮你关
                mainActivity.getSlidingMenu().toggle();

                //3.（右边正文）切换到对应的详情页面，新闻，专题，图组，互动
                switchPager(prePosition);
            }
        });

        return listView;
    }

    /**
     * 根据位置切换不同详情页面
     * @param position
     */
    private void switchPager(int position) {
        MainActivity mainActivity = (MainActivity)context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        newsCenterPager.switchPager(position);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单数据被初始化了");
    }

    /**
     * 设置本页面（左侧菜单fragment）的网络数据
     * @param data
     */
    public void setData(List<NewsCenterPgerBean.DataBean> data) {
        this.data = data;

//        for (int i= 0;i < data.size();i++){
//            LogUtil.e(data.get(i).getTitle());
//        }

        //设置listview的适配器（建议在有数据的地方设置适配器）
        adapter = new LeftmenuFragmentAdapter();
        listView.setAdapter(adapter);

        //默认选中第0个item 并让右边内容切换到对应的页面
        switchPager(prePosition);
    }


    class LeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data ==  null ? 0 : data.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) View.inflate(context, R.layout.item_leftmenu,null);
            textView.setText(data.get(position).getTitle());

            //简写了：textView.setEnabled(prePosition == position);
//            if (prePosition == position){
//                //item变红
//                textView.setEnabled(true);
//            }else {
//                //item变白
//                textView.setEnabled(false);
//            }

            textView.setEnabled(prePosition == position);

            return textView;
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }
}
