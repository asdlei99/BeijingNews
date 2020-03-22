package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.bobo.beijingnews.R;
import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.bobo.beijingnews.utils.BitmapCacheUtils;
import com.bobo.beijingnews.utils.CacheUtils;
import com.bobo.beijingnews.utils.Constants;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.LogRecord;

import static com.bobo.beijingnews.utils.NetcacheUtils.SUCESS;
import static com.bobo.beijingnews.utils.NetcacheUtils.FAIL;

/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 互动菜单页面
 */
public class InteracMenuDetailPager extends MenuDetailBasePager {

    private PhotosMenuDetailPagerAdapter adapter;

    @ViewInject(R.id.listview)
    private ListView listView;

    @ViewInject(R.id.gridview)
    private GridView gridView;

    private NewsCenterPagerBean2.DetailPagerData detailPagerData;

    private String url;

    /**
     * listview / gridview 的数据源
     */
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> news;

    /**
     * 记录当前是显示list view还是grid view的变量 默认是 true 即 list view
     * true 显示list view 隐藏grid view
     * false 是显示 grid view 隐藏 list view
     */
    private boolean isShowListView = true;

    /**
     * (图片)三级缓存工具类
     */
    private BitmapCacheUtils bitmapCacheUtils;

    /**
     * 用于图片三级缓存子线程切换到主线程
     */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case SUCESS:
                    // 三级缓存图片请求成功的处理
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;

