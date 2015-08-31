package com.example.evertour_guide;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;

public class Orders extends Activity {

	ListView orderList;
	Handler handler;

	final String orderRequestURL = UriAPI.orderRequestURL;

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
				String form = httpOrderRequest(0, 19);
			}
		});
	}

	private String httpOrderRequest(int start, int end) 
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
				return EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
