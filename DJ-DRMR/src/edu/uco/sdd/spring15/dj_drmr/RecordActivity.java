package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class RecordActivity extends Activity {


	private static final String TAG = RecordActivity.class.getSimpleName();

	// Buttons
	
	Button mRecord;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.record_activity);
	    
	}
	
}
