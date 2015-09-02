package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.Toast;

public class AddTimeslot extends Activity {
	
	Button btnBack,btnFinish;
	Button btnStart,btnEnd;
	CalendarView calendarView;

	Handler handler;
	
	Button currentButton;
	int sy,sm,sd,ey,em,ed;	//开始年份，开始月份，开始日，结束年份，结束月份，结束日
	boolean bStart = false , bEnd = false;	//是否已经输入开始年份，结束年份
	
	final String addTimeslotURL = "http://evertour.sinaapp.com/guide_add_timeslot.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_timeslot);
		
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
		
		btnFinish= (Button)findViewById(R.id.finish);
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(bStart == false)
				{
					Toast.makeText(getApplicationContext(), "请选择开始日期！", Toast.LENGTH_LONG).show();
					return;
				}
				else if(bEnd == false)
				{
					Toast.makeText(getApplicationContext(), "请选择结束日期！", Toast.LENGTH_LONG).show();
					return;
				}
				else if(sy  >ey || sm > em || sd > ed)
				{
					Toast.makeText(getApplicationContext(), "开始日期不能晚于结束日期！", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					/*Bundle data = new Bundle();
					data.putInt("sy", sy);
					data.putInt("sm", sm);
					data.putInt("sd", sd);
					data.putInt("ey", ey);
					data.putInt("em", em);
					data.putInt("ed", ed);
					intent.putExtras(data);*/
					Intent intent = new Intent(getApplicationContext(),Timetable.class);
					Thread addThread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							print(sendRequestToHost());
						}
					});
					addThread.start();
					startActivity(intent);
					finish();
				}
			}
		});
		
		btnStart = (Button)findViewById(R.id.start);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentButton = btnStart;
				Toast.makeText(getApplicationContext(), "请在日历中选择开始日期", Toast.LENGTH_LONG).show();
			}
		});
		
		btnEnd = (Button)findViewById(R.id.end);
		btnEnd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentButton = btnEnd;
				Toast.makeText(getApplicationContext(), "请在日历中选择结束日期", Toast.LENGTH_LONG).show();
			}
		});
		
		currentButton = btnStart;
		
		calendarView = (CalendarView)findViewById(R.id.calendar_view);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				Calendar calendar = new GregorianCalendar();
				month++;
				if(currentButton == btnStart)
				{
					bStart = true;
					sy = year;
					sm = month;
					sd = dayOfMonth;
				}
				else
				{
					bEnd = true;
					ey = year;
					em = month;
					ed = dayOfMonth;
				}
				currentButton.setText(year + "年" + month + "月" + dayOfMonth + "日");
			}
		});
		
	}
	
	private String sendRequestToHost() {
		HttpPost httpRequest = new HttpPost(addTimeslotURL);
		
		MyApplication myApp = (MyApplication)getApplicationContext();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("guide_id",String.valueOf(myApp.guide_id)));
		params.add(new BasicNameValuePair("start_year",String.valueOf(sy)));
		params.add(new BasicNameValuePair("start_month",String.valueOf(sm)));
		params.add(new BasicNameValuePair("start_day",String.valueOf(sd)));
		params.add(new BasicNameValuePair("end_year",String.valueOf(ey)));
		params.add(new BasicNameValuePair("end_month",String.valueOf(em)));
		params.add(new BasicNameValuePair("end_day",String.valueOf(ed)));
		
		print("开始时间:" + sy + "年" + sm + "月" + sd + "日");
		print("结束时间:" + ey + "年" + em + "月" + ed + "日");
		
		try {
			// 添加请求参数到请求对象
			httpRequest.setEntity(new UrlEncodedFormEntity(params,
					HTTP.UTF_8));

			// 发送请求并等待响应
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 处理返回的信息

				return EntityUtils.toString((HttpEntity) httpResponse
						.getEntity());

			} else {
				Log.e("MainScreen:Http Post", "Error Response: "
						+ httpResponse.getStatusLine().toString());
				print(
						"Error Response: "
								+ httpResponse.getStatusLine()
										.toString());
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
