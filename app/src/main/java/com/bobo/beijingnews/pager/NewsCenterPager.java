package com.bobo.beijingnews.pager;

import android.app.VoiceInteractor;
import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bobo.beijingnews.activity.MainActivity;
import com.bobo.beijingnews.base.BasePager;
import com.bobo.beijingnews.base.MenuDetailBasePager;
import com.bobo.beijingnews.domain.NewsCenterPagerBean2;
import com.bobo.beijingnews.domain.NewsCenterPagerBean;
import com.bobo.beijingnews.fragment.LeftmenuFragment;
import com.bobo.beijingnews.menudetailpager.InteracMenuDetailPager;
import com.bobo.beijingnews.menudetailpager.NewsMenuDetailPager;
import com.bobo.beijingnews.menudetailpager.PhotosMenuDetailPager;
import com.bobo.beijingnews.menudetailpager.TopicMenuDetailPager;
import com.bobo.beijingnews.menudetailpager.VoteMenuDetailPager;
import com.bobo.beijingnews.utils.CacheUtils;
import com.bobo.beijingnews.utils.Constants;
import com.bobo.beijingnews.utils.LogUtil;
import com.bobo.beijingnews.view.LEloadingView;
import com.bobo.beijingnews.volley.VolleyManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 公众号IT波 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心
 */
public class NewsCenterPager extends BasePager {

    /**准备传递给左侧菜单的数据*/
    private List<NewsCenterPagerBean2.DetailPagerData> data;

    /**详情页的集合*/
    private ArrayList<MenuDetailBasePager> detailBasePagers;

    /**
     * 网络请求的起始事件（用于对比Okhttp 和 Volley 的性能）
     */
    private long startTime;

    /**
     * 自定义Leon特色的加载loading...
     */
    private KProgressHUD mProgressHUD;


    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("新闻中心数据被加载了");

        // 点击侧划的按钮-点击事件的监听在父类中实现了
        ib_menu.setVisibility(View.VISIBLE);

        //1.设置标题
        tv_title.setText("新闻");

        // 2.联网请求，得到数据，创建视图
        // TextView textView = new TextView(context);
        // textView.setGravity(Gravity.CENTER);
        // textView.setTextColor(Color.RED);
        // textView.setTextSize(25);

        // //3.把子视图添加到BasePager的FrameLayout中
        // fl_content.addView(textView);

        // //4.绑定数据
        // textView.setText("新闻中心内容");

