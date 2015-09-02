package my.activity;

import my.application.MyApplication;

import android.app.Activity;
import android.os.Bundle;

public class MyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication myApp = (MyApplication)getApplicationContext();
		myApp.activityTable.put(this.getLocalClassName(), this);
	}
	
	@Override
	public void finish() {
		MyApplication myApp = (MyApplication)getApplicationContext();
		if(myApp.activityTable.containsKey(this.getLocalClassName()))
		{
			myApp.activityTable.remove(this.getLocalClassName());
		}
		super.finish();
	}
	
}
