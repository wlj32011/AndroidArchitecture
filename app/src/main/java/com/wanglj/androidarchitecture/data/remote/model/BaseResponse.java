package com.wanglj.androidarchitecture.data.remote.model;

/**
 * Created by wanglj on 16/7/4.
 */

public class BaseResponse<T> {

    private int status_code;
    private String status_msg;
    private T data;


    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
