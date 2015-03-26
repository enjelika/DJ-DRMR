package edu.uco.sdd.spring15.dj_drmr;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Params;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;

import edu.uco.sdd.spring15.dj_drmr.TrackResultsFragment.TrackResultsListener;
import edu.uco.sdd.spring15.dj_drmr.record.RecordDialogFragment.RecordDialogListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;
import edu.uco.sdd.spring15.dj_drmr.record.RecordDialogFragment;
import edu.uco.sdd.spring15.dj_drmr.record.RecordMp3;
import edu.uco.sdd.spring15.dj_drmr.stream.IMediaPlayerServiceClient;
import edu.uco.sdd.spring15.dj_drmr.stream.MediaPlayerService;
import edu.uco.sdd.spring15.dj_drmr.stream.MediaPlayerService.MediaPlayerBinder;
import edu.uco.sdd.spring15.dj_drmr.stream.MusicController;
import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;
import edu.uco.sdd.spring15.dj_drmr.stream.StateMediaPlayer;

public class DjdrmrMain extends Activity implements 
NavigationDrawerFragment.NavigationDrawerCallbacks, TrackResultsListener, RecordDialogListener{


	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private static String recordPath = Environment.getExternalStorageDirectory() + "/test.mp3";
	//MP3
	private static RecordMp3 mRecordMp3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.djdrmr_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		
		//set up the drawer
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		
		switch(position) {
		default: 
		case 0: fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							WelcomeFragment.newInstance(position + 1)).commit();
				break;
		case 1: fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							BrowseFragment.newInstance(position + 1), "BrowseFragment").commit();
				break;
		case 2: fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							RecordFragment.newInstance(position + 1), "RecordFragment").commit();
				break;
		case 3: fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							UploadFragment.newInstance(position + 1)).commit();
				break;
		}
		/*fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		*/
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.djdrmr_main, menu);
		//return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id){
			case R.id.action_settings:
				return true;
			case R.id.action_logout:
				Intent i = new Intent(this, Login.class);
				startActivity(i);
			default:
				return super.onOptionsItemSelected(item);
		}
		/*
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
		*/
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class WelcomeFragment extends Fragment {
		private Button btnBrowse;
		private Button btnRecord;
		private Button btnLogin;
		private Button btnSignUp;
		private Button btnUpload;
		
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static WelcomeFragment newInstance(int sectionNumber) {
			WelcomeFragment fragment = new WelcomeFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public WelcomeFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_welcome, container,
					false);
			/*
			// Login Activity
			btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), Login.class);	
					startActivity(intent);
				}
			});
			
			// SignUp Activity
			btnSignUp = (Button) rootView.findViewById(R.id.btnSignUp);
			btnSignUp.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), Register.class);	
					startActivity(intent);
				}
			});
			
			// Browse Activity
			btnBrowse = (Button) rootView.findViewById(R.id.btnBrowse);
			btnBrowse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), BrowseActivity.class);	
					startActivity(intent);
				}
			});
			
			// Record Activity
			btnRecord = (Button) rootView.findViewById(R.id.btnRecord);
			btnRecord.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), RecordActivity.class);
					startActivity(intent);
				}
			});
			
			// Upload Activity
			btnUpload = (Button) rootView.findViewById(R.id.btnUpload);
			btnUpload.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), UploadActivity.class);	
					startActivity(intent);
				}
			});
			*/
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DjdrmrMain) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	public static class BrowseFragment extends Fragment implements IMediaPlayerServiceClient, TrackResultsListener, 
																	OnClickListener, MediaPlayerControl {
		
		private StateMediaPlayer mp = null;
		private MediaPlayerService mService;
		private SoundcloudResource resource = null;
		private ArrayList<SoundcloudResource> resourceList;
		private boolean bound;
		private Resources res;
		private String genres[];
		private ToggleButton btnPlayPause;
        private ListView lvGenres;
        private MusicController mController;
        private boolean playbackPaused;
		
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static BrowseFragment newInstance(int sectionNumber) {
			BrowseFragment fragment = new BrowseFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public BrowseFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.browse_activity, container,
					false);
			
			res = getResources();
			genres = res.getStringArray(R.array.soundcloud_genres);
			btnPlayPause = (ToggleButton) rootView.findViewById(R.id.btnPlayPause);
			lvGenres = (ListView) rootView.findViewById(R.id.genreList);
			initializeButtons();
			bindToService();
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			if (bound && mService.isPaused()) {
				setController();
			}
		}
		
		@Override
		public void onStop() {
			mController.hide();
			super.onStop();
		}

		private void bindToService() {
			Intent intent = new Intent(getActivity(), MediaPlayerService.class);
			if (MediaPlayerServiceRunning()) {
	            // Bind to LocalService
	            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	        }
	        else {
	            getActivity().startService(intent);
	            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
	            mService.setClient(BrowseFragment.this);
	            bound = true;
	 
	            // TODO: update the UI...?
		        //Set play/pause button to reflect state of the service's contained player
	            btnPlayPause.setChecked(mService.getMediaPlayer().isPlaying());	
	            // set the mediacontroller
	            setController();
	        }
	 
	        @Override
	        public void onServiceDisconnected(ComponentName arg0) {
	            bound = false;
	        }
	    };
	    
	    private void initializeButtons() {
	        // PLAY/PAUSE BUTTON
	        btnPlayPause.setOnClickListener(this);
	        
//	        setController();
	        
	        final ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getActivity(), R.layout.listitem, genres);
	        lvGenres.setAdapter(genreAdapter);

			//final ListView lvTracks = (ListView) findViewById(R.id.trackList);
	        
	        lvGenres.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String genre = (String) parent.getItemAtPosition(position);
					Log.d("BrowseFragment", "genre = " + genre);
					SoundcloudResource tracklist = new SoundcloudResource(
								"/tracks?client_id=" + SoundcloudResource.CLIENT_ID + "&genres=" + genre);
					tracklist.setType(SoundcloudResource.RESOURCE_TYPE_QUERY_RESULT); // a list of tracks
					tracklist.pullData(); // get result from soundcloud
					while (!tracklist.hasData()) { /* wait for data */ }
					String result = tracklist.getSoundcloudData();
					try {
						JSONArray json = new JSONArray(result);
						resourceList = new ArrayList<SoundcloudResource>();
						for (int i = 0; i < json.length(); i++) {
							JSONObject object = json.getJSONObject(i);
							if (object.getBoolean("streamable")) {
								// only parse streamable tracks
								String title = object.getString("title").toString();
								String artist = object.getJSONObject("user").getString("username").toString();
								String url = object.getString("stream_url").toString();
								SoundcloudResource scr = new SoundcloudResource(url);
								scr.setType(SoundcloudResource.RESOURCE_TYPE_TRACK);
								scr.setTitle(title);
								scr.setArtist(artist);
								resourceList.add(scr);
							}
						}
						// debug
//						Log.d("BrowseActivity", "resourceList == null? " + (resourceList==null)+"");
						
						//pass to fragment - Debra
						TrackResultsFragment t = new TrackResultsFragment();
						Bundle bundle = new Bundle();
						bundle.putParcelableArrayList("results", resourceList);
						t.setArguments(bundle);
						t.show(getFragmentManager(), result);
						//lvTracks.setAdapter(new ArrayAdapter<String>(BrowseActivity.this, R.layout.listitem, tracks));
					} catch (JSONException e) {
						Log.e("BrowseFragment", "JSONException when parsing soundcloud data");
						e.printStackTrace();
					}
				}
			});
	    }
		    
	    private boolean MediaPlayerServiceRunning() {
	    	 
	        ActivityManager manager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
	 
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
			btnPlayPause.setChecked(true);		
		}

		@Override
		public void onError() {
			Log.e("BrowseFragment", "onError called by service");
		}
		
		public void shutdownActivity() {
			 
	        if (bound) {
	            mService.stopMediaPlayer();
	            // Detach existing connection.
	            getActivity().unbindService(mConnection);
	            bound = false;
	         }
	 
	        Intent intent = new Intent(getActivity(), MediaPlayerService.class);
	        getActivity().stopService(intent);
	        getActivity().finish();
	 
	    }

		@Override
		public void onPickTrackClick(int trackIndex, DialogFragment dialog) {
			if (bound) {
				Log.d("BrowseActivity", "onPickTrackClick");
				resource = resourceList.get(trackIndex);
//				resource.pullData();
//				while (!resource.hasData()) { /* wait for data */ }
				mService.initializeMediaPlayer(resourceList, trackIndex);
				btnPlayPause.setChecked(true);
			}
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DjdrmrMain) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case (R.id.btnPlayPause):
				if (bound) {
	                mp = mService.getMediaPlayer();
	
	                //pressed pause ->pause
	                if (!btnPlayPause.isChecked()) {
	
	                    if (mp.isStarted()) {
	                        mService.pauseMediaPlayer();
	                    }
	
	                }
	
	                //pressed play
	                else if (btnPlayPause.isChecked()) {
	                    // STOPPED, CREATED, EMPTY, -> initialize
	                    if (mp.isStopped()
	                            || mp.isCreated()
	                            || mp.isEmpty())
	                    {
	                        mService.initializeMediaPlayer(resource);
	                    }
	
	                    //prepared, paused -> resume play
	                    else if (mp.isPrepared()
	                            || mp.isPaused())
	                    {
	                        mService.startMediaPlayer();
	                    }
	
	                }
	            }
				break;
			}
		}

		private void setController() {
			// set up MusicController
			Log.d("BrowseFragment", "setController");
			mController = new MusicController(getActivity());
			mController.setPrevNextListeners(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mService.playNext();
					if (playbackPaused) {
						setController();
						playbackPaused = false;
					}
					mController.show(0);
				}
			}, new OnClickListener() {
				@Override
				public void onClick(View v) {
					mService.playPrev();
					if (playbackPaused) {
						setController();
						playbackPaused = false;
					}
					mController.show(0);
				}
			});
			mController.setMediaPlayer(this);
			mController.setAnchorView(getView());
			mController.setEnabled(true);
			mController.show(0);
		}
		
		@Override
		public void start() {
			mService.startMediaPlayer();
		}

		@Override
		public void pause() {
			mService.pauseMediaPlayer();
			playbackPaused = true;
		}

		@Override
		public int getDuration() {
			if(mService != null && bound && mService.isPlaying()) {
			    return mService.getDur();
			}
			else return 0;
		}

		@Override
		public int getCurrentPosition() {
			if(mService!=null && bound && mService.isPlaying()) {
			    return mService.getPosn();
			}
			else return 0;
		}

		@Override
		public void seekTo(int pos) {
			mService.seek(pos);
		}

		@Override
		public boolean isPlaying() {
			if(mService!=null && bound) {
			    return mService.isPlaying();
			}
			return false;
		}

		@Override
		public int getBufferPercentage() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean canPause() {
			return true;
		}

		@Override
		public boolean canSeekBackward() {
			return true;
		}

		@Override
		public boolean canSeekForward() {
			return true;
		}
	}
	
	public static class RecordFragment extends Fragment implements RecordDialogListener{
		
		private static final String TAG = RecordFragment.class.getSimpleName();

		// Buttons
		private Button btnRecord;
		private Button btnStop;
		

		
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		public static RecordFragment newInstance(int sectionNumber) {
			RecordFragment fragment = new RecordFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public RecordFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.record_activity, container,
					false);
		    btnRecord = (Button) rootView.findViewById(R.id.btn_record);
		    btnStop = (Button) rootView.findViewById(R.id.btn_stop);
		    

		    btnRecord.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "Start button clicked");
