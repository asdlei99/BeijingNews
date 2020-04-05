package com.bobo.beijingnews.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

/**
 * Created by 微信公众号IT波 on 2020/4/5. Copyright © Leon. All rights reserved.
 * Functions:内存缓存工具类
 */
public class MemoryCacheUtils {

    /**
     * 最少使用算法，它的核心思想就是会优先淘汰那些近期最少使用的缓存对象。最少使用算法，
     * 它的核心思想就是会优先淘汰那些近期最少使用的缓存对象。
     */
    private LruCache<String, Bitmap> lruCachel;

    public MemoryCacheUtils() {

        // 使用系统分配给应用程序的1/8作为内存缓存
        int maxSize = (int)(Runtime.getRuntime().maxMemory() / 1024 / 8);

        lruCachel = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // return super.sizeOf(key, value);
                return (value.getRowBytes() * value.getHeight() / 1024);
            }
        };
    }

    /**
     * 根据url从内存中获取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        return lruCachel.get(imageUrl);
    }

    /**
     * 将请求到的数据保存在内存中
     * @param imageUrl 图片路径
     * @param bitmap 图片
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCachel.put(imageUrl, bitmap);
    }
}
