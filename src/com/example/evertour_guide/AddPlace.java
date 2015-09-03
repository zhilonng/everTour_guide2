package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.structor.ProvinceIdPos;

import my.activity.MyActivity;
import my.application.MyApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPlace extends MyActivity {

	Button btnBack;
	Button btnAdd;
	Spinner spinnerProvince = null, spinnerCity = null;
	
	String province,city;
	
	List<String> provinceList,cityList;

	Handler printHandler,spinnerProvinceInitHandler,spinnerCityInitHandler,loadCitySpinnerHandler;

	int province_id = 0, city_id = 0;

	final String addPlaceRequestURL = API.UriAPI.addPlaceRequestURL;
	final String getCityListRequestURL = API.UriAPI.getCityListRequestURL;
	final String getProvinceListRequestURL = API.UriAPI.getProvinceListRequestURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_place);

		initView();
		init();
	}

	private void initView() {

		btnBack = (Button)findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),PlaceList.class));
				finish();
			}
		});
		
		btnAdd = (Button) findViewById(R.id.add);
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Thread httpAddThread = new Thread(new Runnable() {

					@Override
					public void run() {
						MyApplication MyApp = (MyApplication) getApplicationContext();
						int guide_id = MyApp.guide_id;
						if (city == null) {
							print("请选择省份和城市");
							return;
						}
						try {
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							MyApplication myApp = getApplicationContext();
							params.add(new BasicNameValuePair("guide_id",String.valueOf(myApp.guide_id)));
							params.add(new BasicNameValuePair("REGION_NAME",city));
							String result = EntityUtils.toString(sendRequestToHost(
									addPlaceRequestURL, params));
							print(result);
							if(result.compareTo("succeed!") == 0)
							{
								print("添加成功");
								startActivity(new Intent(getApplicationContext(),PlaceList.class));
								finish();
							}
							else if(result.compareTo("duplicate") == 0)
							{
								print("你已经添加过这个城市了哟~");
							}
							else
							{
								print("未知错误");
							}
							return;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
				httpAddThread.start();
			}
		});
		
		spinnerProvince = (Spinner)findViewById(R.id.provinceSelector);
		spinnerCity = (Spinner)findViewById(R.id.citySelector);
	}
	
	private void init()
	{
		
		spinnerProvinceInitHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				loadProvinceSpinner();
			}
		};
		
		spinnerCityInitHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Thread getCityListThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("REGION_NAME",province));
						try {
							String result = EntityUtils.toString(sendRequestToHost(getCityListRequestURL,params));
							cityList = new ArrayList<String>();
							JSONArray jArray = new JSONArray(result);
							int length = jArray.length();
							for(int i=0;i<length;i++)
							{
								JSONObject jo = (JSONObject) jArray.get(i);
								cityList.add(jo.getString("REGION_NAME"));
							}
							loadCitySpinnerHandler.sendEmptyMessage(0);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				getCityListThread.start();
			}
		};
		
		loadCitySpinnerHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				loadCitySpinner();
			}
		};
		
		Thread getProvinceListHost = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					MyApplication myApp = (MyApplication) getApplicationContext();
					String result = EntityUtils.toString(sendRequestToHost(
							getProvinceListRequestURL, null));

					// 解析得到的JSON字符串
					JSONArray jArray = new JSONArray(result);
					provinceList = new ArrayList<String>();
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject jo = (JSONObject) jArray.get(i);
						provinceList.add((String) jo.get("REGION_NAME"));
					}
					spinnerProvinceInitHandler.sendEmptyMessage(0);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		getProvinceListHost.start();
	}

	private void loadProvinceSpinner()
	{
		print(provinceList.toString());
		ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,provinceList);
		spinnerProvince.setAdapter(provinceAdapter);
		spinnerProvince.setOnItemSelectedListener(new OnItemSelectedListener() 
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				province = parent.getItemAtPosition(position).toString();
				spinnerCityInitHandler.sendEmptyMessage(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
	}
	
	private void loadCitySpinner()
	{
		ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cityList);
		spinnerCity.setAdapter(cityAdapter);
		spinnerCity.setOnItemSelectedListener(new OnItemSelectedListener() 
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				city = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	
}