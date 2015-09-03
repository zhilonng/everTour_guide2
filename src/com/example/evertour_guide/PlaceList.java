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
	
	Button btnBack,btnAdd;
	ListView lvPlace;
	
	List<ProvinceIdPos> placeList;
	
	Handler listViewInitHandler;
	
	final String getPlaceListURL = API.UriAPI.getPlaceListURL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_list);
		
		initView();
		init();
	}
	
	private void initView()
	{
		btnBack = (Button)findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnAdd = (Button)findViewById(R.id.add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),AddPlace.class));
			}
		});
		
		lvPlace = (ListView)findViewById(R.id.place_list);
		
	}
	
	private void init()
	{
		
		listViewInitHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				drawPlaceList();
			}
		};
		
		Thread getProvinceListHost = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					MyApplication myApp = (MyApplication)getApplicationContext();
					int guide_id = myApp.guide_id;
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("guide_id",String.valueOf(guide_id)));
					String result = EntityUtils.toString(sendRequestToHost(getPlaceListURL, params));

					//解析得到的JSON字符串
					JSONArray jArray = new JSONArray(result);
					placeList = new ArrayList<ProvinceIdPos>();
					int length = jArray.length();
					for(int i = 0; i< length ; i++){
						JSONObject jo = (JSONObject) jArray.get(i);
						placeList.add(new ProvinceIdPos((String)jo.get("province"),(Integer)jo.get("id"),i));
					}
					
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*JSONArray jsonArray = new JSONArray(result);
				List<ProvinceIdPos> pList = new ArrayList<ProvinceIdPos>();
				int length = jsonArray.length();
				for(int i=0;i<length;i++)
				{
					JSONObject o = (JSONObject) jsonArray.get(i);
					pList.add(new ProvinceIdPos(o.getString(""), id, pos))
				}*/ catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		getProvinceListHost.start();
	}
	
	private void drawPlaceList()
	{
		
	}
}
