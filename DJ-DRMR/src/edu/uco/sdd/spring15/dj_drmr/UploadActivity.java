package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UploadActivity extends Activity {
	private Button btUpload;
	private TextView txtFileChose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.btUpload = (Button) findViewById(R.id.btnUpload);
		this.txtFileChose = (TextView) findViewById(R.id.txtFileChose);
		
		this.btUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
			        fileintent.setType("audio/mpeg");
			        try {
			            startActivityForResult(fileintent, 1);
			        } catch (ActivityNotFoundException e) {
			            Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
			        }
			}
		});
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	       
	    if (resultCode == Activity.RESULT_OK) {  
	        Uri uri = data.getData();
	        String path = uri.getPath();
	        File input = new File(path);
	        txtFileChose.setText(input.getName());
	    }           
	    super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
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
