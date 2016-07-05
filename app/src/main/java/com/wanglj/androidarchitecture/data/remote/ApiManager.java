package com.wanglj.androidarchitecture.data.remote;

import android.app.Application;

import com.wanglj.androidarchitecture.data.remote.model.BaseResponseFunc;
import com.wanglj.androidarchitecture.data.remote.model.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wanglj on 16/7/4.
 */

public class ApiManager {

    private final ApiService apiService;

    private final Application application;

    public ApiManager(ApiService apiService, Application application) {
        this.apiService = apiService;
        this.application = application;
    }

    public void login(String username, String password,SimpleCallback<User> simpleCallback){
         apiService.login(username,password)
                 .flatMap(new BaseResponseFunc<User>())
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new ExceptionSubscriber<User>(simpleCallback,application));
    }

}
