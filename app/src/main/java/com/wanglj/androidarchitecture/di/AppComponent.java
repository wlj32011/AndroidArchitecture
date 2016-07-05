package com.wanglj.androidarchitecture.di;

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
