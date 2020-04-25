package com.bobo.beijingnews.activity;

import android.app.Activity;
import android.os.Bundle;

import com.bobo.beijingnews.R;
import com.bobo.beijingnews.utils.StBarUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends Activity {

    /**
     * 上页面跳转时携带的url
     */
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        // 设置沉浸式状态栏
        StBarUtil.setOccupationHeight(this,null);

        // 获取上一页面携带过来的url
        url = getIntent().getStringExtra("url");

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        Picasso.with(this)
                .load(url)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }

                    @Override
                    public void onError() {
                    }
                });
    }
}