//					mRecordMp3.start();
					DialogFragment newFragment = new RecordDialogFragment();
					newFragment.show(getFragmentManager(), "id3");
				}
			});
		    
			btnStop.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.d(TAG, "Stop");
					mRecordMp3.stop();	
				}
			});
			return rootView;	
		}

		@Override
		public void onDialogPositiveClick(DialogFragment dialog) {

		}

		@Override
		public void onDialogNegativeClick(DialogFragment dialog) {
			// Do nothing.  Just cancel. 
			
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DjdrmrMain) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
		
	}

	public static class UploadFragment extends Fragment {
		
		private Button btChoose;
		private TextView txtFileChose;
		private EditText editTextTags;
		private Button btUpload;
		private Song song;
		
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static UploadFragment newInstance(int sectionNumber) {
			UploadFragment fragment = new UploadFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public UploadFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_upload, container,
					false);
			
			this.btChoose = (Button) rootView.findViewById(R.id.btnUpload);
			this.txtFileChose = (TextView) rootView.findViewById(R.id.txtFileChose);
			this.editTextTags = (EditText) rootView.findViewById(R.id.editTextTags);
			this.btUpload = (Button) rootView.findViewById(R.id.uploadBtn);
			
			this.btUpload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					song.setTags(editTextTags.getText().toString());
	                	new Thread(new Runnable() {
		                	public void run() {
			                	try {
			                		ApiWrapper wrapper = new ApiWrapper(SoundcloudResource.CLIENT_ID, SoundcloudResource.CLIENT_SECRET,  null,  null);
									Token token = wrapper.login("renan.kub@gmail.com", "soundcloud");
									song.getSong().setReadable(true, false);
									HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
						            .add(Params.Track.TITLE, "test.mp3")
						            .add(Params.Track.TAG_LIST, "test")
						            .withFile(Params.Track.ASSET_DATA, song.getSong()));
									
			                		 } catch (IOException e) {
			     						e.printStackTrace();
			     					}
			                	}
	                	 }).start();
    					editTextTags.setText("");
    					txtFileChose.setText("Choose File");
				}
			});
			
			this.btChoose.setOnClickListener(new View.OnClickListener() {
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
			
			
			this.editTextTags.setOnFocusChangeListener(new OnFocusChangeListener() {          
			    public void onFocusChange(View v, boolean hasFocus) {
			        if(!hasFocus) {
			        	String text = editTextTags.getText().toString();
			        	text = text.replaceAll("[^a-zA-Z ]+", "");
			        	text = text.replaceAll("[ ]{2,}", " ");
			        	String[] allTags = text.split(" ");
			        	text = "";
			        	for (String tag : allTags) {
							tag = "#"+tag;
							text = text + tag + " ";
						}
			        	editTextTags.setText(text);
			        }
			    }
			});
			
			this.editTextTags.setOnEditorActionListener(new OnEditorActionListener() {
			    @Override
			    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			        if (actionId == EditorInfo.IME_ACTION_DONE) {
			           editTextTags.clearFocus();
			        }
			        return false;
			    }
			});
		
			return rootView;
		}

		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			
			if (resultCode == Activity.RESULT_OK) {  
		        Uri uri = data.getData();
		        String path = uri.getPath();
		        File input = new File(path);
		        this.song = new Song(input);
		        txtFileChose.setText(input.getName());
		    }           
		    super.onActivityResult(requestCode, resultCode, data);
		}

		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getActivity().getMenuInflater().inflate(R.menu.upload, menu);
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
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DjdrmrMain) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onPickTrackClick(int trackIndex, DialogFragment dialog) {
		BrowseFragment mBrowseFragment = (BrowseFragment) getFragmentManager()
				.findFragmentByTag("BrowseFragment");
		mBrowseFragment.onPickTrackClick(trackIndex, dialog);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		final String TAG = "TEST";
		Log.d("test", "outside ?????");
		Log.d(TAG, "Recording started");
		Dialog dialogView = dialog.getDialog();
		EditText txt_artist, txt_title, txt_album, txt_comment, txt_year;
		txt_artist = (EditText) dialogView.findViewById(R.id.record_info_artist);
		txt_title = (EditText) dialogView.findViewById(R.id.record_info_title);
		txt_album = (EditText) dialogView.findViewById(R.id.record_info_album);
		txt_comment = (EditText) dialogView.findViewById(R.id.record_info_comment);
		txt_year = (EditText) dialogView.findViewById(R.id.record_info_year);
			
		mRecordMp3 = new RecordMp3(recordPath, 
				8000, 
				8000, 
				txt_album.getText().toString(), 
				txt_title.getText().toString(), 
				txt_artist.getText().toString(), 
				txt_comment.getText().toString(), 
				txt_year.getText().toString());
		mRecordMp3.start();
	    mRecordMp3.setHandle(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case RecordMp3.MSG_REC_STARTED:
					Log.d(TAG, "Recording");
					break;
				case RecordMp3.MSG_REC_STOPPED:
					Log.d(TAG, "Recording stopped");
					break;
				case RecordMp3.MSG_ERROR_GET_MIN_BUFFERSIZE:
					Log.d(TAG, "Buffer");
					break;
				case RecordMp3.MSG_ERROR_CREATE_FILE:
					Log.d(TAG, "Error_Creating file");
					break;
				case RecordMp3.MSG_ERROR_REC_START:
					Log.d(TAG, "Can't start recording");
					break;
				case RecordMp3.MSG_ERROR_AUDIO_RECORD:
					Log.d(TAG, "Can't recording audio");
					break;
				case RecordMp3.MSG_ERROR_AUDIO_ENCODE:
					Log.d(TAG, "Can't encode");
					break;
				case RecordMp3.MSG_ERROR_WRITE_FILE:
					Log.d(TAG, "Can't write a file");
					break;
				case RecordMp3.MSG_ERROR_CLOSE_FILE:
					Log.d(TAG, "Can't close the file");
					break;
				default:
					break;
				}
			}
		});
		
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

}
