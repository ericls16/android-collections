package com.ls.retrofit.vo;

/**
 * 回调信息统一封装类
 * Created by ls on 2017/2/21.
 */

public class ApiCommonVo<T> {

    private String reason;
    private int error_code;
    private T data;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
