package bobo.com.refreshlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import bobo.com.refreshlistview.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 求知自学网 on 2019/11/1. Copyright © Leon. All rights reserved.
 * Fun: 自定义下拉刷新的list view
 *      1.自定义下ListView实现下拉刷新，RefreshListview,必须重写带有两个参数的构造方法。
 *      2.在构造方法里面，把头文件添加。
 *      自定义progressbar（进度条）
 *      3.下拉刷新控件隐藏和显示的原理
 *      View.setPadding(0,-控件高，0，0);//完全的隐藏状态
 *      View.setPadding(0,0，0，0);//完全显示
 *      View.setPadding(0,控件的高，0，0);//完全（两倍）显示
 *      状态
 *      4.拖动实现隐藏和显示下拉刷新控件
 *      a,重写onTouchEvent()
 *         在down startX，startY
 *      b,计算滑动距离
 *      float distanceY = endY - startY;
 *      int paddingTop = - 控件高 + distanceY
 *      View.setPadding(0,paddingTop，0，0);//动态显示下拉刷新控件
 *      c,设置效果
 * #刷新效果的实现
 *  1.定义刷新的三个状态
 *   public static final int pull_down_refresh = 0;//下拉刷新
 *   public static final int release_refresh = 1;//手松开刷新
 *   public static final int refreshing = 2;//正在刷新
 *   private int currentStatus = pull_down_refresh;//当前状态默认等于下拉刷新
 *
 *  2.实现状态的切换
 *   if(paddingTop < 0 && currentStatus != pull_down_refresh){
 *       //下拉刷新状态
 *       currentStatus = pull_down_refresh;
 *       //更新状态
 *   }else if(paddingTop > 0 && currentStatus != release_refresh){
 *       //手松开刷新
 *       currentStatus = release_refresh;
 *       //更新状态
 *   }
 *
 *  3.实现手离开的处理
 *  if(currentStatus ==  pull_down_refresh){
 *      View.setPadding(0,-控件高，0，0);//完全的隐藏状态
 *  }else if(currentStatus == release_refresh){
 *      currentStatus = refreshing;
 *      //设置状态为正在刷新
 *      //回调接口
 *      View.setPadding(0,0，0，0);//完全显示
 *  }
 *
 * #自定义list view上拉加载更多
 *  1.加载更多的布局,构造方法中
 *  2.监听ListView的划动当划动到底部的最后一条的时候，显示下拉刷新（下拉加载更多）控件，设置状态，回调加载更多的接口
 *  3.定义和回调接口
 */
public class RefreshListview extends ListView{

    //自定义下拉刷新和顶部轮播图（先不加入后来加入）
    private LinearLayout headerView;

    //下拉刷新控件
    private View ll_pull_down_refreh;

    //下拉刷新箭头
    private ImageView iv_arrow;

    //下拉刷新的圆形进度条
    private ProgressBar pb_status;

    //下拉刷新状态的textview
    private TextView tv_status;

    //下拉刷新时间的textView
    private TextView tv_time;

    //下拉刷新控件的高度
    private int pullDownRefreshHeight;

    //记录用户竖直方向划动距离的变量
    private float startY = -1;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;

    /**
     * 手松开刷新
     */
    public static final int RELEASE_REFRESH = 1;

    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;

    /**
     * 当前状态默认等于下拉刷新
     */
    private int currentStatus = PULL_DOWN_REFRESH;

    //箭头向上转动 动画
    private Animation upAnimation;
    //箭头向下转动 动画
    private Animation downAnimation;

    //下拉刷新的回调接口
    private OnRefreshListener mOnRefreshListener;

    //上拉加载更多控件
    private View footerView;

    //上拉加载更多控件的高度
    private int footerViewHeight;

    //是否已经上拉加载更多
    private boolean isLoadMore = false;

    /**
     * 顶部轮播图部分
     */
    private View topNewsView;

    //listview在Y轴上的坐标默认为-1
    private int listViewOnScreenY = -1;

    //自定义控件一般要实现三个构造方法一
    public RefreshListview(Context context) {
        this(context,null);
    }

    //自定义控件一般要实现三个构造方法二
    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    //自定义控件一般要实现三个构造方法三  直接在这里初始化操作
    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    //初始化上拉加载更多的布局
    private void initFooterView(Context context) {
        footerView = View.inflate(context,R.layout.refresh_footer,null);
        footerView.measure(0,0);//先调用测量方法才能获取到控件的高度
        footerViewHeight = footerView.getMeasuredHeight();

        footerView.setPadding(0,-footerViewHeight,0,0);

        //ListView添加footer
        addFooterView(footerView);

        //监听ListView的滚动
        setOnScrollListener(new MyOnScrollListener());
    }

    /**
     * （自定义）添加顶部轮播图
     * @param topNewsView
     */
    public void addTopNewsView(View topNewsView) {
        if (topNewsView != null){
            this.topNewsView = topNewsView;
            headerView.addView(topNewsView);
        }
    }

