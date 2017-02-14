package com.ls.js;

import android.app.Application;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by Jiang Chen on 2016/11/14.
 */
public class App extends Application {

    {
        PlatformConfig.setWeixin("wxf1a512a82131e88e", "822f4afb18fb7625359a2238b83f1519");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initUMConfig();
    }

    private void initUMConfig() {
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        UMShareAPI.get(this);
    }
}
