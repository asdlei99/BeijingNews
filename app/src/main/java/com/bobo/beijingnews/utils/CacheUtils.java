package com.bobo.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bobo.beijingnews.SplashActivity; /**
 * Created by 求知自学网 on 2019/7/6. Copyright © Leon. All rights reserved.
 * Functions: (本地持久化保存)缓存软件的一些参数和数据
 */
public class CacheUtils {

    /**
     * （根据key）得到本地持久化保存的值
     * @param context 上下文
     * @param key 必须是string类型
     * @return
     */
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }
}
