package com.example.evertour_guide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Profiles extends Activity {
	
	Button btnTimetable,btnPlaceList;
	Button btnBack;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profiles);
		
		initView();
	}
	
	private void initView()
	{
		btnTimetable = (Button)findViewById(R.id.timetable);
		btnTimetable.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),Timetable.class));
			}
		});
		
		btnBack = (Button)findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btnPlaceList = (Button)findViewById(R.id.place_list);
		btnPlaceList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),PlaceList.class));
			}
		});
	}
}
