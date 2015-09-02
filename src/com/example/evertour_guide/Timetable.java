package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.widget.Toast;

public class Timetable extends Activity {
	
	Button btnAdd;
	Button btnBack;
	ListView listView;	//时间表
	
	Date dateStart = null, dateEnd = null;
	
	Handler getTimeslotHandler,handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);
		
		initView();
		
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:	//返回的结果
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				case 1:	//在屏幕弹出提醒消息
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("text"),
							Toast.LENGTH_LONG).show();
				}
			}
		};
		
		getTimeslotHandler = new Handler()
		{
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
				startActivity(new Intent(getApplicationContext(),AddTimeslot.class));
			}
		});
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		processExtraData();
	}
	
	private void processExtraData()
	{
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		
		//这不是由AddTimeslot启动的实例，不需要处理数据
		if(data == null)return;
		
		dateStart = new Date( data.getInt("sy"),data.getInt("sm"),data.getInt("sd"));
		dateEnd = new Date( data.getInt("ey"),data.getInt("em"),data.getInt("ed"));
		
		
	}
	
	
	
	private void print(String text)
	{
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("text", text);
		msg.setData(data);
		msg.what = 1;
		handler.sendMessage(msg);
	}
}
