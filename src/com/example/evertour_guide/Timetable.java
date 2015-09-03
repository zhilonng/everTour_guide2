package com.example.evertour_guide;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.application.MyApplication;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import time.TimeSlot;

import adapter.MyAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Timetable extends Activity {

	Button btnAdd;
	Button btnBack;
	ListView listView; // 时间表

	Date dateStart = null, dateEnd = null;

	Handler getTimeslotHandler, handler;
	Handler loadListHandler ;
	Handler delBtnHandler;
	
	
	ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private ArrayList<TimeSlot> timeList;

	

	final String timetableLoadRequestURL = API.UriAPI.timetableLoadRequestURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);

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
						AddTimeslot.class));
				finish();
			}
		});

		listView = (ListView) findViewById(R.id.timelist);
		// SimpleAdapter adapter = new Simple
	}

	private void init() {
		
		loadListHandler = new Handler() {
			public void handleMessage(Message msg) {
				loadList();
			};
		};
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0: // 返回的结果
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				case 1: // 在屏幕弹出提醒消息
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("text"), Toast.LENGTH_LONG)
							.show();
				}
			}
		};

		getTimeslotHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle data = msg.getData();
				int yearStart = Integer.valueOf(data.getString("yearStart"));
				int monthStart = Integer.valueOf(data.getString("monthStart"));
				int dayStart = Integer.valueOf(data.getString("dayStart"));
				int yearEnd = Integer.valueOf(data.getString("yearEnd"));
				int monthEnd = Integer.valueOf(data.getString("monthEnd"));
				int dayEnd = Integer.valueOf(data.getString("dayEnd"));
			}
		};

		
		
		Thread addThread = new Thread(new Runnable() {

			@Override
			public void run() {
				print(sendRequestToHost());
			}
		});
		addThread.start();
	}

	private void loadList() {
		int length = timeList.size();
		for (int i = 0; i < length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("time_start", timeList.get(i).sy + "年"
					+ timeList.get(i).sm + "月" + timeList.get(i).sd + "日");
			item.put("time_end", timeList.get(i).ey + "年" + timeList.get(i).em
					+ "月" + timeList.get(i).ed + "日");
			mData.add(item);
		}
		MyAdapter adapter = new MyAdapter(getApplicationContext(),mData, timeList);
		listView.setAdapter(adapter);

	}

	private String sendRequestToHost() {
		HttpPost httpRequest = new HttpPost(timetableLoadRequestURL);

		MyApplication myApp = (MyApplication) getApplicationContext();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("guide_id", String
				.valueOf(myApp.guide_id)));

		try {
			// 添加请求参数到请求对象
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 发送请求并等待响应
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 处理返回的信息

				String jsonStr = EntityUtils.toString((HttpEntity) httpResponse
						.getEntity());
				JSONArray jsonArray = new JSONArray(jsonStr);

				timeList = new ArrayList<TimeSlot>();

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject o = (JSONObject) jsonArray.get(i);
					timeList.add(new TimeSlot(o.getInt("start_year"), o
							.getInt("start_month"), o.getInt("start_day"), o
							.getInt("end_year"), o.getInt("end_month"), o
							.getInt("end_day")));
				}

				loadListHandler.sendEmptyMessage(0);
				
			} else {
				Log.e("MainScreen:Http Post", "Error Response: "
						+ httpResponse.getStatusLine().toString());
				print("Error Response: "
						+ httpResponse.getStatusLine().toString());
				return "";
			}
		} catch (ClientProtocolException e) {
			// Log.e("MainScreen:Http Post", e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			// Log.e("MainScreen:Http Post", e.getMessage().toString());
			e.printStackTrace();
		} catch (Exception e) {
			// Log.e("MainScreen:Http Post", e.getMessage().toString());
			e.printStackTrace();
		}
		return "";

	}
	
	private void print(String text) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("text", text);
		msg.setData(data);
		msg.what = 1;
		handler.sendMessage(msg);
	}
	
}
