package com.bim.posts.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import com.bim.posts.API.APIMgr;
import com.bim.posts.API.CallbackRequest;
import com.bim.posts.API.DLHttp;

import org.json.JSONObject;

import java.io.IOException;

public class AppMgr{

    public static APIMgr ApiMgr;

    private AppMgr() {
        //CODE
    }

    public static boolean isInternetAvailable(Context context){
        boolean hasInternet;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ConnectivityManager connectivityManager = context.getSystemService(ConnectivityManager.class);
            Network currentNetwork = connectivityManager.getActiveNetwork();
            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);
            if(caps != null){
                hasInternet = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            }else{
                hasInternet = false;
            }
        }else{
            try {
                Process p = Runtime.getRuntime().exec("ping -c 1 www.google.com");
                hasInternet = p.waitFor() == 0;
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                hasInternet = false;
            }
        }
        return hasInternet;
    }

    public static void requestGetWithCallback(CallbackRequest callbackRequest, String url, int method) {
        DLHttp statusReq = new DLHttp();
        statusReq.urlPath = url;
        statusReq.method = method;
        statusReq.callbackRequest = callbackRequest;
        statusReq.body = new JSONObject();
        statusReq.execute();
    }
}
