package com.example.evertour_guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {

	Button btnBack, btnSignup;
	EditText edtUsername, edtPassword, edtPasswordAgain, edtSchool,
			edtStudentId, edtRealName, edtEmail,edtIntroduction;
	String username, password, passwordAgain, school, studentId, realName,
			email,introduction;

	Handler handler;
	
	final String requestURL = UriAPI.guideSignup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		initView();
		
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch(msg.what)
				{
				case 0:
					Toast.makeText(getApplicationContext(), msg.getData().getString("result"), Toast.LENGTH_LONG).show();
				}
			}
		};
	}
	
	private void initView() {
		edtUsername = (EditText)findViewById(R.id.username);
		edtPassword = (EditText)findViewById(R.id.password);
		edtPasswordAgain = (EditText)findViewById(R.id.password_again);
		edtSchool = (EditText)findViewById(R.id.school);
		edtStudentId = (EditText)findViewById(R.id.student_id);
		edtRealName = (EditText)findViewById(R.id.real_name);
		edtEmail = (EditText)findViewById(R.id.email);
		edtIntroduction = (EditText)findViewById(R.id.introduction);
		
		btnBack = (Button) findViewById(R.id.back);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnSignup = (Button) findViewById(R.id.signup);
		btnSignup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = edtUsername.getText().toString();
				password = edtPassword.getText().toString();
				passwordAgain = edtPasswordAgain.getText().toString();
				school = edtSchool.getText().toString();
				studentId = edtStudentId.getText().toString();
				realName = edtRealName.getText().toString();
				email = edtEmail.getText().toString();
				introduction = edtIntroduction.getText().toString();

				if (password.compareTo(passwordAgain) != 0) {
					Toast.makeText(getApplicationContext(), "请输入一致的密码", Toast.LENGTH_LONG).show();
					return;
				}else if(username.length() < 1)
				{
					Toast.makeText(getApplicationContext(), "用户名的长度不能小于1", Toast.LENGTH_LONG).show();
					return;
				}
				else if(password.length() < 8)
				{
					Toast.makeText(getApplicationContext(),"密码长度不能小于8",Toast.LENGTH_LONG).show();
					return;
				}
				else if(school.length() == 0)
				{
					Toast.makeText(getApplicationContext(),"请输入学校名称",Toast.LENGTH_LONG).show();
					return;
				}
				else if(studentId.length() == 0)
				{
					Toast.makeText(getApplicationContext(),"请输入学号",Toast.LENGTH_LONG).show();
					return;
				}
				else if(realName.length() == 0)
				{
					Toast.makeText(getApplicationContext(),"请输入真实姓名",Toast.LENGTH_LONG).show();
					return;
				}
				else if(isEmail(email) == false)
				{
					Toast.makeText(getApplicationContext(),"请输入正确的email地址",Toast.LENGTH_LONG).show();
					return;
				}
				else 
				{
					Thread sendRequest = new Thread(new Runnable() {
						
						@Override
						public void run() {
							String result = sendRequestToHost();
							Message msg = new Message();
							Bundle data = new Bundle();
							data.putString("result", result);
							msg.setData(data);
							msg.what = 0;
							handler.sendMessage(msg);
							if(result.compareTo("succeed") == 0)
							{
								startActivity(new Intent(getApplicationContext(), Signin.class));
								finish();
							}
							
						}
					});
					sendRequest.start();
				}
			}

			private String sendRequestToHost() {
				HttpPost httpRequest = new HttpPost(requestURL);

				// NameValuePair实现请求参数的封装
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("school", school));
				params.add(new BasicNameValuePair("studentId", studentId));
				params.add(new BasicNameValuePair("realname", realName));
				params.add(new BasicNameValuePair("email", email));
				params.add(new BasicNameValuePair("introduction",introduction));

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
						Toast.makeText(
								getApplicationContext(),
								"Error Response: "
										+ httpResponse.getStatusLine()
												.toString(), Toast.LENGTH_LONG)
								.show();
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
		});
	}
	
	private boolean isEmail(String strEmail) {

		String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();

	}
}
