package com.app.motiv.data;

import android.util.Log;

import com.app.motiv.BuildConfig;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;


/**
 * Created by 123 on 25-09-2017.
 */

public class Injector {

    private static Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideHeaderInterceptor())
                .addInterceptor(new ForbiddenInterceptor())
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("Injector", message);
                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
        return httpLoggingInterceptor;
    }

    private static Interceptor provideHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance();
                String accessToken = helper.getAccessToken();
                if (helper.getIsLogin() && !accessToken.isEmpty()) {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer " + accessToken).build();
                    return chain.proceed(request);
                } else {
                    Request request = chain.request().newBuilder().build();
                    return chain.proceed(request);
                }
            }
        };
    }

    public static InterfaceApi provideApi() {
        return provideRetrofit(InterfaceApi.BASE_URL).create(InterfaceApi.class);
    }

    public static class ForbiddenInterceptor implements Interceptor {

        @Override
        public Response intercept(final Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.code() == 401 || response.code() == 403) {
//                App.startLoginActivity(response.code());
            }
            return response;
        }
    }
}
