package com.bobo.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 求知自学网 on 2019/7/20. Copyright © Leon. All rights reserved.
 * Functions: 新闻中心
 */
public class NewsCenterPager extends BasePager {

    /**准备传递给左侧菜单的数据*/
    private List<NewsCenterPagerBean2.DetailPagerData> data;

    /**详情页的集合*/
    private ArrayList<MenuDetailBasePager> detailBasePagers;


    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        LogUtil.e("新闻中心数据被加载了");

        //点击侧划的按钮-点击事件的监听在父类中实现了
        ib_menu.setVisibility(View.VISIBLE);

        //1.设置标题
        tv_title.setText("新闻中心");

        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView);

        //4.绑定数据
        textView.setText("新闻中心内容");

        //（联网请求前）获取缓存数据 默认为""
        String saveJson = CacheUtils.getString(context,Constants.NEWSCENTER_PAGER_URL);

        //如果有缓存数据先显示缓存数据（而后再请求网络）不至于 空白
        if (!TextUtils.isEmpty(saveJson)){
            processData(saveJson);
        }

        //联网请求数据
        getDataFromNet();
    }

    /**使用xUtils3 联网请求数据 xUtils3 application中初始化*/
    private void getDataFromNet(){

        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);

        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                LogUtil.e("用xUtils3 联网请求成功 ："+result);

                //缓存数据本地持久化保存
                CacheUtils.putString(context,Constants.NEWSCENTER_PAGER_URL,result);

                processData(result);

                //设置适配器
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("用xUtils3 联网请求失败 ："+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("用xUtils3 onCancelled ："+cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("用xUtils3 onFinished()");
            }
        });
    }

    /**解析json数据和显示数据*/
    private void processData(String json) {
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
        detailBasePagers.add(new PhotosMenuDetailPager(context));//新闻中心下： 图组详情页面
        detailBasePagers.add(new InteracMenuDetailPager(context));//新闻中心下： 互动详情页面
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
        MenuDetailBasePager detailBasePager = detailBasePagers.get(position);
        View rootView = detailBasePager.rootView;
        detailBasePager.initData();//初始化数据
        fl_content.addView(rootView);
    }
}
