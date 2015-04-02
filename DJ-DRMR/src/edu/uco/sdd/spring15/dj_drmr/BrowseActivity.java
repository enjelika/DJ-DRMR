package edu.uco.sdd.spring15.dj_drmr;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import edu.uco.sdd.spring15.dj_drmr.R;
import edu.uco.sdd.spring15.dj_drmr.TrackResultsFragment;
import edu.uco.sdd.spring15.dj_drmr.TrackResultsFragment.TrackResultsListener;
import edu.uco.sdd.spring15.dj_drmr.stream.IMediaPlayerServiceClient;
import edu.uco.sdd.spring15.dj_drmr.stream.MediaPlayerService;
import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;
import edu.uco.sdd.spring15.dj_drmr.stream.StateMediaPlayer;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DialogFragment;
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
//import android.widget.TextView;
import android.widget.ToggleButton;

public class BrowseActivity extends Activity implements IMediaPlayerServiceClient, TrackResultsListener 
{	
	private StateMediaPlayer mp = null;
	private MediaPlayerService mService;
	private SoundcloudResource resource = null;
	private ArrayList<SoundcloudResource> resourceList;
	private boolean bound;
	private Resources res;
	private String genres[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_activity);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		res = getResources();
		genres = res.getStringArray(R.array.soundcloud_genres);
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
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) serviceBinder;
            mService = binder.getService();
 
            //send this instance to the service, so it can make callbacks on this instance as a client
            mService.setClient(BrowseActivity.this);
            bound = true;
        }
 
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
    
    private void initializeButtons() {
    	if (bound) {
    		mp = mService.getMediaPlayer();
    	}
        
        ListView lvGenres = (ListView) findViewById(R.id.genreList);
        final ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(this, R.layout.listitem, genres);
        lvGenres.setAdapter(genreAdapter);
        
        lvGenres.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String genre = (String) parent.getItemAtPosition(position);
				Log.d("BrowseActivity", "genre = " + genre + "!!!");
				SoundcloudResource tracklist = new SoundcloudResource(
							"/tracks?client_id=" + SoundcloudResource.CLIENT_ID + "&genres=" + genre);
				tracklist.setType(SoundcloudResource.RESOURCE_TYPE_QUERY_RESULT); // a list of tracks
				
				while (!tracklist.hasData()) { /* wait for data */ }
				
				String result = tracklist.getSoundcloudData();
				Log.d("BrowseActivity", "onItemClick got soundcloud track list");
				
				try {
					JSONArray json = new JSONArray(result);
					resourceList = new ArrayList<SoundcloudResource>();
					for (int i = 0; i < json.length(); i++) {
						JSONObject object = json.getJSONObject(i);
						String title = object.getString("title").toString();
						String artist = object.getJSONObject("user").getString("username").toString();
						System.out.println("got title and user");
						String url = object.getString("uri").toString();
						url = url.replace("http://api.soundcloud.com", "");
						SoundcloudResource scr = new SoundcloudResource(url);
						while (!scr.hasData()) { /* wait for data */ };
						scr.setType(SoundcloudResource.RESOURCE_TYPE_TRACK);
						scr.setTitle(title);
						scr.setArtist(artist);
						resourceList.add(scr);
					}
					// debug
					Log.d("BrowseActivity", "resourceList == null? " + (resourceList==null)+"");
					
					TrackResultsFragment t = new TrackResultsFragment();
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("results", resourceList);
					t.setArguments(bundle);
					t.show(getFragmentManager(), result);
				} catch (JSONException e) {
					Log.e("BrowseActivity", "JSONException when parsing soundcloud data");
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

	@Override
	public void onPickTrackClick(int trackIndex, DialogFragment dialog) {
		if (bound) {
			Log.d("BrowseActivity", "onPickTrackClick");
			resource = resourceList.get(trackIndex);
			mService.initializeMediaPlayer(resource);
		}
	}
}
