package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ViewFlipper;

public class SplashScreen extends Activity {

	private ViewFlipper animation;
	private static int SPLASH_TIME_OUT = 5000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		//ViewFlipper animation frames here
		animation = (ViewFlipper) findViewById(R.id.animation);
		animation.setFlipInterval(500); //500 is 0.5 sec
		animation.startFlipping();		
		
		new LoadData().execute();
	}
	
	private class LoadData extends AsyncTask <Void, Void, Void> {
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0){
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run(){
					Intent i = new Intent(SplashScreen.this, Login.class);
					startActivity(i);
					finish();
				}
			}, SPLASH_TIME_OUT);
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
