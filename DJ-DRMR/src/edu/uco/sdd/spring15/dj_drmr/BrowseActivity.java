package edu.uco.sdd.spring15.dj_drmr;

import edu.uco.sdd.spring15.dj_drmr.MediaPlayerService.MediaPlayerBinder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class BrowseActivity extends Activity implements IMediaPlayerServiceClient 
{
	
	private StateMediaPlayer mp;
	private MediaPlayerService mService;
	private boolean bound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		bindToService();
	}
	
	private void bindToService() {
		Intent intent = new Intent(this, MediaPlayerService.class);
		if (MediaPlayerServiceRunning()) {
            // Bind to LocalService
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        else {
            startService(intent);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
	}
	
	 private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            Log.d("MainActivity","service connected");
 
            //bound with Service. get Service instance
            MediaPlayerBinder binder = (MediaPlayerBinder) serviceBinder;
            mService = binder.getService();
 
            //send this instance to the service, so it can make callbacks on this instance as a client
            mService.setClient(BrowseActivity.this);
            bound = true;
 
            // TODO: update the UI...?
//	            //Set play/pause button to reflect state of the service's contained player
//	            final ToggleButton playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
//	            playPauseButton.setChecked(mService.getMediaPlayer().isPlaying());
 
//	            //Set station Picker to show currently set stream station
//	            Spinner stationPicker = (Spinner) findViewById(R.id.stationPicker);
//	            if(mService.getMediaPlayer() != null && mService.getMediaPlayer().getStreamStation() != null) {
//	                for (int i = 0; i < CONSTANTS.STATIONS.length; i++) {
//	                    if (mService.getMediaPlayer().getStreamStation().equals(CONSTANTS.STATIONS[i])) {
//	                        stationPicker.setSelection(i);
//	                        mSelectedStream = (StreamStation) stationPicker.getItemAtPosition(i);
//	                    }
//	 
//	                }
//	            }
 
        }
 
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
	    
    private boolean MediaPlayerServiceRunning() {
    	 
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
 
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("edu.uco.sdd.spring15.dj_drmr.MediaPlayerService".equals(service.service.getClassName())) {
                return true;
            }
        }
 
        return false;
    }

	@Override
	public void onInitializePlayerStart(String msg) {
		// TODO: UI stuff
	}

	@Override
	public void onInitializePlayerSuccess() {
		// TODO UI stuff
		
	}

	@Override
	public void onError() {
		// TODO UI stuff
		
	}
	
	public void shutdownActivity() {
		 
        if (bound) {
            mService.stopMediaPlayer();
            // Detach existing connection.
            unbindService(mConnection);
            bound = false;
         }
 
        Intent intent = new Intent(this, MediaPlayerService.class);
        stopService(intent);
        finish();
 
    }
}
