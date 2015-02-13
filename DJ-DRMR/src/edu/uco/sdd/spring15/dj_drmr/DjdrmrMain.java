package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DjdrmrMain extends Activity {

	private static final String TAG = DjdrmrMain.class.getSimpleName(); 
	
	private Button btnBrowse;
	private Button btnRecord;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.djdrmr_main);
		
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
		btnRecord = (Button) findViewById(R.id.btn_RecordActivity);
		btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DjdrmrMain.this, RecordActivity.class);
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
	
	// This is just test from Donghan to test if I can actually commit...
	
}
