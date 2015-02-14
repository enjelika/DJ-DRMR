package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DjdrmrMain extends Activity {

	//private static final String TAG = DjdrmrMain.class.getSimpleName(); 
	
	private Button btnBrowse;
	private Button btnRecord;
	private Button btnLogin;
	private Button btnSignUp;
	private Button btnUpload;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.djdrmr_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Login Activity
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, Login.class);	
				startActivity(intent);
			}
		});
				
		// SignUp Activity
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, SignUp.class);	
				startActivity(intent);
			}
		});
				
		// Browse Activity
		btnBrowse = (Button) findViewById(R.id.btnBrowse);
		btnBrowse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, BrowseActivity.class);	
				startActivity(intent);
			}
		});
		
		// Record Activity
		btnRecord = (Button) findViewById(R.id.btnRecord);
		btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, RecordActivity.class);
				startActivity(intent);
			}
		});
		
		// Upload Activity
		btnUpload = (Button) findViewById(R.id.btnUpload);
		btnUpload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, UploadActivity.class);	
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.djdrmr_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
