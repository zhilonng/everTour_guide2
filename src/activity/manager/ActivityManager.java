package activity.manager;

import java.util.Hashtable;

import android.app.Activity;

public class ActivityManager extends Hashtable<String,Activity> {
	
	@Override
	public synchronized Activity remove(Object key) {
		Activity activity = get(key);
		activity.finish();
		return  activity;
	}
	
}
