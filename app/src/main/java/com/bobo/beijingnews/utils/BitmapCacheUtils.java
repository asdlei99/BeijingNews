package com.bobo.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by 微信公众号IT波 on 2020/3/22. Copyright © Leon. All rights reserved.
 * Functions: 图片三级缓存的工具类 互动菜单模块在使用 InteracMenuDetailPager
 * 三级缓存设计步骤：
 * ①从内存中取图片
 * ②从本地文件中取图片，向内存中保存一份
 * ③请求网络图片，获取图片，显示到控件上 ，向内存中保存一份，向本地文件中保存一份
 */
public class BitmapCacheUtils {

    /**
     * 网络缓存工具类
     */
    private NetcacheUtils netcacheUtils;

    /**
     * 本地缓存工具类
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netcacheUtils = new NetcacheUtils(handler, localCacheUtils, memoryCacheUtils);
    }

    /**
     * 根据url获取内存或本地或网络的 Bitmap
     * @param imageUrl
     * @param position
     * @return
     */
    public Bitmap getBitmap(String imageUrl, int position) {

        // 1.从内存中获取图片
        if (memoryCacheUtils != null){
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                Log.e("BitmapCacheUtils", "内存加载图片成功" + position);
                return bitmap;
            }
        }

        // 2.从本地文件中获取图片
        if (localCacheUtils != null){
           Bitmap bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
           if (bitmap != null){
               Log.e("BitmapCacheUtils", "本地加载图片成功" + position);
               return bitmap;
           }
        }

        // 3.请求网络图片
        netcacheUtils.getBitmapFromNet(imageUrl, position);

        return null;
    }
}
