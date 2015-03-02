package edu.uco.sdd.spring15.dj_drmr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uco.sdd.spring15.dj_drmr.MediaPlayerService.MediaPlayerBinder;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class BrowseActivity extends Activity implements IMediaPlayerServiceClient 
{
	
	private StateMediaPlayer mp;
	private MediaPlayerService mService;
	private boolean bound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		initializeButtons();
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
	            //Set play/pause button to reflect state of the service's contained player
	            final ToggleButton playPauseButton = (ToggleButton) findViewById(R.id.btnPlayPause);
	            playPauseButton.setChecked(mService.getMediaPlayer().isPlaying());
 
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
    
    private void initializeButtons() {
        // PLAY/PAUSE BUTTON
    	final ToggleButton playPauseButton = (ToggleButton) findViewById(R.id.btnPlayPause);
        playPauseButton.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                if (bound) {
                    mp = mService.getMediaPlayer();
 
                    //pressed pause ->pause
                    if (!playPauseButton.isChecked()) {
 
                        if (mp.isStarted()) {
                            mService.pauseMediaPlayer();
                        }
 
                    }
 
                    //pressed play
                    else if (playPauseButton.isChecked()) {
                        // STOPPED, CREATED, EMPTY, -> initialize
                        if (mp.isStopped()
                                || mp.isCreated()
                                || mp.isEmpty())
                        {
                            mService.initializeMediaPlayer(
                            		"http://www.songspw.com/fileDownload/Songs/128/23718.mp3");
                        }
 
                        //prepared, paused -> resume play
                        else if (mp.isPrepared()
                                || mp.isPaused())
                        {
                            mService.startMediaPlayer();
                        }
 
                    }
                }
            }
 
        });
        
        ListView lvGenres = (ListView) findViewById(R.id.genreList);
        Resources res = getResources();
        String genres[] = res.getStringArray(R.array.soundcloud_genres);
        final ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, R.layout.listitem, genres);
        lvGenres.setAdapter(genreAdapter);

		final ListView lvTracks = (ListView) findViewById(R.id.trackList);
        
        lvGenres.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String genre = (String) parent.getItemAtPosition(position);
				System.out.println("genre = " + genre);
				SoundcloudResource resource = new SoundcloudResource(
							"/tracks?client_id=" + SoundcloudResource.clientId + "&genres=" + genre);
				while (!resource.hasData()) { /* wait for data */ }
				String result = resource.getSoundcloudData();
				try {
					JSONArray json = new JSONArray(result);
					String tracks[] = new String[json.length()];
					for (int i = 0; i < json.length(); i++) {
						tracks[i] = json.getJSONObject(i).getString("title").toString();
						System.out.println(tracks[i]);
					}
					lvTracks.setAdapter(new ArrayAdapter<String>(BrowseActivity.this, R.layout.listitem, tracks));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
    }
	    
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
		
	}

	@Override
	public void onInitializePlayerSuccess() {
		final ToggleButton btnPlayPause = (ToggleButton) findViewById(R.id.btnPlayPause);
		btnPlayPause.setChecked(true);		
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
