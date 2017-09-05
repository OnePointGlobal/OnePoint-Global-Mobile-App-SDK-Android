package com.onepointglobal.mysurveysn.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by kiran on 27-03-2017.
 */

public class MyFirebaseInstanceId extends FirebaseInstanceIdService {
    String token;

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("REG",token);
    }
}
