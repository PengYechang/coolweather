package com.myweather.app.service;

import com.myweather.app.receiver.AutoUpdateReceiver;
import com.myweather.app.util.HttpCallbackLister;
import com.myweather.app.util.HttpUtil;
import com.myweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				updateWeather();
			}
		});
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int eightHour = 8 * 60*60*1000;
		long triggerAtTime = SystemClock.elapsedRealtime()+eightHour;
		Intent i = new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	private void updateWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		HttpUtil.sendHttpRequest(address, new HttpCallbackLister() {
			
			@Override
			public void onFinish(String response) {
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		})
	}
}