                    // 如果当前显示的是listview
                    if (listView.isShown()){
                        ImageView iv_icon = listView.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    // 如果当前显示的是gridView
                    if (gridView.isShown()){
                        ImageView iv_icon = gridView.findViewWithTag(position);
                        if (iv_icon != null && bitmap != null){
                            iv_icon.setImageBitmap(bitmap);
                        }
                    }

                    Log.e("三级缓存联网请求图片成功","" + position);

                    break;
                case FAIL:
                    // 三级缓存图片请求失败的处理
                    int failPosition = msg.arg1;
                    Log.e("三级缓存联网请求图片失败","" + failPosition);
                    break;
            }
        }
    };

    public InteracMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;

        // 实例化图片三级缓存工具类
        bitmapCacheUtils = new BitmapCacheUtils(handler);
    }

    @Override
    public View initView() {

        // 加载图组xml布局
        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);

        // 使用xUtils 初始化布局
        x.view().inject(InteracMenuDetailPager.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + detailPagerData.getUrl();
        LogUtil.e("互动详情页面数据被初始化了...");

        // 取出缓存数据
        String saveJson = CacheUtils.getString(context, url);

        // 判断是否有本地缓存的数据
        if (!TextUtils.isEmpty(saveJson)){
            // 将json对象转为Java对象
            processData(saveJson);
        }

        // 发起网络请求
        getDataFromNet();
    }

    /**
     * 使用Volley做网络请求
     */
    private void getDataFromNet() {

        // String请求
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {

                        // 请求成功的处理
                        LogUtil.e("用Volley 联网请求成功 ："+result);

                        //缓存数据本地持久化保存
                        CacheUtils.putString(context, url, result);

                        // 将json对象转为Java对象
                        processData(result);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 请求失败的处理
                LogUtil.e("用Volley 联网请求失败 ："+ volleyError.getMessage());
            }
        }){
            /**
             * 解决Volley乱码
             * @param response
             * @return
             */
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                try {
                    String parsed = new String(response.data, "UTF-8");
                    return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return super.parseNetworkResponse(response);
            }
        };

        // 添加到队列
        //  queue.add(request);原来的写法
        VolleyManager.getRequestQueue().add(request);
    }

    /**
     * 解析json数据 和显示数据
     * @param json
     */
    private void processData(String json){

        // 解析json
        PhotosMenuDetailPagerBean bean = parsedJson(json);

        LogUtil.e("图组解析成功== " + bean.getData().getNews().get(0).getTitle());

        // 是显示 list view 还是显示grid view 默认显示 grid view（觉得这个不写也可以）
        isShowListView = true;

        // 设置适配器
        news = bean.getData().getNews();
        // adapter = new PhotosMenuDetailPagerAdapter();
        adapter = new PhotosMenuDetailPagerAdapter();
        listView.setAdapter(adapter);
    }

    /**
     * 图组页面 右上角的按钮被点击了（只有图组页右上角有按钮）
     * 切换list view 和 grid view的方法
     * @param ib_swich_list_grid
     */
    public void switchListViewAndGridView(ImageButton ib_swich_list_grid) {

        // 如果是要显示list view
        if (isShowListView){
            /// 修改状态 注释原因：已经合并成一句 isShowListView = !isShowListView;
            // isShowListView = false;

            // 切换到grid view 隐藏 list view
            gridView.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            gridView.setAdapter(adapter);
            listView.setVisibility(View.GONE);

            // 按钮显示--list view
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_list_type);

        }else{
            /// 修改状态 注释原因：已经合并成一句 isShowListView = !isShowListView;
            // isShowListView = true;

            // 显示list view 隐藏 grid view
            listView.setVisibility(View.VISIBLE);
            adapter = new PhotosMenuDetailPagerAdapter();
            listView.setAdapter(adapter);
            gridView.setVisibility(View.GONE);

            // 按钮显示--grid view
            ib_swich_list_grid.setImageResource(R.drawable.icon_pic_grid_type);
        }

        // 状态取反
        isShowListView = !isShowListView;
    }

    /**
     * 内部类实现list view的适配器
     */
    class PhotosMenuDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news == null ? 0 : news.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PhotosMenuDetailPager.ViewHolder viewHolder;

            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_photos_menudetail_pager, null);
                viewHolder = new PhotosMenuDetailPager.ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (PhotosMenuDetailPager.ViewHolder) convertView.getTag();
            }

            // 根据位置（position）得到对应的数据
            PhotosMenuDetailPagerBean.DataBean.NewsBean newsBean = news.get(position);

            // 设置标题
            viewHolder.tv_title.setText(newsBean.getTitle());

            // 使用Volley请求-设置图片
            String imageUrl = Constants.BASE_URL + newsBean.getSmallimage();

            /// 使用Volley请求图片-设置图片的方法
            // loaderImager(viewHolder, imageUrl);

            // ---↓使用自定义三级缓存请求图片↓---

            // 设置tag为了配合三级缓存回传图片
            viewHolder.iv_icon.setTag(position);

            // 自定义三级缓存请求图片
            Bitmap bitmap = bitmapCacheUtils.getBitmap(imageUrl, position);
            if (bitmap != null) {
                viewHolder.iv_icon.setImageBitmap(bitmap);
            }

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }

    /**
     * 使用Volley请求-设置图片
     * @param viewHolder
     * @param imageurl
     */
    private void loaderImager(final PhotosMenuDetailPager.ViewHolder viewHolder, String imageurl) {

        /**
         * 异步请求成功根据url找到对应的imageview （要是全部url都一样？）
         * 直接在这里请求会乱位置
         */
        viewHolder.iv_icon.setTag(imageurl);

        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (viewHolder.iv_icon != null) {
                        if (imageContainer.getBitmap() != null) {
                            // 网络图片不为空设置网络图片
                            viewHolder.iv_icon.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            // 网络图片为空设置默认图片
                            viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                viewHolder.iv_icon.setImageResource(R.drawable.home_scroll_default);
            }
        };

        // 使用Volley发起请求
        VolleyManager.getImageLoader().get(imageurl, listener);
    }


    /**
     * 专门解析json的方法
     * @param json
     * @return
     */
    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }

    // 原来的代码
    // private TextView textView;
    //
    // public InteracMenuDetailPager(Context context) {
    //     super(context);
    // }

    // @Override
    // public View initView() {
    //
    //     textView = new TextView(context);
    //     textView.setGravity(Gravity.CENTER);
    //     textView.setTextColor(Color.RED);
    //     textView.setTextSize(25);
    //
    //     return textView;
    // }

    // @Override
    // public void initData() {
    //     super.initData();
    //     LogUtil.e("互动详情页面数据被初始化了...");
    //     textView.setText("互动详情页面的内容");
    // }
}
