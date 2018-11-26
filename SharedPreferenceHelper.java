package com.app.motiv.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.motiv.utils.ErrorUtils;

/**
 * Created by 123 on 04-Jan-18.
 */

public class SharedPreferenceHelper {

    public static final String TAG = SharedPreferenceHelper.class.getSimpleName();
    public static final String NAME = "motiv";
    public static final String IS_LOGIN = "isLogin";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String USER_ID = "user_id";
    public static final String FACEBOOK_USER_ID = "facebookUserId";
    public static final String FACEBOOK_ACCESS_TOKEN = "facebookAccessToken";
    public static final String IS_SOCIAL = "is_social";
    public static final String IMAGE_PATH = "image_path";

    private static SharedPreferenceHelper instance;
    protected final SharedPreferences sharedPreferences;
    protected final SharedPreferences.Editor sharedPreferencesEditor;

    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper(context);
        }
    }

    private SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    public static SharedPreferenceHelper getInstance() {
        if (instance == null) {
            ErrorUtils.logError(TAG, "SharedPreferenceHelper was not initialized!");
        }
        return instance;
    }

    private void delete(String key) {
        if (sharedPreferences.contains(key)) {
            sharedPreferencesEditor.remove(key).commit();
        }
    }

    private void savePref(String key, Object value) {
        delete(key);

        if (value instanceof Boolean) {
            sharedPreferencesEditor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            sharedPreferencesEditor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            sharedPreferencesEditor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            sharedPreferencesEditor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            sharedPreferencesEditor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            sharedPreferencesEditor.putString(key, value.toString());
        } else if (value != null) {
            ErrorUtils.logError(TAG, "Attempting to save non-primitive preference");
        }
        sharedPreferencesEditor.commit();
    }

    protected <T> T getPref(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    protected <T> T getPref(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public void clearAll() {
        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.commit();
    }

    public void saveIsLogin(boolean isLogin) {
        savePref(IS_LOGIN, isLogin);
    }

    public boolean getIsLogin() {
        return getPref(IS_LOGIN, false);
    }

    public void saveAccessToken(String access_token) {
        savePref(ACCESS_TOKEN, access_token);
    }

    public String getAccessToken() {
        return getPref(ACCESS_TOKEN, "");
    }

    public void saveDeviceToken(String deviceToken) {
        savePref(DEVICE_TOKEN, deviceToken);
    }

    public String getDeviceToken() {
        return getPref(DEVICE_TOKEN, "");
    }

    public void saveUserId(String userId) {
        savePref(USER_ID, userId);
    }

    public String getUserId() {
        return getPref(USER_ID, "");
    }

    public String getFacebookAccessToken() {
        return getPref(FACEBOOK_ACCESS_TOKEN);
    }

    public void saveFacebookAccessToken(String accessToken) {
        savePref(FACEBOOK_ACCESS_TOKEN, accessToken);
    }

    public String getFacebookUserId() {
        return getPref(FACEBOOK_USER_ID);
    }

    public void saveFacebookUserId(String userId) {
        savePref(FACEBOOK_USER_ID, userId);
    }

    public void saveIsFromSocial(boolean isSocial) {
        savePref(IS_SOCIAL, isSocial);
    }

    public boolean getIsFromSocial() {
        return getPref(IS_SOCIAL, false);
    }

    public void saveImagePath(String imagePath) {
        savePref(IMAGE_PATH, imagePath);
    }

    public String getImagePath() {
        return getPref(IMAGE_PATH, "");
    }
}
