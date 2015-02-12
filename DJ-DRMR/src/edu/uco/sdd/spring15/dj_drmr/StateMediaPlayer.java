package edu.uco.sdd.spring15.dj_drmr;

import android.media.MediaPlayer;
import android.util.Log;

public class StateMediaPlayer extends MediaPlayer 
{

	public enum MPlayerStates {
		EMPTY,
		CREATED,
		PREPARED,
		STARTED,
		PAUSED,
		STOPPED,
		ERROR
	}
	
	private MPlayerStates mState;
	
	public StateMediaPlayer() {
		super();
		setState(MPlayerStates.CREATED);
	}
	
	public StateMediaPlayer(String dataSourceUrl) {
		super();
		try {
			setDataSource(dataSourceUrl);
			setState(MPlayerStates.CREATED);
		} catch (Exception e) {
			Log.e("StateMediaPlayer", "setDataSource failed");
			setState(MPlayerStates.ERROR);
		}
	}
	
	// reference to a streamStation or whatever
	// TODO: for now i will just set the datasource directly
	// however in the future this should be done through a class
//	public void setDataSource(StreamStation orWhatever...) {
//		try {
//			setDataSource(url);
//			setState(MPlayerStates.CREATED);
//		} catch (Exception e) {
//			Log.e("StateMediaPlayer", "setDataSource failed");
//			setState(MPlayerStates.ERROR);
//		}
//	}
	
	// TODO: will also need to be changed to a class
	public void setState(MPlayerStates state) {
		this.mState = state;
	}
	
	@Override
	public void reset() {
		super.reset();
		setState(MPlayerStates.EMPTY);
	}
	
	@Override
	public void start() {
		super.start();
		setState(MPlayerStates.STARTED);
	}
	
	@Override
	public void pause() {
		super.pause();
		setState(MPlayerStates.PAUSED);
	}
	
	@Override
	public void stop() {
		super.stop();
		setState(MPlayerStates.STOPPED);
	}
	
	@Override
	public void release() {
		super.release();
		setState(MPlayerStates.EMPTY);
	}
	
	public boolean isCreated() {
		return (mState == MPlayerStates.CREATED);
	}
	
	public boolean isStarted() {
		return (mState == MPlayerStates.STARTED || this.isPlaying());
	}
	
	public boolean isStopped() {
		return (mState == MPlayerStates.STOPPED);
	}
	
	public boolean isPaused() {
		return (mState == MPlayerStates.PAUSED);
	}
	
	public boolean isPrepared() {
		return (mState == MPlayerStates.PREPARED);
	}
	
	public boolean isEmpty() {
		return (mState == MPlayerStates.EMPTY);
	}
}
