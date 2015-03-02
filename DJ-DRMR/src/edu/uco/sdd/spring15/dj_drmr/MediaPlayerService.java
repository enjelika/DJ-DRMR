package edu.uco.sdd.spring15.dj_drmr;

import edu.uco.sdd.spring15.dj_drmr.StateMediaPlayer.MPlayerStates;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import edu.uco.sdd.spring15.dj_drmr.BrowseActivity;

public class MediaPlayerService extends Service implements OnBufferingUpdateListener, OnInfoListener, 
																	OnPreparedListener, OnErrorListener {
	
	private StateMediaPlayer mp = new StateMediaPlayer();
	private IMediaPlayerServiceClient mClient;
	private final Binder mBinder = new MediaPlayerBinder();
	
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
	
	// initialize the media player given a url
	// TODO: a method to initialize given a stream station object?
	public void initializeMediaPlayer(String url) {
		mp = new StateMediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mp.setDataSource(url);
			SoundcloudResource soundcloud = new SoundcloudResource(
					"/tracks?client_id=" + SoundcloudResource.clientId + "&q=berlin&format=json&genres=techno");
			String result = soundcloud.getSoundcloudData();
		} catch (Exception e) {
			Log.e("MediaPlayerService", "error setting data source");
			mp.setState(MPlayerStates.ERROR);
		}
		mp.setOnBufferingUpdateListener(this);
		mp.setOnInfoListener(this);
		mp.setOnPreparedListener(this);
		mp.prepareAsync();
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
	
	public void startMediaPlayer() {
		Context context = getApplicationContext();
		
		//set to foreground
		// TODO: fix deprecated method calls
        Notification notification = new Notification(android.R.drawable.ic_media_play, "MediaPlayerService",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, DjdrmrMain.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
 
        CharSequence contentTitle = "MediaPlayerService Is Playing";
        CharSequence contentText = "todo";
        notification.setLatestEventInfo(context, contentTitle,
                contentText, pendingIntent);
        startForeground(1, notification);
 
        Log.d("MediaPlayerService","startMediaPlayer() called");
        mp.start();
	}
	
	public void pauseMediaPlayer() {
		Log.d("MediaPlayerService","pauseMediaPlayer() called");
        mp.pause();
        stopForeground(true);
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
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MediaPlayerService", "service started");
		return START_STICKY;
	}
	
	public void setClient(IMediaPlayerServiceClient client) {
		this.mClient = client;
	}
}
