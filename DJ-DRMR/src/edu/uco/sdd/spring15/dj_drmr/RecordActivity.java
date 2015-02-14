package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RecordActivity extends Activity {


	private static final String TAG = RecordActivity.class.getSimpleName();

	// Buttons
	private Button btnRecord;
	private Button btnPlay;
	private Button btnSave;
	
	private boolean isRecording;
	private MediaRecorder mRecorder;
	
	// Constant values for MediaRecorder
	private static final int SAMPLEING_RATE = 44100;
	private static final int BIT_RATE = 96000;
	
	// Hard directory to save file to
	private static final File SAVE_TO = new File(
			Environment.getExternalStorageDirectory(),
			"files");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.record_activity);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    btnRecord = (Button) findViewById(R.id.btn_record);
	    btnPlay = (Button) findViewById(R.id.btn_play);
	    btnSave = (Button) findViewById(R.id.btn_save);
	    
	    btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getRecorder(SAVE_TO);
				
			}
		});
	}
	
	
	/**
	 * 
	 * @param path A path where file is saved.
	 * @return Returns MediaRecorder object.
	 * 
	 * This create MediaRecorder object and 
	 * set the default value of audio settings.
	 * 
	 */
	private MediaRecorder getRecorder(File path) {
		Log.d(TAG, "getRecorder");
		MediaRecorder recorder = new MediaRecorder();
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setAudioSamplingRate(SAMPLEING_RATE);
		recorder.setAudioEncodingBitRate(BIT_RATE);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setOutputFile(path.getAbsolutePath());
		
		try {
			recorder.prepare();
		} catch (IOException e) {
			Log.d(TAG, "prepare failed");
		}
		return recorder;
	}
}