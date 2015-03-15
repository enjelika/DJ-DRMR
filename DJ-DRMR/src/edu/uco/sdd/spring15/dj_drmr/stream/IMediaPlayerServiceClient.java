package edu.uco.sdd.spring15.dj_drmr.stream;

public interface IMediaPlayerServiceClient {
	
	// player is initializing...
	public void onInitializePlayerStart(String msg);
	// player is initialized
	public void onInitializePlayerSuccess();
	// player encountered an error
	public void onError();
}
