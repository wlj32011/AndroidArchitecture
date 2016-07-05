package com.wanglj.androidarchitecture.module.login;

import dagger.Subcomponent;

/**
 * Created by wanglj on 16/7/4.
 */
@Subcomponent(modules = LoginModule.class)
public interface LoginComponent {
    LoginActivity inject(LoginActivity loginActivity);
}
