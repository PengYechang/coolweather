package com.myweather.app.util;

public interface HttpCallbackLister {
	void onFinish(String response);
	
	void onError(Exception e);
}
