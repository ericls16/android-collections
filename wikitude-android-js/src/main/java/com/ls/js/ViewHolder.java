package com.ls.js;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.CheckResult;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Jiang Chen on 16/4/24.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding mBinding;

    public ViewHolder(View itemView) {
        super(itemView);
        mBinding = DataBindingUtil.bind(itemView);
    }

    @CheckResult
    public ViewDataBinding getBinding() {
        return mBinding;
    }
}
