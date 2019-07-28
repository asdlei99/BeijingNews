#开源项目ViewPageIndicator的使用:

1.下载
Github地址：https://github.com/JakeWharton/ViewPagerIndicator


2.解压导入开发工具 Androidstudio，运行案例（不熟悉可以运行案例参考学习）
https://github.com/leonInShanghai/BeijingNews/tree/master/ViewPagerIndicator_library


3.开始继承-关联ViewPagerIndicator的库
     File  → New → ImportModule....  路径

4.写布局文件

    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.viewpagerindicator.TabPageIndicator
            android:id="@+id/tabPageIndicator"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

        <android.support.v4.view.ViewPager
            android:id="@+id/viewpager"
            android:layout_weight="1"
            android:layout_width="match_parent"
            android:layout_height="0dp"/>

    </LinearLayout>


5.写代码

     //设置适配器
     viewPager.setAdapter(new MyNewsMenuDetailPagerAdapter());

     //viewPager和tabPageIndicator 关联 注意：以后监听页面的变化将会用tabPageIndicator(不要再用ViewPager了)
     tabPageIndicator.setViewPager(viewPager);

6.在适配器中重写getPageTitle(int position)方法：

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        //注意写了这个方法tabPageIndicator上就有标题了
        return children.get(position).getTitle();
    }

7.设置样式

          <activity android:name=".activity.MainActivity"
            android:theme="@style/Theme.PageIndicatorDefaults"/>

8.修改样式（根据需要修改）

    <style name="Widget.TabPageIndicator" parent="Widget">
        <item name="android:gravity">center</item>
        <item name="android:background">@drawable/vpi__tab_indicator</item>
        <item name="android:paddingLeft">22dip</item>
        <item name="android:paddingRight">22dip</item>
        <item name="android:paddingTop">12dp</item>
        <item name="android:paddingBottom">12dp</item>
        <item name="android:textAppearance">@style/TextAppearance.TabPageIndicator</item>
        <item name="android:textSize">16sp</item>
        <!--Leon在这里修改文字的颜色-->
        <item name="android:textColor">@drawable/vpi_tab_textcolor_indicator</item>
        <item name="android:maxLines">1</item>
    </style>

9.修改背景 vpi__tab_indicator

    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <!-- Non focused states                                                               Leon修改   @drawable/vpi__tab_unselected_holo 修改为:@android:color/transparent-->
        <item android:state_focused="false" android:state_selected="false" android:state_pressed="false" android:drawable="@android:color/transparent" />
                                                                                        <!-- @drawable/vpi__tab_selected_holo修改为@drawable/vpi__tab_selected_holo   -->
        <item android:state_focused="false" android:state_selected="true"  android:state_pressed="false" android:drawable="@drawable/vpi__tab_selected_holo" />

        <!-- Focused states                                                                       @drawable/vpi__tab_unselected_focused_holo修改为:@android:color/transparent-->
        <item android:state_focused="true" android:state_selected="false" android:state_pressed="false" android:drawable="@android:color/transparent" />
                                                                                        <!-- @drawable/vpi__tab_selected_holo修改为@drawable/vpi__tab_selected_holo   -->
        <item android:state_focused="true" android:state_selected="true"  android:state_pressed="false" android:drawable="@drawable/vpi__tab_selected_holo" />

        <!-- Pressed -->
        <!--    Non focused states                                                                @drawable/vpi__tab_unselected_pressed_holo修改为:@android:color/transparent-->
        <item android:state_focused="false" android:state_selected="false" android:state_pressed="true" android:drawable="@android:color/transparent" />
                                                                                        <!-- @drawable/vpi__tab_selected_holo修改为@drawable/vpi__tab_selected_holo   -->
        <item android:state_focused="false" android:state_selected="true"  android:state_pressed="true" android:drawable="@drawable/vpi__tab_selected_holo" />

        <!--    Focused states                                                                   @drawable/vpi__tab_unselected_pressed_holo修改为:@android:color/transparent-->
        <item android:state_focused="true" android:state_selected="false" android:state_pressed="true" android:drawable="@android:color/transparent" />
                                                                                       <!-- @drawable/vpi__tab_selected_holo修改为@drawable/vpi__tab_selected_holo   -->
        <item android:state_focused="true" android:state_selected="true"  android:state_pressed="true" android:drawable="@drawable/vpi__tab_selected_holo" />
    </selector>

10.改文字颜色 （根据需要设置选中和非选中的颜色）vpi_tab_textcolor_indicator

    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <!-- Non focused states                                                               根据需要设置选中和非选中的颜色-->
        <item android:state_focused="false" android:state_selected="false" android:state_pressed="false" android:color="@android:color/black" />
        <!-- 根据需要设置选中和非选中的颜色   -->
        <item android:state_focused="false" android:state_selected="true"  android:state_pressed="false" android:color="#33B5E5" />

        <!-- Focused states                                                                  根据需要设置选中和非选中的颜色-->
        <item android:state_focused="true" android:state_selected="false" android:state_pressed="false" android:color="@android:color/black" />
        <!-- 根据需要设置选中和非选中的颜色   -->
        <item android:state_focused="true" android:state_selected="true"  android:state_pressed="false" android:color="#33B5E5" />

        <!-- Pressed -->
        <!--    Non focused states                                                            根据需要设置选中和非选中的颜色-->
        <item android:state_focused="false" android:state_selected="false" android:state_pressed="true" android:color="@android:color/black" />
        <!-- 根据需要设置选中和非选中的颜色 -->
        <item android:state_focused="false" android:state_selected="true"  android:state_pressed="true" android:color="#33B5E5" />

        <!--    Focused states                                                                 根据需要设置选中和非选中的颜色-->
        <item android:state_focused="true" android:state_selected="false" android:state_pressed="true" android:color="@android:color/black" />
        <!-- 根据需要设置选中和非选中的颜色  -->
        <item android:state_focused="true" android:state_selected="true"  android:state_pressed="true" android:color="#33B5E5" />
    </selector>
