package com.myweather.app.model;

import java.util.ArrayList;
import java.util.List;

import com.myweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	private static CoolWeatherDB coolweatherDB;
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
				
	}
	
	//»ñÈ¡CoolWeatherDBÊµÀý
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolweatherDB == null){
			coolweatherDB = new CoolWeatherDB(context);
		}
		return coolweatherDB;
	}
	
	//Province
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	//City
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("province_name", city.getCityName());
			values.put("province_code", city.getCityCode());
			db.insert("City", null, values);
		}
	}
	
	public List<Province> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
}
	
}






