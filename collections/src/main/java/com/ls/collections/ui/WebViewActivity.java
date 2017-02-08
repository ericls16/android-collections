package com.ls.collections.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ls.collections.R;
import com.ls.collections.databinding.ActivityWebviewBinding;


/**
 * webview基本配置->浏览网页
 * Created by liusong on 2016/11/24.
 */

public class WebViewActivity extends AppCompatActivity {


    private ActivityWebviewBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_webview);
        webViewConfiguration(mBinding.webView);
        initData();
    }

    private void initData() {
        String url="http://192.168.41.167/3js/examples/webgl_loader_json_xianglu.html";
        Log.i("LOG_CAT","URL="+url);
        mBinding.webView.loadUrl(url);

        //加载html代码到webview
//        mBinding.webView.loadDataWithBaseURL(null,"data string", "text/html","utf-8",null);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBinding.webView.goBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding.webView != null) {
            ViewGroup parent = (ViewGroup) mBinding.webView.getParent();
            if (parent != null) {
                parent.removeView(mBinding.webView);
            }
            mBinding.webView.removeAllViews();
            mBinding.webView.destroy();
        }
    }

    @Override
    public void onPause() {
        if(mBinding.webView!=null) {
            mBinding.webView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if(mBinding.webView!=null){
            mBinding.webView.onResume();
        }
        super.onResume();
    }

    /**
     * 配置webview
     */
    private void webViewConfiguration(WebView webView) {
        final WebSettings webSettings = webView.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8"); //编码格式
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webSettings.setSupportMultipleWindows(false);
        webSettings.setAllowFileAccess(true);

        //水平滚动条是否可用，默认不可用
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.requestFocusFromTouch();

        //API>= 19(SDK4.4)启动硬件加速，否则启动软件加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            //支持自动加载图片
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            //关闭自动加载图片
            webSettings.setLoadsImagesAutomatically(false);
        }

        //支持Dom存储API（html存储数据）
        webSettings.setDomStorageEnabled(true);

        //---------------------------------
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //点击链接继续在当前browser中响应;
                Log.i("url", ""+url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //在页面装入完成后设置加载图片
                if(!webSettings.getLoadsImagesAutomatically()) {
                    webSettings.setLoadsImagesAutomatically(true);
                }
            }
        });
    }

}