    //采用内部类的方式实现 list view 滚动接口的监听
    class MyOnScrollListener implements OnScrollListener{

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或者惯性滚动的时候
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState ==
                    OnScrollListener.SCROLL_STATE_FLING){
                //是最后一条可见
                if (getLastVisiblePosition() >= getCount() -1){

                    //1.显示加载更多布局
                    footerView.setPadding(8,8,8,8);

                    //2.状态改变
                    isLoadMore = true;

                    //3.回调接口
                    if (mOnRefreshListener != null){
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    private void initAnimation() {

        upAnimation = new RotateAnimation(0,-180,RotateAnimation.RELATIVE_TO_SELF,
                0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180,-360,RotateAnimation.RELATIVE_TO_SELF,
                0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    //初始化自定下拉刷新布局
    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context,R.layout.refresh_header,null);

        //下拉刷新控件
        ll_pull_down_refreh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = headerView.findViewById(R.id.iv_arrow); //下拉刷新箭头
        pb_status = headerView.findViewById(R.id.pb_status);//下拉刷新的圆形进度条
        tv_status = headerView.findViewById(R.id.tv_status);//下拉刷新状态的textview
        tv_time = headerView.findViewById(R.id.tv_time);//下拉刷新时间的textView

        //测量下拉刷新控件的高
        ll_pull_down_refreh.measure(0,0);
        pullDownRefreshHeight = ll_pull_down_refreh.getMeasuredHeight();

        //默认隐藏下拉刷新控件
        //View.setPadding(0,-控件高，0，0);//完全的隐藏状态
        //View.setPadding(0,0，0，0);//完全显示
        ll_pull_down_refreh.setPadding(0,-pullDownRefreshHeight,0,0);

        //添加liestView的头
        addHeaderView(headerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //1.记录起始坐标
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1){
                    startY = ev.getY();
                }

                //判断顶部轮播图是否完全显示,只有完全显示才会有下拉刷新，解决上拉加载更多后下拉刷新回到顶部的bug
                boolean isDisplayTopNews = isDisplayTopNews();

                if (!isDisplayTopNews){
                   //加载更多-break
                   break;
                }

                //如果是正在刷新，就不让再刷新了
                if (currentStatus == REFRESHING){
                    break;
                }

                //2.来到新的坐标
                float endY = ev.getY();
                //3.计算划动的距离
                float distanceY = endY - startY;

                if (distanceY > 0){//下拉
                    int paddingTop = (int)(-pullDownRefreshHeight + distanceY);

                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH){
                        //下拉刷新狀態
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();

                    }else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH){
                        //手松开刷新
                        currentStatus = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }

                    ll_pull_down_refreh.setPadding(0,paddingTop,0,0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;

                if (currentStatus == PULL_DOWN_REFRESH){
                    //设置下拉刷新控件 完全隐藏
                    ll_pull_down_refreh.setPadding(0,-pullDownRefreshHeight,0,0);
                }else if (currentStatus == RELEASE_REFRESH){
                    //设置状态为正在刷新
                    currentStatus = REFRESHING;

                    refreshViewState();

                    //设置下拉刷新控件 完全显示
                    ll_pull_down_refreh.setPadding(0,0,0,0);

                    //回调接口
                    if (mOnRefreshListener != null){
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否完全显示顶部轮播图
     * 当listView在屏幕上的Y轴坐标小于或等于顶部轮播图在Y轴上坐标的时候，顶部轮播图完全显示。
     * @return
     */
    private boolean isDisplayTopNews() {

        if (topNewsView != null){
            //1.得到ListView 在屏幕上的坐标
            int[] location = new int[2];
            if (listViewOnScreenY == -1) {
                getLocationOnScreen(location);
                listViewOnScreenY = location[1];
            }

            //2.得到顶部轮播图在屏幕上的坐标
            topNewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];

            //当listView在屏幕上的Y轴坐标小于或等于顶部轮播图在Y轴上坐标的时候，顶部轮播图完全显示。
            //if (listViewOnScreenY <= topNewsViewOnScreenY){
            // return true;
            //}else{
            //return false;
            //}
            return listViewOnScreenY <= topNewsViewOnScreenY;
        }else{
            return true;
        }
    }

    private void refreshViewState(){
        switch (currentStatus){
            case PULL_DOWN_REFRESH://下拉刷新状态
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH://手松刷新
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("松开刷新...");
                break;
            case REFRESHING://正在刷新
                tv_status.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                //清除动画
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }
    }

    /**
     * 当联网成功和失败的时候回调该方法
     * 用于刷新状态的还原
     * @param sucess true 请求成功 false 请求失败
     */
    public void onRefreshFinish(boolean sucess) {
        if (isLoadMore){
            //加载更多
            isLoadMore = false;
            //隐藏加载更多的布局
            footerView.setPadding(0,-footerViewHeight,0,0);
        }else{
            //下拉刷新
            tv_status.setText("下拉刷新...");
            currentStatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_arrow.setVisibility(VISIBLE);
            //隐藏下拉刷新控件
            ll_pull_down_refreh.setPadding(0,-pullDownRefreshHeight,0,0);
            if (sucess){
                //设置最新的更新时间
                tv_time.setText("上次更新时间"+getSystemTime());
            }
        }
    }

    /**
     * 得到当前安卓系统的时间
     * @return
     */
    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener{

        /**
         * 当下拉刷新的时候回调这个方法
         */
        public void onPullDownRefresh();

        /**
         * 当上拉加载更多的时候回调这个方法
         */
        public void onLoadMore();
    }

    /**
     * 设置监听刷新,由外界设置
     */
    public void setOnRefreshListener(OnRefreshListener l){
        this.mOnRefreshListener = l;
    }
}
