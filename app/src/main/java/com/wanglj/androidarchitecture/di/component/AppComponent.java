package com.wanglj.androidarchitecture.di.component;

import com.wanglj.androidarchitecture.di.module.ApiModule;
import com.wanglj.androidarchitecture.di.module.AppModule;
import com.wanglj.androidarchitecture.module.login.LoginComponent;
import com.wanglj.androidarchitecture.module.login.LoginModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wanglj on 16/7/4.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
    LoginComponent plus(LoginModule loginModule);
}
