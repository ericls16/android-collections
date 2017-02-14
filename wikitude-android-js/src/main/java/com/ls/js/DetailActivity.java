package com.ls.js;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.ls.js.databinding.ActivityDetailBinding;
import com.ls.js.model.ArticleDetail;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Jiang Chen on 2017/1/16.
 */
public class DetailActivity extends RxActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        ServiceFactory.create(RetrofitService.class)
                .articledetail(getIntent().getIntExtra("id", -1))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleDetail>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ArticleDetail detail) {
                        binding.setItem(detail);
                        String url=Url.url+detail.getImage();
                        Log.i("LOG_CAT","URL="+url);
                        binding.image.setImageURI(url);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ar:
                startActivity(new Intent(this, WikitudeActivity.class));
                return;
        }
    }
}
