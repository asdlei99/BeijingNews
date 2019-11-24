package com.atguigu.androidandh5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.view.View;
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
public class JsCallJavaCallPhoneActivity extends Activity {


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
//        webSettings.setUseWideViewPort(true);
//        //增加缩放按钮
//        webSettings.setBuiltInZoomControls(true);
        //设置文字大小
        webSettings.setTextZoom(100);

        //添加javaScript接口
        webview.addJavascriptInterface(new MyJavascriptInterface(), "Android");

        //可以加载网络的页面，也可以加载应用内置的页面
        webview.loadUrl("file:///android_asset/JsCallJavaCallPhone.html");
//        webview.loadUrl("http://192.168.21.165:8080/JsCallJavaCallPhone.html");

        //(当加载页面完成的时候回调)不让从当前网页跳转到系统的浏览器中（顺便解决重定向）
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //当加载页面完成的时候回调
                super.onPageFinished(view, url);

                String json = "[{\"name\":\"阿福\", \"phone\":\"18600012345\"}]";
                // 调用JS中的方法
                webview.loadUrl("javascript:show('" + json + "')");
            }
        });

    }


    class MyJavascriptInterface {
        //拨打电话
        @JavascriptInterface
        public void call(String phone) {

            // TODO:拨号前 先获取打电话的权限
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
            //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            //if (ActivityCompat.checkSelfPermission(JsCallJavaCallPhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
              //  return;
            //}
            //startActivity(intent);
         }

        //加载联系人
        @JavascriptInterface
        @UiThread
         public void showcontacts(){
            //FIXME:如果写成这样，第二句的效果有可能看不到。原因：loadUrl是异步执行的。有可能第二句
            //FIXME: 后发先至。
            //String json = "[{\"name\":\"尚硅谷\", \"phone\":\"18600012345\"}]";
            // 调用JS中的方法
            //webview.loadUrl("javascript:show("+"'"+json+"'"+")");

//            // 下面的代码建议在子线程中调用
            //String json = "[{\"name\":\"阿福\", \"phone\":\"18600012345\"}]";
//            // 调用JS中的方法  '" + json + "'
            //webview.loadUrl("javascript:show('" + json + "')");

         }
    }

}