        //（联网请求前）获取缓存数据 默认为""
        String saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);

        //如果有缓存数据先显示缓存数据（而后再请求网络）不至于 空白
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);

            // FIXME:2020-050-17有缓存就解析缓存，不要再网络请求了
            return;
        }

        // 加载框显示
        mProgressHUD = KProgressHUD.create(context)
                                     .setCustomView(new LEloadingView(context))
                                     .setLabel("Please wait", Color.GRAY)
                                     .setBackgroundColor(Color.WHITE)
                                     .show();

        startTime = SystemClock.uptimeMillis();

        // 联网请求数据xUtils3
        getDataFromNet();

        // 联网请求Volley
        // getDataFromNetByVolley();
    }

    /**
     * 使用Volley做网络请求
     */
    private void getDataFromNetByVolley() {

        /// 创建一个请求队列（VolleyManager.getRequestQueue()代替）
        // RequestQueue queue = Volley.newRequestQueue(context);

        // String请求
        StringRequest request = new StringRequest(Request.Method.GET, Constants.NEWSCENTER_PAGER_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String result) {

                        long endTime = SystemClock.uptimeMillis();

                        long passTime = endTime - startTime;

                        LogUtil.e("Volley 请求用时:" + passTime);

                        // 请求成功的处理
                        LogUtil.e("用Volley 联网请求成功 ："+result);

                        //缓存数据本地持久化保存
                        CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);

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
     * 使用xUtils3 联网请求数据 xUtils3 application中初始化
     */
    private void getDataFromNet(){

        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("用xUtils3 联网请求成功 ："+result);

                long endTime = SystemClock.uptimeMillis();

                long passTime = endTime - startTime;

                LogUtil.e("xUtils3 请求用时:" + passTime);

                //缓存数据本地持久化保存
                CacheUtils.putString(context, Constants.NEWSCENTER_PAGER_URL, result);


                // 无论成功或是失败加载框都应消失
                mProgressHUD.dismiss();

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("用xUtils3 联网请求失败 ："+ex.getMessage());

                // 无论成功或是失败加载框都应消失
                mProgressHUD.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("用xUtils3 onCancelled ："+cex.getMessage());

                // 无论成功或是失败加载框都应消失
                mProgressHUD.dismiss();
            }

            @Override
            public void onFinished() {
                LogUtil.e("用xUtils3 onFinished()");

                // 无论成功或是失败加载框都应消失
                mProgressHUD.dismiss();
            }
        });
    }

    /**解析json数据和显示数据*/
    private void processData(String json) {

        // FIXME:2020-050-17 有缓存就解析缓存，不要再网络请求了
        if (detailBasePagers != null && detailBasePagers.size() > 0) {
            return;
        }

        NewsCenterPagerBean2 bean = parsedJson2(json);

        //给左侧菜单传递数据
        data = bean.getData();

        //本身上下文就事那传递的MainActivity
        MainActivity mainActivity = (MainActivity) context;

        //得到左菜单的fragment
        LeftmenuFragment leftmenuFragment = mainActivity.getLeftmenuFragment();

        //添加详情页面 必须要在leftmenuFragment.setData(data) 前面
        detailBasePagers = new ArrayList<>();
        detailBasePagers.add(new NewsMenuDetailPager(context,data.get(0)));//新闻中心下： 新闻详情页面
        detailBasePagers.add(new TopicMenuDetailPager(context,data.get(0)));//新闻中心下： 专题详情页面
        detailBasePagers.add(new PhotosMenuDetailPager(context, data.get(2)));//新闻中心下： 图组详情页面
        detailBasePagers.add(new InteracMenuDetailPager(context, data.get(2)));//新闻中心下： 互动详情页面
        detailBasePagers.add(new VoteMenuDetailPager(context));//新闻中心下： 投票详情页面

        //把数据传递给左侧菜单
        leftmenuFragment.setData(data);
    }

    /**
     * 解析json数据：①使用系统的api ②使用第三方框架解析例如：Gson fastJson
     *@param json json字符串
     */
    private NewsCenterPagerBean2 parsedJson2(String json) {

        //使用系统的api 解析 （手动解析） ↓
        NewsCenterPagerBean2 bean2 = new NewsCenterPagerBean2();
        try {
            JSONObject object = new JSONObject(json);
            //用getInt如果将来服务器不返回了会崩溃用optInt不会崩溃
            int retcode = object.optInt("retcode");
            bean2.setRetcode(retcode);//retcode字段就解析成功了
            JSONArray data = object.optJSONArray("data");

            if (data != null && data.length() > 0){

                List<NewsCenterPagerBean2.DetailPagerData> detailPagerDatas = new ArrayList<>();
                //设置列表数据
                bean2.setData(detailPagerDatas);

                //for循环解析每条数据
                for (int i = 0;i < data.length();i++){
                    JSONObject jsonObject = data.optJSONObject(i);

                    NewsCenterPagerBean2.DetailPagerData detailPagerData = new NewsCenterPagerBean2.
                            DetailPagerData();
                    //添加到集合中 先添加到集合中再设置数据也是可以的
                    detailPagerDatas.add(detailPagerData);

                    int id = jsonObject.optInt("id");
                    detailPagerData.setId(id);

                    int type = jsonObject.optInt("type");
                    detailPagerData.setType(type);

                    String title = jsonObject.optString("title");
                    detailPagerData.setTitle(title);

                    String url = jsonObject.optString("url");
                    detailPagerData.setUrl(url);

                    String url1 = jsonObject.optString("url1");
                    detailPagerData.setUrl1(url1);

                    String dayurl = jsonObject.optString("dayurl");
                    detailPagerData.setDayurl(dayurl);

                    String excurl = jsonObject.optString("excurl");
                    detailPagerData.setExcurl(excurl);

                    String weekurl = jsonObject.optString("weekurl");
                    detailPagerData.setWeekurl(weekurl);

                    JSONArray children = jsonObject.optJSONArray("children");

                    if (children != null && children.length() > 0){

                        List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> childrenDatas = new ArrayList<>();

                        //设置 集合到ChildrenData对象中 先设置后赋值也是可以的
                        detailPagerData.setChildren(childrenDatas);

                        //for循环解析每条数据
                        for (int j = 0; j < children.length();j++){

                            JSONObject childrenItem = children.optJSONObject(j);
                            NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData = new NewsCenterPagerBean2
                                    .DetailPagerData.ChildrenData();
                            //添加到集合在 先添加再赋值也是可以的
                            childrenDatas.add(childrenData);

                            int childId = childrenItem.optInt("id");
                            childrenData.setId(childId);

                            String childTitle = childrenItem.optString("title");
                            childrenData.setTitle(childTitle);

                            String childUrl = childrenItem.optString("url");
                            childrenData.setUrl(childUrl);

                            int childType = childrenItem.optInt("type");
                            childrenData.setType(childType);
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bean2;
    }

    /**
     * 解析json数据：①使用系统的api ②*使用第三方框架解析例如：Gson fastJson
     *@param json json字符串
     */
    private NewsCenterPagerBean2 parsedJson(String json) {
        //原来Gson用的是2.2.1 我下载了2.8.5 使用Gson解析 ↓
        //Gson gson = new Gson();
        return new Gson().fromJson(json, NewsCenterPagerBean2.class);
    }

    /**
     * 根据位置切换详情页面（本页面下的子页面）
     * @param position LeftmenuFragment左侧菜单传递过来的位置
     */
    public void switchPager(int position) {
        //1.更换页面标题也跟着更换
        tv_title.setText(data.get(position).getTitle());

        //2.移除之前的内容
        fl_content.removeAllViews();

        //3.添加新的内容
        final MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View rootView = detailBasePager.rootView;

        // 初始化数据
        detailBasePager.initData();
        fl_content.addView(rootView);

        // 导航栏右上角的切换布局的按钮（只有组图才显示）
        if (position == 2){
            // 图组页
            ib_swich_list_grid.setVisibility(View.VISIBLE);

            // 设置点击事件
            ib_swich_list_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 1.得到图组对象
                    PhotosMenuDetailPager detailPager = (PhotosMenuDetailPager) detailBasePagers.get(2);

                    // 2.调用图组对象的切换list view 和 grid view的方法
                    detailPager.switchListViewAndGridView(ib_swich_list_grid);
                }
            });
        }else{
            // 其他页面
            ib_swich_list_grid.setVisibility(View.GONE);
        }
    }
}
