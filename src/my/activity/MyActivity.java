package my.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONObject;

import time.TimeSlot;
import my.application.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyActivity extends Activity {

	Handler printHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication myApp = (MyApplication) getApplicationContext();
		myApp.activityTable.put(this.getLocalClassName(), this);

		initHandler();
	}

	@Override
	public void finish() {
		MyApplication myApp = (MyApplication) getApplicationContext();
		if (myApp.activityTable.containsKey(this.getLocalClassName())) {
			myApp.activityTable.remove(this.getLocalClassName());
		}
		super.finish();
	}

	private void initHandler() {
		printHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0: // 返回的结果
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				case 1: // 在屏幕弹出提醒消息
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("text"), Toast.LENGTH_LONG)
							.show();
				}
			}
		};
	}

	protected void print(String text) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putString("text", text);
		msg.setData(data);
		msg.what = 1;
		printHandler.sendMessage(msg);
	}

	protected HttpEntity sendRequestToHost(String requestURL,
			List<NameValuePair> params) {
		HttpPost httpRequest = new HttpPost(requestURL);

		MyApplication myApp = (MyApplication) getApplicationContext();

		try {
			// 添加请求参数到请求对象
			if (params != null) {
				httpRequest.setEntity(new UrlEncodedFormEntity(params,
						HTTP.UTF_8));
			}
			
			// 发送请求并等待响应
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 处理返回的信息

				return httpResponse.getEntity();

			} else {
				Log.e("MainScreen:Http Post", "Error Response: "
						+ httpResponse.getStatusLine().toString());
				print("Error Response: "
						+ httpResponse.getStatusLine().toString());
				return null;
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
		return null;

	}
	
	@Override
	public MyApplication getApplicationContext() {
		return (MyApplication)super.getApplicationContext();
	}
}
