package com.bobo.beijingnews.menudetailpager.tabdetailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.domain.TabDetailPagerBean;
import com.bobo.beijingnews.utils.CacheUtils;
import com.bobo.beijingnews.utils.Constants;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import bobo.com.refreshlistview.RefreshListview;

/**
 * Created by 求知自学网 on 2019/7/28. Copyright © Leon. All rights reserved.
 * Functions: 专题下的 页签详情页面
 */
public class TopicDetailPager extends MenuDetailBasePager{

    //用ViewPager 制作轮播图
    private HorizontalScrollViewPager viewpager;

    //轮播图上的标题
    private TextView tv_title;

    //轮播图上的指示器小点
    private LinearLayout ll_point_group;

    //展示内容的listview
    private ListView listview;

    //listview的适配器
    private TabDetailPagerListAdapter adapter;

    //xUtils  设置占位图要用到的变量
    private ImageOptions imageOptions;

    //页面的数据
    private NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;

    //页签详情页面 请求数据的url
    private String url;

    //顶部benner的数据集合
    private List<TabDetailPagerBean.DataBean.TopnewsData> topnews;

    //banner上之前 高亮（红色）显示的位置 默认为0
    private int prePosition;

    //list view 新闻列表对应的集合数据
    List<TabDetailPagerBean.DataBean.NewsData> news;

    /**
     * 下一页的联网路径
     */
    private String moreUrl;

    /**
     * 是否加载更多的变量
     */
    private boolean isLoadMore = false;

    // PullToRefreshListView下拉刷新上拉加载更多的ListView
    private PullToRefreshListView mPullToRefreshListView;

    public TopicDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;

        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                //设置圆角
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //加载时的图片（占位图片）
                .setLoadingDrawableId(R.drawable.news_pic_default)
                //错误时显示的图片
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.topic_detail_pager,null);

        //展示内容的listview
        mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);

        listview = mPullToRefreshListView.getRefreshableView();

        /**
         * Add Sound Event Listener (添加声音事件侦听器)
         */
        SoundPullEventListener<ListView> soundListener  = new SoundPullEventListener<>(context);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH,R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET,R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING,R.raw.refreshing_sound);
        mPullToRefreshListView.setOnPullEventListener(soundListener);


        //打气筒加载 顶部轮播图的xml布局文件
        View topNewsView = View.inflate(context,R.layout.topnews,null);
        //用ViewPager 制作轮播图
        viewpager = (HorizontalScrollViewPager)topNewsView.findViewById(R.id.viewpager);
        //轮播图上的标题
        tv_title = (TextView)topNewsView.findViewById(R.id.tv_title);
        //轮播图上的指示器小点
        ll_point_group = (LinearLayout)topNewsView.findViewById(R.id.ll_point_group);

        //把顶部轮播图部分视图以“头”的方式添加到list view中
        //listview.addTopNewsView(topNewsView);
        listview.addHeaderView(topNewsView);


        //设置监听下拉刷新上拉加载更多
        //listview.setOnRefreshListener(new MyOnRefreshListene());
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                getDataFromNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                if (TextUtils.isEmpty(moreUrl)){
                    //没有更多数据
                    Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                    //隐藏上拉加载更多控件
                    //listview.onRefreshFinish(false);
                    mPullToRefreshListView.onRefreshComplete();
                }else{
                    getMoreDataFromNet();
                }
            }
        });

        return view;
    }

