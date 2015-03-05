package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;

public class SignUp extends Activity
{
		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			// Get the view from new_activity.xml
			setContentView(R.layout.signup);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) 
		{
			getMenuInflater().inflate(R.menu.djdrmr_main, menu);
			return true;
		}		
}