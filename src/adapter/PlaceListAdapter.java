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

import base.adapter.MyBaseAdapter;

import com.example.evertour_guide.R;

import data.structor.ProvinceIdPos;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceListAdapter extends MyBaseAdapter {

	private LayoutInflater mInflater;
	private List<String> provinceList,cityList;
	
	final String deletePlaceRequestURL = API.UriAPI.deletePlaceRequestURL;
	
	public PlaceListAdapter(final Context context,List<String> provinceList,List<String> cityList)
	{
		super(context);
		this.provinceList = provinceList;
		this.cityList = cityList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return provinceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return provinceList.get(position);
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
			view = inflater.inflate(R.layout.list_item_place_list, null);
		}
		
		TextView txtPlace=(TextView)view.findViewById(R.id.textPlace);
		txtPlace.setTextColor(Color.BLACK);
		
		txtPlace.setText(provinceList.get(index) + cityList.get(index));
		
		Button btnDelete = (Button)view.findViewById(R.id.delete);
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				provinceList.remove(index);
				final String cityToDelete = cityList.remove(index);
				notifyDataSetChanged();
				Thread deleteThread = new Thread(new Runnable() {
					
					@Override
					public void run() {

						MyApplication myApp = (MyApplication) context;

						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("guide_id", String
								.valueOf(myApp.guide_id)));
						params.add(new BasicNameValuePair("city", cityToDelete));
						sendDeleteRequest(deletePlaceRequestURL,params);
					}
				});
				deleteThread.start();
			}
		});
				
		return view;
	}

	private String sendDeleteRequest(String requestURL,List params)
	{
		
		HttpPost httpRequest = new HttpPost(requestURL);

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

}