//    //采用内部类的方式实现下拉刷新接口的监听
//    class MyOnRefreshListene implements RefreshListview.OnRefreshListener{
//
//        @Override
//        public void onPullDownRefresh() {
//            //联网请求数据
//            //Toast.makeText(context,"下拉刷新被回调了",Toast.LENGTH_SHORT).show();
//            getDataFromNet();
//        }
//
//        @Override
//        public void onLoadMore() {
//
//            if (TextUtils.isEmpty(moreUrl)){
//                //没有更多数据
//                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
//                //隐藏上拉加载更多控件
//                listview.onRefreshFinish(false);
//            }else{
//                getMoreDataFromNet();
//            }
//        }
//    }

    /**
     * 获取网络数据
     */
    private void getMoreDataFromNet(){

        RequestParams params = new RequestParams(moreUrl);

        //设置链接超时时间
        //params.setConnectTimeout(10000);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //解析数据
                Log.e("加载更多请求成功==",result);

                //listview.onRefreshFinish(false);
                mPullToRefreshListView.onRefreshComplete();

                //一定要把这个放在前面
                isLoadMore = true;

                //解析数据
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("加载更多联网失败onError==",ex.getMessage());
                //listview.onRefreshFinish(false);
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("加载更多联网onCancelled==",cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("加载更多联网onFinished()==","onFinished()");
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        url = Constants.BASE_URL + childrenData.getUrl();

        //把之前缓存的数据取出
        String saveJson = CacheUtils.getString(context,url);

        //如果有上次缓存的数据先使用缓存避免白屏
        if (!TextUtils.isEmpty(saveJson)){
            //解析和处理显示数据
            processData(saveJson);
        }

        LogUtil.e(childrenData.getTitle() +" : "+url);

        //联网请求数据
        getDataFromNet();
    }

    //联网请求数据
    private void getDataFromNet(){

        RequestParams params = new RequestParams(url);

        //设置链接超时时间
        //params.setConnectTimeout(10000);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                //缓存数据
                CacheUtils.putString(context,url,result);

                Log.e("leon",childrenData.getTitle()+"-页面数据请求成功=="+result);
                //LogUtil.e(childrenData.getTitle()+"-页面数据请求成功=="+result);

                //解析和处理显示数据
                processData(result);

                //隐藏下拉刷新控件-（重新显示数据），更新时间
                //listview.onRefreshFinish(true);
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("leon",childrenData.getTitle()+"-页面数据请求失败=="+ex.getMessage());
                //LogUtil.e(childrenData.getTitle()+"-页面数据请求失败=="+ex.getMessage());
                //隐藏下拉刷新控件-不更新时间，只是隐藏
                //listview.onRefreshFinish(false);
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("leon",childrenData.getTitle()+"-页面数据onCancelled=="+cex.getMessage());
                //LogUtil.e(childrenData.getTitle()+"-页面数据onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("leon",childrenData.getTitle()+"-页面数据onFinished==");
                //LogUtil.e(childrenData.getTitle()+"-页面数据onFinished==");
            }
        });

    }

    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);

        //Log.e("leon->",bean.getData().getNews().get(0).getTitle());

        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + bean.getData().getMore();
        }

        //默认和加载更多
        if (!isLoadMore) {//默认

            //顶部轮播图数据
            topnews = bean.getData().getTopnews();

            //设置viewPager的适配器
            viewpager.setAdapter(new TabDeailPagerTopNewsAdapter());

            //添加view pager中的指示器 灰红点
            addPoint();

            //监听页面的改变,设置红点变化和文本变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(prePosition).getTitle());//默认显示第0个标题

            //准备list view 对应的集合数据
            news = bean.getData().getNews();

            //设置listView的适配器
            adapter = new TabDetailPagerListAdapter();
            listview.setAdapter(adapter);
        }else{
            //加载更多
            isLoadMore = false;
            //List<TabDetailPagerBean.DataBean.NewsData> morenews = bean.getData().getNews();
            //添加到原来集合中
            news.addAll(bean.getData().getNews());
            //刷新适配器
            adapter.notifyDataSetChanged();
        }
    }

    //内部类 新闻列表list view的适配器
    class TabDetailPagerListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news == null ? 0 : news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null){
                //打气筒加载xml中的布局文件
                convertView = View.inflate(context,R.layout.item_tabdetail_pager,null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置设置数据
            TabDetailPagerBean.DataBean.NewsData newsData = news.get(position);
            String imageUrl =Constants.BASE_URL + newsData.getListimage();
            //请求图片使用xUtils 可以使用
            //x.image().bind(viewHolder.iv_icon,imageUrl,imageOptions);

            //Glide加载图片 diskCacheStrategy:磁盘缓存策略
            Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(
                    R.drawable.news_pic_default).error(R.drawable.news_pic_default).
                    into(viewHolder.iv_icon);

            //设置标题
            viewHolder.tv_title.setText(newsData.getTitle());

            //设置 时间
            viewHolder.tv_time.setText(newsData.getPubdate());

            return convertView;
        }
    }

    //list view适配器的 ViewHolder
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }


    //添加view pager中的指示器 灰红点
    private void addPoint() {
        //先移除之前的子控件
        ll_point_group.removeAllViews();

        //根据数组中的元素个数创建banner指示器上的点
        for (int i = 0; i < topnews.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.point_selector);


            //设置布局间距 xUtils 也有一个DensityUtil 像素转dp 的工具类能用
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(5.0f)
                    ,DensityUtil.dip2px(5.0f));

            //默认选中第0个红点
            if (i == 0){
                imageView.setEnabled(true);
            }else{
                imageView.setEnabled(false);
                //设置左边距
                params.leftMargin = DensityUtil.dip2px(8.0f);
            }

            imageView.setLayoutParams(params);

            ll_point_group.addView(imageView);
        }
    }

    //内部类 实现viewpager页面改变接口
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            //某个页面被选中了 ①设置文本
            tv_title.setText(topnews.get(position).getTitle());
            //②红点高亮-(当前)红色 （之前的要变成灰色）
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            ll_point_group.getChildAt(position).setEnabled(true);

            //③ prePosition 赋值 不能一直为0 啊
            prePosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    //内部类适配器 banner ViewPager的适配器
    class TabDeailPagerTopNewsAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topnews == null ? 0 : topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            //使用Java代码动态创建imageview
            ImageView imageView = new ImageView(context);

            //设置背景(默认图片）占位图
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            //设置拉伸类型 X轴 和 Y轴方向拉伸
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            //添加到容器中即viewpager中
            container.addView(imageView);

            //根据索引获取数据
            TabDetailPagerBean.DataBean.TopnewsData topnewsData = topnews.get(position);
            //图片请求地址的拼接
            String imageUrl = Constants.BASE_URL + topnewsData.getTopimage();

            //联网请求图片 xUtil 有和 Glide 类似的功能
            //x.image().bind(imageView,imageUrl,imageOptions);
            x.image().bind(imageView,imageUrl);

            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

    //json对象转Java模型对象
    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json,TabDetailPagerBean.class);
    }
}





//        java创建textView
//        textView = new TextView(context);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.RED);
//        textView.setTextSize(25);

//  XUtils3框架——绑定图片 基本用法
//  imageOptions = new ImageOptions.Builder()
//          //设置加载过程中的图片
//          .setLoadingDrawableId(R.drawable.ic_launcher)
//          //设置加载失败后的图片
//          .setFailureDrawableId(R.drawable.ic_launcher)
//          //设置使用缓存
//          .setUseMemCache(true)
//          //设置显示圆形图片
//          .setCircular(true)
//          //设置支持gif
//          .setIgnoreGif(false)    //以及其他方法
//          .build();