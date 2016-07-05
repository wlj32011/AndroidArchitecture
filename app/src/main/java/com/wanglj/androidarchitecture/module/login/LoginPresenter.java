package com.wanglj.androidarchitecture.module.login;

import com.wanglj.androidarchitecture.data.local.PreferencesManager;
import com.wanglj.androidarchitecture.data.remote.ApiManager;
import com.wanglj.androidarchitecture.data.remote.SimpleCallback;
import com.wanglj.androidarchitecture.data.remote.model.User;
import com.wanglj.androidarchitecture.utils.Validator;

/**
 * Created by wanglj on 16/7/4.
 */

public class LoginPresenter {

    private final LoginView loginView;
    private final Validator validator;
    private final ApiManager apiManager;
    private final PreferencesManager preferencesManager;

    public LoginPresenter(LoginView loginView, Validator validator, ApiManager apiManager, PreferencesManager preferencesManager) {
        this.loginView = loginView;
        this.validator = validator;
        this.apiManager = apiManager;
        this.preferencesManager = preferencesManager;
    }


    public void checkInput(String username,String password){
        loginView.canLogin(validator.validUsername(username) && validator.validPassword(password));
    }

    public void login(String username,String password){
        apiManager.login(username, password, new SimpleCallback<User>() {
            @Override
            public void onStart() {
                loginView.showLoading();
            }

            @Override
            public void onNext(User user) {
                loginView.showUser(user);
            }

            @Override
            public void onComplete() {
                loginView.hideLoading();
            }
        });
    }

    public void saveLoginInfo(String username,String password){
        preferencesManager.saveLoginInfo(username,password);
    }

    public String getUserNameFromLocal(){
        return preferencesManager.getUserName();
    }

    public String getPasswordFromLocal(){
        return preferencesManager.getPassword();
    }
}
