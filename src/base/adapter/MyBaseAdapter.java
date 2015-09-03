package base.adapter;

import java.util.List;
import java.util.Map;

import time.TimeSlot;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class MyBaseAdapter extends BaseAdapter {

	protected Context context;
	protected Handler messageHandler;

	public MyBaseAdapter(final Context context) {
		this.context = context;
		messageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0: // 返回的结果
					Toast.makeText(context, msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				case 1: // 在屏幕弹出提醒消息
					Toast.makeText(context, msg.getData().getString("text"),
							Toast.LENGTH_LONG).show();
				}
			}
		};
	}

	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		return convertView;
	}
	
	protected void print(String text) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("text", text);
		msg.setData(data);
		msg.what = 1;
		messageHandler.sendMessage(msg);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
