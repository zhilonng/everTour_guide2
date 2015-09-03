package adapter;

import java.io.IOException;
import java.util.ArrayList;
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

import time.TimeSlot;

import com.example.evertour_guide.R;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String,Object>> text;
	private LayoutInflater mInflater;
	private List<TimeSlot> timeList;
	
	Handler handler;
	
	final String timetableDeleteRequestURL = API.UriAPI.timetableDeleteRequestURL;
	
	
	public MyAdapter(final Context context,List<Map<String,Object>> text,List<TimeSlot> timeList)
	{
		this.context = context;
		this.text = text;
		this.timeList = timeList;
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0: // 返回的结果
					Toast.makeText(context,
							msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				case 1: // 在屏幕弹出提醒消息
					Toast.makeText(context,
							msg.getData().getString("text"), Toast.LENGTH_LONG)
							.show();
				}
			}
		};
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return text.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return text.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		
		final int index = position;
		View view = convertView;
		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.timetable_item, null);
		}
		
		TextView txtSP=(TextView)view.findViewById(R.id.text_start);
		txtSP.setTextColor(Color.BLACK);
		TextView txtEP=(TextView)view.findViewById(R.id.text_end);
		txtEP.setTextColor(Color.BLACK);
		final TextView txtStart = (TextView)view.findViewById(R.id.time_start);
		txtStart.setText((CharSequence) text.get(index).get("time_start"));
		txtStart.setTextColor(Color.BLACK);
		final TextView txtEnd = (TextView)view.findViewById(R.id.time_end);
		txtEnd.setText((CharSequence) text.get(index).get("time_end"));
		txtEnd.setTextColor(Color.BLACK);
		Button btnDelete = (Button)view.findViewById(R.id.delete);
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				text.remove(index);
				notifyDataSetChanged();
				Thread deleteThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						sendDeleteRequest(timeList.get(index).sy,timeList.get(index).sm,timeList.get(index).sd);
					}
				});
				deleteThread.start();
			}
		});
				
		return view;
	}

	private String sendDeleteRequest(int sy,int sm,int sd)
	{
		HttpPost httpRequest = new HttpPost(timetableDeleteRequestURL);

		MyApplication myApp = (MyApplication) this.context;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("guide_id", String
				.valueOf(myApp.guide_id)));
		params.add(new BasicNameValuePair("start_year", String
				.valueOf(sy)));
		params.add(new BasicNameValuePair("start_month", String
				.valueOf(sm)));
		params.add(new BasicNameValuePair("start_day", String
				.valueOf(sd)));

		try {
			// 添加请求参数到请求对象
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 发送请求并等待响应
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 处理返回的信息

				String result = EntityUtils.toString((HttpEntity) httpResponse
						.getEntity());
				print(result);
				
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