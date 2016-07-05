package com.wanglj.androidarchitecture;

import android.app.Application;
import android.content.Context;

import com.wanglj.androidarchitecture.di.AppComponent;
import com.wanglj.androidarchitecture.di.component.DaggerAppComponent;
import com.wanglj.androidarchitecture.di.AppModule;

/**
 * Created by wanglj on 16/7/4.
 */

public class ArchitectureApplication extends Application{

    private AppComponent appComponent;

    public static ArchitectureApplication get(Context context){
        return (ArchitectureApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }


    public AppComponent getAppComponent(){
        return appComponent;
    }


}
