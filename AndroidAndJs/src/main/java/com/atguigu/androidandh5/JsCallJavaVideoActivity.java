package com.atguigu.androidandh5;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 作者：尚硅谷-杨光福 on 2016/7/28 11:19
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：java和js互调
 */
public class JsCallJavaVideoActivity extends Activity {

    private WebView webview;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_java_video);
        webview = (WebView) findViewById(R.id.webview);
        //设置支持javaScript
        webSettings = webview.getSettings();
        //设置支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);
        //设置文字大小
        webSettings.setTextZoom(100);
        //不让从当前网页跳转到系统的浏览器中
        webview.setWebViewClient(new WebViewClient() {
            //当加载页面完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        //添加javaScript接口
        webview.addJavascriptInterface(new MyJavascriptInterface(),"android");

        //可以加载网络的页面，也可以加载应用内置的页面
        webview.loadUrl("file:///android_asset/RealNetJSCallJavaActivity.htm");
//        webview.loadUrl("http://192.168.21.165:8080/RealNetJSCallJavaActivity.htm");

    }

    /**
     * 内部类实现Java调用JavaScript
     * 内部类的方法中加上 @JavascriptInterface 注解可以适配新老版本的安卓手机
     */
    private class MyJavascriptInterface {

        @JavascriptInterface
        public void playVideo(int id,String videoUrl,String title){
            // 隐式意图
            Intent intent = new Intent();
            // 打开系统播放器
            intent.setDataAndType(Uri.parse(videoUrl),"video/*");
            startActivity(intent);
        }
    }
}
