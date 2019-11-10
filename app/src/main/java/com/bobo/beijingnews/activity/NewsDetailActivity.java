package com.bobo.beijingnews.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.utils.StBarUtil;

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibBack;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private WebView webview;
    private ProgressBar pbLoadig;
    private String url;

    //用户在新闻详情页选择的字体默认大小
    private int tempSize = 2;

    //用户在新闻详情页选择的字体真实大小
    private int realSize = tempSize;

    private WebSettings webSettings;


    /**
     * Find the Views in the layout
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvTitle = (TextView)findViewById( R.id.tv_title );
        ibMenu = (ImageButton)findViewById( R.id.ib_menu );
        ibBack = (ImageButton)findViewById( R.id.ib_back );
        ibTextsize = (ImageButton)findViewById( R.id.ib_textsize );
        ibShare = (ImageButton)findViewById( R.id.ib_share );
        webview = (WebView)findViewById( R.id.webview );
        pbLoadig = (ProgressBar)findViewById( R.id.pb_loadig );

        //这里不需要中间的标题和左菜单把它隐藏掉
        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);

        //显示返回 文本显示大小 分享 三个按钮
        ibBack.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibBack.setOnClickListener( this );
        ibTextsize.setOnClickListener( this );
        ibShare.setOnClickListener( this );
    }

    /**
     * Auto-created on 2019-11-10 15:40:19 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == ibBack ) {
            // 用户点击了返回按钮
            finish();
        } else if ( v == ibTextsize ) {
            // 用户点击了右边的设置文字大小按钮
            //Toast.makeText(this,"设置文字大小",Toast.LENGTH_SHORT).show();
            showChangeTextSizeDialog();
        } else if ( v == ibShare ) {
            // 用户点击了分享按钮
            Toast.makeText(this,"分享",Toast.LENGTH_SHORT).show();
        }
    }

    //显示选择字体大小的弹框
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");

        String[] items = {"超大字体","大字体","正常字体","小字体","超小字体"};

        //参数 ①items：单选框的内容  ②checkeditem:默认选中第几个（从0开始的） ③点击事件的监听
        builder.setSingleChoiceItems(items, tempSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });

        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //用户点击了确定真正的修改文字的大小
                realSize = tempSize;

                changeTextSize(realSize);
            }
        });

        builder.show();
    }

    //修改本页面（webview中的）文字大小
    private void changeTextSize(int realSize) {

        switch (realSize){
            case 0://"超大字体"
                webSettings.setTextZoom(200);
                break;
            case 1://"大字体"
                webSettings.setTextZoom(150);
                break;
            case 2://"正常字体"
                webSettings.setTextZoom(100);
                break;
            case 3://"小字体"
                webSettings.setTextZoom(75);
                break;
            case 4://"超小字体"
                webSettings.setTextZoom(50);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //设置沉浸式状态栏
        StBarUtil.setOccupationHeight(this,null);

        //初始化UI控件
        findViews();

        getData();
    }

    /**
     * 获取上一页面传递过来的url并让webview 加载
     */
    private void getData() {
        url = getIntent().getStringExtra("url");

        webSettings = webview.getSettings();
        //设置支持JavaScript
        webSettings.setJavaScriptEnabled(true);

        //设置图片双击变大变小
        webSettings.setUseWideViewPort(true);

        //设置增加缩放按钮
        webSettings.setBuiltInZoomControls(true);

        //设置文字的大小
        //webSettings.setTextSize(WebSettings.TextSize.NORMAL);过时的方法能用
        webSettings.setTextZoom(100);

        //不让从当前网页跳转到系统的浏览器中（解决重定向）
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //当加载页面完成的时候回调
                super.onPageFinished(view, url);
                //页面加载完成进度条隐藏
                pbLoadig.setVisibility(View.GONE);
            }
        });

        //webview.loadUrl("http://www.atguigu.com/");
        webview.loadUrl(url);
    }


}
