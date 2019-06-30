package bobo.com.beijingnews;


import android.app.Activity;
import android.os.Bundle;
import bobo.com.beijingnews.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //设置沉浸式状态栏
        StBarUtil.setOccupationHeight(this,null);
    }
}
