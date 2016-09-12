package com.ken.fcmandroidinights.services;

import android.provider.SyncStateContract;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ken on 11/09/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

//        Log.d("myLog", "Refreshed token: " + refreshedToken);
    }

}
