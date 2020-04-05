package com.bobo.beijingnews.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by 微信公众号IT波 on 2020/4/5. Copyright © Leon. All rights reserved.
 * Functions: 本地缓存工具类 互动菜单模块在使用 InteracMenuDetailPager
 */
public class LocalCacheUtils {

    private MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 根据Url获取图片
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {

        // 判断sdcard是否存在（可用）
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            /**
             * 保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxx.png
             * 对图片的名字进行MD5加密  保存图片在/mnt/sdcard/beijingnews/jjmkklnnlnlknnlnlnkl
             */
            try {
                String fileNmae = MD5Encoder.encode(imageUrl);

                // 存储在SD卡中的位置
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews",
                        fileNmae);

                // 如果文件存在
                if (file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap != null){
                        memoryCacheUtils.putBitmap(imageUrl, bitmap);
                        Log.e("LocalCacheUtils", "把图片从本地保存到内存中");
                    }
                    return bitmap;
                }

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("LocalCacheUtils", "本地图片获取失败");
            }
        }

        return null;
    }

    /**
     * 根据Url保存图片
     * @param imageUrl 图片的url
     * @param bitmap 图片
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {

        // 判断sdcard是否存在（可用）
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            /**
             * 保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxx.png
             * 对图片的名字进行MD5加密  保存图片在/mnt/sdcard/beijingnews/jjmkklnnlnlknnlnlnkl
             */
            try {
                String fileNmae = MD5Encoder.encode(imageUrl);

                // 存储在SD卡中的位置
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews",
                        fileNmae);

                // 得到文件的上级目录
                File parentFile = file.getParentFile();

                if (!parentFile.exists()){
                    // 创建目录
                    parentFile.mkdirs();
                }

                // 如果文件不存在
                if (!file.exists()){
                    // 创建出来文件
                    file.createNewFile();
                }

                // 保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("LocalCacheUtils", "图片本地保存失败");
            }
        }
    }
}
