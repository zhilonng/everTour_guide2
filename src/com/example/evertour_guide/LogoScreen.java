package com.example.evertour_guide;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;

public class LogoScreen extends Activity {

	private long m_dwSplashTime = 3000; // �������ڱ���ʱ��
	private boolean m_bPaused = false; // �Ƿ���ͣ��ʾ��������
	private boolean m_bSplashActive = true; // ���øñ���������������

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_logo_screen);
        
        Thread splashTimer = new Thread()
        {
        	@Override
        	public void run()
        	{
        		try
        		{
        			long ms = 0;
        			while(m_bSplashActive && ms < m_dwSplashTime)
        			{
        				sleep(100);
        				if(!m_bPaused)
        				{
        					ms += 100;
        				}
        			}
        			startActivity(new Intent(getApplicationContext(),NotSignedIn.class));
        		}
        		catch(Exception ex)
        		{
        			Log.e("Splash",ex.getMessage());
        		}
        		finally
        		{
        			finish();
        		}
        	}
        };
        splashTimer.start();
    }

	@Override
	protected void onPause()
	{
		super.onPause();
		m_bPaused = true;	//ֹͣ��������ļ�ʱ
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		m_bPaused = false;	//������������ļ�ʱ
	}
	
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event)
	{
		super.onKeyDown(keyCode, event);
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_MENU:
			m_bSplashActive = false;
			break;
		case KeyEvent.KEYCODE_BACK:
			android.os.Process.killProcess(android.os.Process.myPid());
			/*����System.exit(0);*/
			break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo_screen, menu);
		return true;
	}
    
}
