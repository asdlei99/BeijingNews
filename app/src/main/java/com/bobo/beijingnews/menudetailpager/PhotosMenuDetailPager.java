package com.bobo.beijingnews.menudetailpager;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.domain.PhotosMenuDetailPagerBean;
import com.bobo.beijingnews.utils.CacheUtils;
import com.bobo.beijingnews.utils.Constants;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.R;
import com.bobo.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by 求知自学网 on 2019/7/27. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心下： 图组菜单页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

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

    public PhotosMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        this.detailPagerData = detailPagerData;
    }

    @Override
    public View initView() {

        // 加载图组xml布局
        View view = View.inflate(context, R.layout.photos_menudetail_pager, null);

        // 使用xUtils 初始化布局
        x.view().inject(PhotosMenuDetailPager.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Constants.BASE_URL + detailPagerData.getUrl();
        LogUtil.e("图组详情页面数据被初始化了...");

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

        LogUtil.e("图组页 " + bean.getData().getNews().get(0).getTitle());

        // 设置适配器
        news = bean.getData().getNews();
        adapter = new PhotosMenuDetailPagerAdapter();
        listView.setAdapter(adapter);
    }

    /**
     * 内部类实现list view的适配器
     */
    class PhotosMenuDetailPagerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news == null ? 0 : news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;

            if (convertView == null){
                convertView = View.inflate(context,R.layout.item_photos_menudetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewHolder);
            }else{

            }

            return convertView;
        }
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_title;
    }

    /**
     * 专门解析json的方法
     * @param json
     * @return
     */
    private PhotosMenuDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
    }
}


// textView = new TextView(context);
// textView.setGravity(Gravity.CENTER);
// textView.setTextColor(Color.RED);
// textView.setTextSize(25);
