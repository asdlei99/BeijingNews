package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.activity.MainActivity;
import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.menudetailpager.tabdetailpager.TopicDetailPager;
import com.bobo.beijingnews.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 专题菜单页面
 * 这个页面用的是TabLauout 作指示器 新闻页用的是ViewPagerIndicator
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {

    @ViewInject(R.id.tablayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    //tabLayout 右边的 右方向箭头
    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;

    /**页签页面的数据集合*/
    private List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> children;

    /**页签页面的集合*/
    private ArrayList<TopicDetailPager> tabDetailPagers;

    public TopicMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.children = detailPagerData.getChildren();
    }

    @Override
    public View initView() {

        final View view = View.inflate(context,R.layout.topicmenu_detail_pager,null);

        //xUtils3 实例化控件
        x.view().inject(TopicMenuDetailPager.this,view);

        //设置tabLayout 右边的右方向箭头的点击事件
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //i 下面这个方法巧妙的处理的数组越界的问题 当最后一个的时候下一个还是最后一个
                int i = viewPager.getCurrentItem() % tabDetailPagers.size();

                //不处理也不要紧 viewPager 会自己处理数组越界 这里我没有处理
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("专题菜单页面面数据被初始化了...");

        //准备专题菜单页面签页面的数据
        tabDetailPagers = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TopicDetailPager(context,children.get(i)));
        }

        /**
         * 之前在别的项目遇到的bug 要是在出现了 打开 viewPager.setOffscreenPageLimit(children.size());
         * 使用了viewpager和listview进行页面数据显示，在切换viewpager的时候会导致前面的fragment页面数据丢失，
         * 这是fragment重新加载而造成的问题，如果是固定数量viewpager，只需要指定页面数量，即可禁止重新加载：
         */
        // viewPager.setOffscreenPageLimit(children.size());

        //设置适配器
        viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());

        /// viewPager和tabLayout 关联
        // tabLayout.setViewPager(viewPager); TabPageIndicator的代码
        tabLayout.setupWithViewPager(viewPager);

        // 注意使用TabPageIndicator以后监听页面的变化将会用tabLayout(不要再用ViewPager了)
        // tabLayout.setOnPageChangeListener(new MyOnPageChangeListener()); TabPageIndicator的代码
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        // TabLayout设置滑动或者固定
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // 自定义tabLayout指示器（下面带小红点） 下面注释的代码可以正常使用
        // for (int i = 0;i < tabLayout.getTabCount();i++){
        //     TabLayout.Tab tab = tabLayout.getTabAt(i);
        //     tab.setCustomView(getTabView(i));
        // }
    }

    /**
     * 自定义tabLayout指示器
     * @param position
     * @return
     */
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv = (TextView)view.findViewById(R.id.textview);
        tv.setText(children.get(position).getTitle());

        /// 注释原因：xml中已经写了
        // ImageView img = view.findViewById(R.id.imageview);
        // img.setImageResource(R.drawable.dot_focus);

        return view;
    }

    //内部类实现tabLayout监听页面的变化
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float v, int i1) {

            if (position == 0){
                //SlidingMenu可以全屏滑动 仅限最左边（第一页）
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }else {
                //除了最左边（第一页）外SlidingMenu都不能滑动
                isEnableSlidingMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    //（通过主页面）根据传入的参数设置SlidingMenu是否可以滑动
    private void isEnableSlidingMenu(int touchmodeFullscreen){
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullscreen);
    }

    //内部类 viewpager的适配器
    class MyNewsMenuDetailPagerAdapter extends PagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //注意写了这个方法tabLayout上就有标题了
            return children.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers == null ? 0: tabDetailPagers.size();
        }


        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            TopicDetailPager tabDetailPager = tabDetailPagers.get(position);

            View rootView = tabDetailPager.rootView;

            tabDetailPager.initData();//初始化数据

            container.addView(rootView);

            return rootView;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
