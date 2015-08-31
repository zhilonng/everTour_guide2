package com.example.evertour_guide;

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

import API.UriAPI;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signin extends Activity {

	Button btnSignin, btnBack;
	EditText edtUsername, edtPassword;

	String username, password;

	Handler handler;

	final String requestURL = UriAPI.guideSignin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signin);

		initView();

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(getApplicationContext(),
							msg.getData().getString("result"),
							Toast.LENGTH_LONG).show();
				}
			}
		};
	}

	private void initView() {
		edtUsername = (EditText) findViewById(R.id.username);
		edtPassword = (EditText) findViewById(R.id.password);

		btnBack = (Button) findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		btnSignin = (Button) findViewById(R.id.signin);
		btnSignin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				username = edtUsername.getText().toString();
				password = edtPassword.getText().toString();

				Thread httpThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						String result = sendHttpRequest(username,password);
						Message msg = new Message();
						Bundle data = new Bundle();
						data.putString("result",result);
						msg.setData(data);
						msg.what = 0;
						handler.sendMessage(msg);
					}
				});
				httpThread.start();
			}
		});
	}

	private String sendHttpRequest(String username, String password) {

		String result = null;
		
		HttpPost httpPost = new HttpPost(requestURL);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result =  EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
			else
			{
				result = "internet access failed";
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
