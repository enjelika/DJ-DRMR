package edu.uco.sdd.spring15.dj_drmr.stream;

import java.util.ArrayList;

import android.R;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import edu.uco.sdd.spring15.dj_drmr.DjdrmrMain;
import edu.uco.sdd.spring15.dj_drmr.stream.StateMediaPlayer.MPlayerStates;


public class MediaPlayerService extends Service implements OnBufferingUpdateListener, OnInfoListener, 
																	OnPreparedListener, OnErrorListener,
																	OnCompletionListener {
	
	private StateMediaPlayer mp = new StateMediaPlayer();
	private IMediaPlayerServiceClient mClient;
	private final Binder mBinder = new MediaPlayerBinder();
	private ArrayList<SoundcloudResource> resourceList = null;
	private int trackIndex = -1;
	
	private boolean paused=false;
	
	public class MediaPlayerBinder extends Binder {
		// returns the instance of this service for clients to connect and make calls to it
		public MediaPlayerService getService() {
			return MediaPlayerService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public StateMediaPlayer getMediaPlayer() {
		return mp;
	}
	
	// initialize the media player given a soundcloud resource object
	// TODO: use the resource data (title, author) to populate the media player ui component?
	public void initializeMediaPlayer(SoundcloudResource resource) {
		Log.d("MediaPlayerService", "initializeMediaPlayer with soundcloud resource");
		String url = resource.getResourceUrl();
		initializeMediaPlayer(url);
	}
	
	// initialize the media player given a list of soundcloud resource objects
	// - when one track finishes, proceed to play the next one
	public void initializeMediaPlayer(ArrayList<SoundcloudResource> resourceList, int trackIndex) {
		Log.d("MediaPlayerService", "initializeMediaPlayer with resource list");
		this.resourceList = resourceList;
		this.trackIndex = trackIndex;
		getNextTrack(trackIndex);
	}
	
	// initialize the media player given a url
	public void initializeMediaPlayer(String url) {
		url = url.replace("https", "http");
		url += "?client_id=" + SoundcloudResource.CLIENT_ID;
		Log.d("MediaPlayerService", "initializeMediaPlayer with " + url);
		if (mp.isPlaying()) mp.stop();
		mp = new StateMediaPlayer();
		mp.setAudioStreamType(MODE_WORLD_READABLE);
		mp.setOnCompletionListener(this);
		
		try {
			mp.setDataSource(url);
		} catch (Exception e) {
			Log.e("MediaPlayerService", "error setting data source");
			mp.setState(MPlayerStates.ERROR);
		}
		mp.setOnBufferingUpdateListener(this);
		mp.setOnInfoListener(this);
		mp.setOnPreparedListener(this);
		mp.prepareAsync();
	}
	
	// initialize the media player with the track at index in the resourceList
	private void getNextTrack(int index) {
		Log.d("MediaPlayerService", "getNextTrack");
		SoundcloudResource resource = resourceList.get(index);
		resource.pullData();
		while (!resource.hasData()) { /* wait for data */ }
		String url = resource.getResourceUrl();
		initializeMediaPlayer(url);
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		if (resourceList == null) {
			// only one resource was passed in - shut down mediaplayer
			mp.release();
		} else {
			// play the next track in the resourceList
			mp.reset(); // TODO: ok?
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		mClient.onError();
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mClient.onInitializePlayerSuccess();
		startMediaPlayer();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		return false;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// not currently used
	}
	
	@SuppressWarnings("deprecation")
	public void startMediaPlayer() {
		
		//set to foreground
        Intent notificationIntent = new Intent(this, DjdrmrMain.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(pendingIntent)
        		.setSmallIcon(R.drawable.btn_radio)
        		.setTicker(resourceList.get(trackIndex).getTitle())
        		.setOngoing(true)
        		.setContentTitle("playing")
        		.setContentText(resourceList.get(trackIndex).getTitle());
        Notification notification = builder.build();
 
        // TODO: make notification into a controller
        startForeground(1, notification);
 
        Log.d("MediaPlayerService","startMediaPlayer() called");
        mp.start();
	}
	
	public void pauseMediaPlayer() {
		Log.d("MediaPlayerService","pauseMediaPlayer() called");
        mp.pause();
        stopForeground(true);
        paused=true;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void stopMediaPlayer() {
		stopForeground(true);
        mp.stop();
        mp.release();
	}
	
	public void resetMediaPlayer() {
		stopForeground(true);
        mp.reset();
	}
	
	public int getPosn(){
	  return mp.getCurrentPosition();
	}
	 
	public int getDur(){
	  return mp.getDuration();
	}
	 
	public boolean isPlaying(){
	  return mp.isPlaying();
	}
	 
	public void seek(int posn){
	  mp.seekTo(posn);
	}
	
	public void playPrev() {
		if (trackIndex-- < 0) trackIndex = resourceList.size()-1;
		getNextTrack(trackIndex);
	}
	
	public void playNext() {
		if (trackIndex++ >= resourceList.size()) trackIndex = 0;
		getNextTrack(trackIndex);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MediaPlayerService", "service started");
		return START_STICKY;
	}
	
	public void setClient(IMediaPlayerServiceClient client) {
		this.mClient = client;
	}
	
	@Override
	public void onDestroy() {
		mp.stop();
		mp.release();
		stopForeground(true);
		super.onDestroy();
	}
}
