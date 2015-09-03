package com.example.evertour_guide;

import my.application.MyApplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends Activity{
	
	Button btnProfiles;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainscreen);

//		Toast.makeText(getApplicationContext(), MainScreen.class.getSimpleName(), Toast.LENGTH_LONG).show();
		
		initView();
	}
	
	private void initView()
	{
		btnProfiles = (Button)findViewById(R.id.profiles);
		btnProfiles.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getApplicationContext(),Profiles.class);
				startActivity(intent);
			}
		});
	}
}
