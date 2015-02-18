package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
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
	private static final File FILE_PATH = new File(
			Environment.getExternalStorageDirectory(),
			"Android/data/edu.uco.sdd.spring15.dj_drmr");
	private static final File FILE_RECORDING = new File(FILE_PATH, "demo.mp4");
	
	
	/**
	 * (1) Handle the record/stop button
	 * (2) Handle the play/stop button
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.d(TAG, "onCreate");
	    
	    setContentView(R.layout.record_activity);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    btnRecord = (Button) findViewById(R.id.btn_record);
	    btnPlay = (Button) findViewById(R.id.btn_play);
	    btnSave = (Button) findViewById(R.id.btn_save);
	    
// 		Check the Storage availability
	    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
	    	if (!FILE_PATH.mkdir()) {
	    		Log.d(TAG, "Could not create" + FILE_PATH);
	    	} else {
	    		Log.d(TAG, "External storage is required");
	    		finish();
	    	}
	    }

	    
//		(1)	    
	    btnRecord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!isRecording) {
					// Record
					Log.d(TAG, "Recording Started");
					isRecording = true;
					mRecorder = getRecorder(FILE_RECORDING);
					mRecorder.start();
					btnRecord.setText(R.string.record_stop);	
				} else {
					// Stop
					Log.d(TAG, "Recording Stopped");
					mRecorder.stop();
					mRecorder.reset();
					mRecorder.release();
					mRecorder = null;
					isRecording = false;
					
					btnRecord.setText(R.string.record_record);
				}
			}
		});
	    
//		(2)
	    btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnPlay.setEnabled(false);
				btnRecord.setEnabled(false);
				playRecorded(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						btnPlay.setEnabled(true);
						btnRecord.setEnabled(true);
					}
				});
			}
		});
	}
	
	
	/**
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
		
		Log.i(TAG, "File path: " + path.getAbsolutePath());
		Log.i(TAG, "File Recording to " + FILE_RECORDING);
		try {
			recorder.prepare();
		} catch (IOException e) {
			Log.d(TAG, "prepare failed");
		}
		return recorder;
	}
	
	/**
	 * @param onCompletion MediaPlayer.OnCompletionListener object
	 * 
	 * Play the sound recorded through absolute-path.
	 */
	private void playRecorded(MediaPlayer.OnCompletionListener onCompletion) {
		Log.d(TAG, "playRecorded");
		
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(FILE_RECORDING.getAbsolutePath());
			player.prepare();
			player.setOnCompletionListener(onCompletion);
			player.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
