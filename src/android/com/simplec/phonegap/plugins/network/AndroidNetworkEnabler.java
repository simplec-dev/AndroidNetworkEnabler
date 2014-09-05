package com.simplec.phonegap.plugins.network;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class AndroidNetworkEnabler extends CordovaPlugin implements Runnable {

	private static final String LOG_TAG = "AndroidNetworkEnabler";
	private Object syncObj = new Object();
	private ScheduledExecutorService scheduledExecutorService = null;

	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		Log.i(LOG_TAG, "In initialize method");
		schedule();
	}
	
	@Override
	public void onDestroy() {
		Log.i(LOG_TAG, "In onDestroy method");
		unschedule();
		super.onDestroy();
	}

	@Override
	public void run() {
		Log.i(LOG_TAG, "In run method");
		ConnectivityManager connManager = (ConnectivityManager) webView
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo current = connManager.getActiveNetworkInfo();

		if (current != null && current.isConnectedOrConnecting()) {
			Log.i(LOG_TAG, "There is an active connection");
			return;
		}

		WifiManager wifiManager = (WifiManager) webView.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			Log.i(LOG_TAG, "Enabling Wifi");
			boolean success = wifiManager.setWifiEnabled(true);
			Log.i(LOG_TAG, "Enable Wifi returns: " + success);
		}
	}

	public void schedule() {
		unschedule();

		synchronized (syncObj) {
			scheduledExecutorService = Executors.newScheduledThreadPool(1);

			scheduledExecutorService.scheduleAtFixedRate((Runnable) this, 30,
					30, TimeUnit.SECONDS);
		}
	}

	public void unschedule() {
		synchronized (syncObj) {
			if (scheduledExecutorService != null) {
				ScheduledExecutorService exeSvc = scheduledExecutorService;
				scheduledExecutorService = null;
				exeSvc.shutdownNow();
			}
		}
	}
}
