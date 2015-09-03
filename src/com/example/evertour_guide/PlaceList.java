package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.activity.MyActivity;
import my.application.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.structor.ProvinceIdPos;

import adapter.PlaceListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PlaceList extends MyActivity {

	Button btnBack, btnAdd;
	ListView lvPlace;

	List<String> provinceList,cityList;

	Handler drawPlaceListHandler;

	final String getPlaceListURL = API.UriAPI.getPlaceListURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);

		initView();
		init();
	}

	private void initView() {
		btnBack = (Button) findViewById(R.id.back);
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
				startActivity(new Intent(getApplicationContext(),
						AddPlace.class));
			}
		});

		lvPlace = (ListView) findViewById(R.id.place_list);

	}

	private void init() {

		drawPlaceListHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				drawPlaceList();
			}
		};
		
		Thread getPlaceListThread = new Thread(new Runnable() {

			@Override
			public void run() {

				MyApplication myApp = getApplicationContext();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("guide_id", String
						.valueOf(myApp.guide_id)));

				try {
					String result = EntityUtils.toString(sendRequestToHost(
							getPlaceListURL, params));
					JSONArray jArray = new JSONArray(result);
					provinceList = new ArrayList<String>();
					cityList = new ArrayList<String>();
					print(provinceList.toString());
					print(cityList.toString());
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject jo = (JSONObject) jArray.get(i);
						provinceList.add(jo.getString("province"));
						cityList.add(jo.getString("city"));
					}
					drawPlaceListHandler.sendEmptyMessage(0);
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
		getPlaceListThread.start();

	}

	private void drawPlaceList() {
		PlaceListAdapter adapter = new PlaceListAdapter(
				getApplicationContext(), provinceList,cityList);
		lvPlace.setAdapter(adapter);
	}
}
