package com.baway.acer.library;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by acer on 2017/3/16.
 * OkHttp的工具類
 */

public class OkHttp_Utils {
    //定义静态类变量
    private static OkHttp_Utils instans;
    //定义okhttp网络请求
    private OkHttpClient client;

    //初始化gson
    Gson gson;
    //定义Handler并初始化
    private Handler mHandler = new Handler(Looper.getMainLooper()){};

    //创建對象接口
   public interface ClassCallBack<T>{
        void success(T t);
        void fail(String meg);
    }

    //创建集合接口
   public interface ListCallBack<T>{
        void success(List<T> t);
        void fail(String meg);
    }

    //初始化方法
    private OkHttp_Utils(){
        gson=new Gson();
        //okhttLog日志类
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .build();
    }
    //创建单例模式的静态方法
    public static OkHttp_Utils getInstan(){
        //双重检锁
        if (instans == null){
            synchronized (OkHttp_Utils.class){
                if (instans == null){
                    //实例化本工具类
                    instans = new OkHttp_Utils();
                }
            }
        }
            return instans;
    }
    //请求网络数据的(集合)方法    参数1是接口  2是传接口参数的集合                                                     3是接口
    public <T> void getOkHttpList(String url, Map<String,Object> map, final Type type, final ListCallBack callback){

        //调用拼接URl接口的方法
       String urlGet = makeGetUrl(url,map);
        //实例化Request,这里可以添加头文件
        final Request request = new Request.Builder()
                .url(urlGet)
                .build();
        //实例化Call对象
        Call call = client.newCall(request);

        //异步
        call.enqueue(new Callback() {
            @Override//请求数据失败方法
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp",e.getMessage());
                        callback.fail(e.getMessage());
                    }
                });

            }

            @Override//请求成功的方法
            public void onResponse(Call call, Response response) throws IOException {
                  //返回的整体接口
                  String url = response.body().string();
                //解析接口
                final List<T> o = gson.fromJson(url, type);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(o);
                    }
                });


            }
        });
    }


    //请求网络数据的(對象)方法      参数1是接口  2是传接口参数的集合                                                     3是接口
    public <T>  void getOkHttpClass(String url, Map<String,Object> map, final Class<T> tepecalss, final ClassCallBack callback){

        //调用拼接URl接口的方法
        String urlGet = makeGetUrl(url,map);

        //实例化Request,这里可以添加头文件
        final Request request = new Request.Builder()
                .url(urlGet)
                .build();
        //实例化Call对象
        Call call = client.newCall(request);

        //异步
        call.enqueue(new Callback() {
            @Override//请求数据失败方法
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("OkHttp",e.getMessage());
                        callback.fail(e.getMessage());
                    }
                });

            }

            @Override//请求成功的方法
            public void onResponse(Call call, Response response) throws IOException {
                //返回的整体接口
                String url = response.body().string();
                //解析接口
               final Object o=  gson.fromJson(url,tepecalss);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(o);
                    }
                });
            }
        });
    }

    //拼接URl接口thtrurygfsdghf
    private String makeGetUrl(String url, Map<String,Object> map) {

        StringBuffer sb = new StringBuffer();
        if(map!=null){
            for(String key : map.keySet()){
                sb.append(key);
                sb.append("=");
                sb.append(map.get(key));
                sb.append("&");
            }
        }else {

            return url;
        }
        String urlGet = url+"?"+sb.substring(0,sb.length()-1);
     return urlGet;
    }
}
