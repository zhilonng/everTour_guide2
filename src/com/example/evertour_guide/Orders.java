package com.example.evertour_guide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import API.UriAPI;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Orders extends Activity {

	ListView orderList;
	Handler handler;

	final String orderRequestURL = UriAPI.orderRequestURL;
	protected List<NameValuePair> orderForm = new ArrayList<NameValuePair>();
	ArrayList<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orders);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:

					break;
					
					//服务器列表读取完成
				case 1:
					setList();
				}
			}
		};

		initView();
	}

	private void initView() {
		orderList = (ListView) findViewById(R.id.orders_list);

		Thread httpThread = new Thread(new Runnable() {
			@Override
			public void run() {
				orderForm = httpOrderRequest(0, 19);
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		httpThread.start();
		
		
	}

	private void setList()
	{
		int length = orderForm.size();
		for(int i = 0; i < length; i++)
		{
			Map<String,Object> item = new HashMap<String,Object>();
			item.put("title",orderForm.get(i).getName());
			item.put("text", orderForm.get(i).getValue());
			listData.add(item);
		}
		SimpleAdapter adapter = new SimpleAdapter(this,listData,android.R.layout.simple_list_item_2,new String[]{"title","text"},new int[]{android.R.id.text1,android.R.id.text2});
		orderList.setAdapter(adapter);
	}
	
	private List httpOrderRequest(int start, int end) 
	{
		HttpPost httpPost = new HttpPost(orderRequestURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("start", String.valueOf(start)));
		params.add(new BasicNameValuePair("end", String.valueOf(end)));

		try 
		{
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return null;//EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
