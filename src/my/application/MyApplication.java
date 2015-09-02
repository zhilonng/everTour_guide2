package my.application;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	
	public int guide_id;
	
	public Hashtable<String,Activity> activityTable;
	
	@Override
	public void onCreate() {
		super.onCreate();
		activityTable = new Hashtable<String,Activity>();
	}

}
