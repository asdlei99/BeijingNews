package com.bobo.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.bobo.beijingnews.SplashActivity;
import com.bobo.beijingnews.activity.GuideActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by 求知自学网 on 2019/7/6. Copyright © Leon. All rights reserved.
 * Functions: (本地持久化保存)缓存软件的一些参数和数据
 */
public class CacheUtils {

    /**
     * （根据key）得到本地持久化保存boolean的值
     * @param context 上下文
     * @param key 必须是string类型
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    /**
     * 根据key持久化保存软件的Boolean值参数
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 根据key持久化保存软件的String值参数
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {

        // 判断sdcard是否存在（可用）
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            /**
             * 保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxx.png
             * 对图片的名字进行MD5加密  保存图片在/mnt/sdcard/beijingnews/jjmkklnnlnlknnlnlnkl
             */
            try {
                String fileNmae = MD5Encoder.encode(key);

                // 存储在SD卡中的位置
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews/files",
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

                // 保存文本数据
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(value.getBytes());
                fileOutputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CacheUtils", "文本数据保存失败");
            }
        }else{
            SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            sp.edit().putString(key,value).commit();
        }
    }

    /**
     * （根据key）得到本地持久化保存的String值
     * @param context 上下文
     * @param key 必须是string类型
     * @return
     */
    public static String getString(Context context, String key) {

        // 判断sdcard是否存在（可用）
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            /**
             * 保存图片在/mnt/sdcard/beijingnews/http://192.168.21.165:8080/xsxxx.png
             * 对图片的名字进行MD5加密  保存图片在/mnt/sdcard/beijingnews/jjmkklnnlnlknnlnlnkl
             */
            try {
                String fileNmae = MD5Encoder.encode(key);

                // 存储在SD卡中的位置
                File file = new File(Environment.getExternalStorageDirectory() + "/beijingnews/files",
                        fileNmae);

                // 如果文件存在
                if (file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) != -1){
                        stream.write(buffer, 0, length);
                    }

                    is.close();
                    stream.close();

                    String result = stream.toString();
                    return result;
                }

            } catch (Exception e) {
                e.printStackTrace();

                Log.e("LocalCacheUtils", "本地图片获取失败");
            }
        }else{
            SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
            return sp.getString(key,"");
        }

        return "";
    }
}
