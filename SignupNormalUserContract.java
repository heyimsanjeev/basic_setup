package com.app.motiv.ui.authenticate.contract;

import com.app.motiv.data.shared.DataResponse;
import com.app.motiv.ui.base.BasePresentor;
import com.app.motiv.ui.base.BaseView;

/**
 * Created by 123 on 13-Feb-18.
 */

public interface SignupNormalUserContract {

    interface View extends BaseView {

        void onSuccessNormalUserSignup(DataResponse dataResponse);

        void onFailure(String message);
    }

    interface Presentor extends BasePresentor {

        void signupNormalUser(String user_image, String name, String userName, String email, String password, String phoneNum,
                              String age, String publicInterest, String musicInterest,String referralCode);
    }

    interface Intracter {

        void signupNormalUser(String user_image, String name, String userName, String email, String password, String phoneNum, String age,
                              String publicInterest, String musicInterest,String referralCode, OnCompleteListener callback);
    }

    interface OnCompleteListener {

        void onSuccessNormalUserSignup(DataResponse dataResponse);

        void onError(String error);
    }
}
