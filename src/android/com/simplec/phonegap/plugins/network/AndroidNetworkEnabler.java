package com.simplec.phonegap.plugins.network;

import org.apache.cordova.CordovaInterface;
import android.util.Log;
import android.os.Build;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;

public class AndroidNetworkEnabler extends CordovaPlugin {

    private static final String LOG_TAG = "AndroidNetworkEnabler";


    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
	        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
	
	        Log.d(LOG_TAG, "setMediaPlaybackRequiresUserGesture: FALSE");
        }

    }
}