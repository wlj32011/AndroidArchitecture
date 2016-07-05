#概述
  原来一直在用Android最原生的框架进行开发，最多也就使用了butterknife,减少了很多的findviewById。前段时间看google的IO大会，偶尔听到了新型的Android开发框架dagger2等等，然后对此框架产生了浓厚的兴趣。
  
  
  通过一段时间的学习，把我的学习分享出来，希望大家能够喜欢。
  
  
  mvp+dagger2+retrofit2+rxjava 一套开发模式自我感觉将是以后Android开发的趋势，尽早的用起来吧。
  
  使用新型框架能给我们带来什么好处？
  * 解耦，降低模块耦合度。
  * 可以更方便的写单元测试。
  * 减少Activity编码
  * 提高团队协作的效率
  * 提高编码的效率
  * 提高代码的可读性


#示例：
  本文示例功能：
  * retrofit2+Rxjava进行Http和Https网络请求封装
  * MVP工程结构
  * Rxjava的使用示例
  * dagger2的使用示例
  

#说明
  阅读此文首先你要对以下技术有一定的了解。对以上技术还不熟悉的朋友可以先去了解一下。
  在我阅读过无数相关技术文章之后，我给大家推荐这些技术学习的文章：
####dagger2
  理论：<http://android.jobbole.com/82694/> 
  实例(网络上没有找到合适的，我自己写的一篇博文)：<http://www.jianshu.com/p/269c3f70ec1e>
  官方:<http://google.github.io/dagger/>
####mvp:
  这个理论很简单，自己百度或者google吧
  示例：<https://github.com/googlesamples/android-architecture>
####retrofit2:
  官方：<http://square.github.io/retrofit/>
####rxjava:
  偏理论:<http://gank.io/post/560e15be2dca930e00da1083>
  偏实践：<http://blog.chinaunix.net/uid-20771867-id-5187376.html>

对上面的技术有一定的了解后，我们开始一个示例：

#架构搭建
  首先我们要一个示例的方式来详细说明整体项目的架构与思想
####示例功能；
  * 登录功能
    * 检查用户名和密码是否合法
    * 登录按钮如果不合法则不可点击，合法后登录按钮可以点击
    * 调用登录接口进行登录
    * 将用户名和密码保存本地
  * 文章列表
    * 从网络获取文章列表并展示
    * 将文章列表保存到数据库
    * 点击列表进入文章详情
    * 网络获取图片
  * 单元测试
    * 集成测试
    * 单元测试


####整体架构图

![未命名.png](https://raw.githubusercontent.com/wlj32011/AndroidArchitecture/master/capture/mvp.png)

####运行webserver json

```command
  //安装json-server
  $ npm install -g json-server
  //进入工程目录
  $ cd AndroidArchitecture/
  //运行服务
  json-server --watch login.json
```

运行后可以通过此地址访问接口
  <http://localhost:3000/users>
  <http://localhost:3000/topics>



####关于工程
由于工程代码较多，在这里就不一一将代码贴出了，详细的demo地址见我的github
<https://github.com/wlj32011/AndroidArchitecture>

下图为demo目录接口，查看demo源码可以参考此结构
![AndroidArchitecture.png](https://raw.githubusercontent.com/wlj32011/AndroidArchitecture/master/capture/AndroidArchitecture.png)


####关键代码

* 网络请求返回消息体统一错误处理

  消息体结构

```json
//登录
{
  "status_msg" : "登录成功",
  "status_code" : 200,
  "data" : {
    "username" : "admin",
    "id" : 1,
    "password" : "123456",
    "gender" : "男"
  }
}
```

  模型结构

```java
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
```

使用Rxjava对象变换

```java
public class BaseResponseFunc<T> implements Func1<BaseResponse<T>, Observable<T>> {


    @Override
    public Observable<T> call(BaseResponse<T> tBaseResponse) {
        //遇到非200错误统一处理,将BaseResponse转换成您想要的对象
        if (tBaseResponse.getStatus_code() != 200) {
            return Observable.error(new Throwable(tBaseResponse.getStatus_msg()));
        }else{
            return Observable.just(tBaseResponse.getData());
        }
    }
}
```

自定义订阅者

```java
/**
 * 错误统一处理
 *
 * Created by wanglj on 16/7/4.
 */

public class ExceptionSubscriber<T> extends Subscriber<T> {

    private SimpleCallback<T> simpleCallback;
    private Application application;

    public ExceptionSubscriber(SimpleCallback simpleCallback, Application application){
        this.simpleCallback = simpleCallback;
        this.application = application;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(simpleCallback != null)
            simpleCallback.onStart();
    }

    @Override
    public void onCompleted() {
        if(simpleCallback != null)
            simpleCallback.onComplete();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException) {
           Toast.makeText(application, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
           Toast.makeText(application, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
           Toast.makeText(application, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(simpleCallback != null)
            simpleCallback.onComplete();
    }

    @Override
    public void onNext(T t) {
        if(simpleCallback != null)
            simpleCallback.onNext(t);
    }
}
```

简单的回调模型

```java
public interface SimpleCallback<T> {
    void onStart();
    void onNext(T t);
    void onComplete();
}
```

presenter层调用

```java
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
```

*  对外提供ApiManager以及retrofit的封装

```java
@Module
public class ApiModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //添加logo日志打印网络请求的拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(ApiService.SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit restAdapter) {
        return restAdapter.create(ApiService.class);
    }

    @Provides
    @Singleton
    public ApiManager provideApiManager(Application application,ApiService githubApiService) {
        return new ApiManager(githubApiService,application);
    }

}
```
*  所有的全局共用对象都可以在AppModule里对外提供，比如PreferencesManager DatabaseManager等等

####更高级的用法--dagger2 划分更细的scope

目前demo示例是将功能模块直接依赖于整个APP，其实我们可以划分更细的作用域。使一个对象的生命周期存在于多个功能模块中。

比如：项目中登录成功后，获取文章列表需要用户信息，获取文章详情以及文章下的评论列表，又需要当前文章和用户的信息。那么我们就可以这样设计我们的工程架构如图：

![scope.png](https://raw.githubusercontent.com/wlj32011/AndroidArchitecture/master/capture/scope.png)


###后记

**由于写的比较仓促，架构图中的红色字体部分还未实现，更细的scope还没有在demo中体现出来，如果您对此感兴趣，请关注，后续会陆续更新。**

**由于表达能力有限，可能有些地方解释的不是很清楚，欢迎在下方评论，一起讨论一起进步~**



  
  
