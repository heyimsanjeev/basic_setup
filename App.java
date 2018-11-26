package com.app.motiv.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.motiv.R;
import com.app.motiv.utils.helpers.SharedPreferenceHelper;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by 123 on 04-Jan-18.
 */

public class App extends Application {

    private static App instance;

    public static App getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        iniSharedHelper();
        twitterConfig();
    }

    private void iniSharedHelper() {
        SharedPreferenceHelper.init(this);
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    // for twitter
    private void twitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.consumer_key), getString(R.string.consumer_secret)))
                .debug(true)
                .build();

        Twitter.initialize(config);
    }
}
