package com.wanglj.androidarchitecture.data.remote;

import com.wanglj.androidarchitecture.data.remote.model.BaseResponse;
import com.wanglj.androidarchitecture.data.remote.model.Topic;
import com.wanglj.androidarchitecture.data.remote.model.User;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wanglj on 16/7/4.
 */

public interface ApiService {

    String SERVER_URL = "http://192.168.104.104:3000/";

    @GET("users")
    Observable<BaseResponse<User>> login(@Query("username") String username, @Query("password") String password);


    @GET("topics")
    Observable<BaseResponse<List<Topic>>> topics(@Query("job_code") String job_code, @Query("password") String password);
}
