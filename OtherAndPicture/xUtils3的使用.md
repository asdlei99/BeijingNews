#xUtils3的使用

主要功能： 数据库的操作，联网请求数据，图片请求，（注解）初始化控件。

使用方法：
1.下载解压
    GitHub地址：https://github.com/wyouflf/xUtils3

2.运行案例  （没有用过的框架可以先运行起来看看）

3.关联xUtils3的库、
    File  → New → ImportModule....  路径

4.在Application中初始化xUtils3
    public class BeijingNewsApplication extends Application {

        //所有组件被创建之前调用这个方法
        @Override
        public void onCreate() {
            super.onCreate();

            //初始化xUtils3
            x.Ext.setDebug(true);
            x.Ext.init(this);
        }
    }
    注意：BeijingNewsApplication 一定要在 AndroidManifest 中做配置：
     <application
            android:name=".BeijingNewsApplication"
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">


5.使用xUtils3注解（初始化控件）
    @ViewInject(R.id.viewpager)
    private ViewPager viewpager;
