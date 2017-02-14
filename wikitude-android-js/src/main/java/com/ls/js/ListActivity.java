package com.ls.js;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;

import com.ls.js.adapter.CommonAdapter;
import com.ls.js.databinding.ActivityListBinding;
import com.ls.js.databinding.ItemListBinding;
import com.ls.js.model.Article;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.RxActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;

/**
 * Created by Jiang Chen on 2017/1/16.
 */
public class ListActivity extends RxActivity implements View.OnClickListener{

    private List<Article> mDataSet;
    private CommonAdapter<Article> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        mDataSet = new ArrayList<>();
        mAdapter = new CommonAdapter<>(R.layout.item_list, mDataSet, new Action2<ViewDataBinding, Integer>() {
            @Override
            public void call(ViewDataBinding viewDataBinding, Integer integer) {
                ItemListBinding b= (ItemListBinding) viewDataBinding;
                String url=Url.url+mDataSet.get(integer).getImage();
                Log.i("LOG_CAT","URL="+url);
                b.image.setImageURI(url);
            }
        });
        mAdapter.setOnItemClickAction(integer -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", mDataSet.get(integer).getId());
            startActivity(intent);
        });
        binding.rvList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        binding.rvList.setAdapter(mAdapter);

        ServiceFactory.create(RetrofitService.class)
                .articles(getIntent().getIntExtra("id", -1))
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Article>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Article> articles) {
                        mDataSet.addAll(articles);
                        mAdapter.notifyDataSetChanged();
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
