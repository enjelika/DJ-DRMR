package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ViewFlipper;

public class SplashScreen extends Activity {

	private ViewFlipper animation;
	private static int SPLASH_TIME_OUT = 3500; //3500 = 3.5 seconds
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		
		//ViewFlipper animation frames here
		animation = (ViewFlipper) findViewById(R.id.animation);
		animation.setFlipInterval(500); //500 is 0.5 sec animation speed
		animation.startFlipping();	
		
		if(Build.VERSION.SDK_INT >= 11){
			new LoadData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}else{
			new LoadData().execute();
		}
	}
	
	private class LoadData extends AsyncTask <Void, Void, Void> {
		
		Boolean done;
		File myDirectory;
		
		@Override
		protected void onPreExecute(){
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... arg0){
			//check for Djdrmr folder
			done = false;
			
			while(!done){
				myDirectory = new File(Environment.getExternalStorageDirectory() + "/Djdrmr/");
				Log.e("SplashScreen","Looking for the Djdrmr folder");
				Log.e("SplashScreen", "Directory = " + myDirectory + " :: exists = " + myDirectory.exists());
				 
				if(!myDirectory.exists()) {
					myDirectory.mkdir();
					Log.e("SplashScreen", "Could not find the folder");
					Log.e("SplashScreen", "Created: " + myDirectory);
				}
				
				done = true;
				
				try {
					Thread.sleep(SPLASH_TIME_OUT);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			Intent i = new Intent(SplashScreen.this, Login.class);
			startActivity(i);
			finish();
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
