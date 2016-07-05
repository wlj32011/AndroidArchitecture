package com.wanglj.androidarchitecture.utils;

import android.text.TextUtils;

/**
 * Created by wanglj on 16/6/29.
 */

public class Validator {

    public Validator() {

    }

    public boolean validUsername(String username) {
        return !TextUtils.isEmpty(username);
    }

    public boolean validPassword(String password) {
        return !TextUtils.isEmpty(password);
    }
}
