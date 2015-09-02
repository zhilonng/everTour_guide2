package com.example.evertour_guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Timetable extends Activity {
	
	Button btnAdd;
	Button btnBack;
	
	Handler getTimeslotHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timetable);
		
		initView();
		
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
}
