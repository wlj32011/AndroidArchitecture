package com.wanglj.androidarchitecture.module.login;

import com.wanglj.androidarchitecture.data.remote.model.User;
import com.wanglj.androidarchitecture.module.base.BaseLoadView;

/**
 * Created by wanglj on 16/7/4.
 */

public interface LoginView extends BaseLoadView{
    void canLogin(boolean canLogin);
    void showUser(User user);
}
