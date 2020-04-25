package com.bobo.okhttpsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bobo.okhttpsample.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int GET = 1;
    private static final int POST = 2;
    private Button btnGet;
    private Button btnPost;
    private TextView tvResult;

    private OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case GET:

                    // 在文本视图上显示请求回来的数据
                    tvResult.setText("GET请求回来的数据：" + msg.obj);

                    break;

                case POST:
                    // 在文本视图上显示请求回来的数据
                    tvResult.setText("POST请求回来的数据：" + msg.obj);
                    break;
            }
        }
    };

    /**
     * 实例化各个子控件
     */
    private void findViews() {
        btnGet = (Button)findViewById( R.id.btn_get );
        btnPost = (Button)findViewById( R.id.btn_post );
        tvResult = (TextView)findViewById( R.id.tv_result );

        btnGet.setOnClickListener( this );
        btnPost.setOnClickListener( this );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
    }

    /**
     * 各个自控键点击事件的处理
     */
    @Override
    public void onClick(View v) {
        if ( v == btnGet ) {
            // Handle clicks for btnGet
            getDataFromByGet();
        } else if ( v == btnPost ) {
            // Handle clicks for btnPost
            getDataFromByPost();
        }
    }

    /**
     * 模拟Post请求
     */
    private void getDataFromByPost(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    String result = post("http://api.m.mtime.cn/PageSubArea/TrailerList.api","");
                    System.out.println(result);
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    /**
     * 模拟Get请求
     */
    private void getDataFromByGet(){
        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    String result = getUrl("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                    Log.e("getDataFromByGet：", result);

                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 根据url请求网络文本数据
     * 注意要在子线程处理
     * get请求
     */
    private String getUrl(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * okhttp的post请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     *  {"trailers":[{"id":75289,"movieName":"《速度与激情:特别行动》曝全新中文预告","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/29/002009.16684021_120X90X4.jpg","movieId":254114,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4","videoTitle":
     *  "速度与激情：特别行动 中文预告","videoLength":146,"rating":-1,"type":["动作","冒险"],
     *  "summary":""},{"id":75272,"movieName":"《决战中途岛》预告再现海空激战","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/27/231348.59732586_120X90X4.jpg","movieId":233842,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627231412433967.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627231412433967.mp4","videoTitle":
     *  "决战中途岛 预告片","videoLength":120,"rating":-1,"type":["动作","剧情","历史","战争"],
     *  "summary":""},{"id":75270,"movieName":"小K领衔新版《霹雳娇娃》帅酷预告","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/27/224744.68512147_120X90X4.jpg","movieId":228417,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/28/mp4/190628075308350550.mp4","hightUrl":
     *  http://vfx.mtime.cn/Video/2019/06/28/mp4/190628075308350550.mp4","videoTitle":
     *  "霹雳娇娃 首款预告片","videoLength":157,"rating":0,"type":["动作","冒险","喜剧"],"summary"
     *  :""},{"id":75271,"movieName":"郑秀文《花椒之味》预告刘德华客串","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/27/225551.29349352_120X90X4.jpg","movieId":255909,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627225613276924.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627225613276924.mp4","videoTitle":"花椒之味
     *  知味版定档预告片","videoLength":116,"rating":-1,"type":["剧情","爱情"],"summary":""},{
     *  "id":75259,"movieName":"张晋《九龙不败》终极预告现飞龙出海","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/27/104144.36321374_120X90X4.jpg","movieId":234318,"url
     *  ":"http://vfx.mtime.cn/Video/2019/06/27/mp4/190627104751316049.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627104751316049.mp4","videoTitle":
     *  "九龙不败 终极版预告","videoLength":102,"rating":-1,"type":["犯罪","动作","剧情"],"summary
     *  ":""},{"id":75258,"movieName":"伊恩麦克莱恩、海伦米伦《优秀的骗子》预告","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/27/104649.48931556_120X90X4.jpg","movieId":255301,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627104816316366.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/27/mp4/190627104816316366.mp4","videoTitle":
     *  "优秀的骗子 中文预告片","videoLength":134,"rating":0,"type":["剧情"],"summary":""},{
     *  "id":75245,"movieName":"《烈火英雄》\"留言\"预告生死告别","coverImg":
     *  http://img5.mtime.cn/mg/2019/06/26/110121.18314261_120X90X4.jpg","movieId":261999,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/26/mp4/190626111517361726.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/26/mp4/190626111517361726.mp4","videoTitle":
     *  "烈火英雄 留言版预告","videoLength":85,"rating":-1,"type":["灾难","动作"],"summary":""},
     *  {"id":75202,"movieName":"《铤而走险》大鹏欧豪雨夜亡命追击","coverImg":
     *  "http://img5.mtime.cn/mg/2019/06/21/175640.99146689_120X90X4.jpg","movieId":255974,"url":
     *  "http://vfx.mtime.cn/Video/2019/06/21/mp4/190621175731672800.mp4","hightUrl":
     *  "http://vfx.mtime.cn/Video/2019/06/21/mp4/190621175731672800.mp4","videoTitle":"铤而走险
     *  “追击”版预告","videoLength":79,"rating":-1,"type":["剧情","犯罪"],"summary":""},{
     *  "id":75223,"movieName":"《星球大战9》日版预告","coverImg":"http://img5.mtime.cn/mg/2019/0
     *  6/25/090951.50753900_120X90X4.jpg","movieId":211982,"url":"http://vfx.mtime.cn/Video/2019/0
     *  6/25/mp4/190625091024931282.mp4","hightUrl":"http://vfx.mtime.cn/Video/2019/06/25/mp4/19062
     *  5091024931282.mp4","videoTitle":"星球大战9 日版预告","videoLength":166,"rating":-1,"type":[
     *  "动作","冒险","奇幻","科幻"],"summary":""},{"id":75167,"movieName":"韩国抗日战争片\"凤梧桐
     *  战斗\"预告","co
     */
}
