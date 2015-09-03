package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import my.activity.MyActivity;
import my.application.MyApplication;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPlace extends MyActivity {

	Button btnBack;
	Button btnAdd;
	Spinner spinnerProvince, spinnerCity;

	Handler printHandler;

	int province_id = 0, city_id = 0;

	final String addPlaceRequestURL = API.UriAPI.addPlaceRequestURL;
	final String getProvinceRequestURL = API.UriAPI.getProvinceRequestURL;

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
						if (province_id == 0 || city_id == 0) {
							print("请选择省份和城市");
							return;
						}
						try {
							String result = EntityUtils.toString(sendRequestToHost(
									addPlaceRequestURL, null));
							print(result);
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
	}
	
	private void init()
	{
		
	}

	
	
}