package edu.uco.sdd.spring15.dj_drmr.stream;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;

//import org.apache.http.HttpEntity;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;
import com.soundcloud.api.examples.CreateWrapper;

import edu.uco.sdd.spring15.dj_drmr.DjdrmrMain;
import edu.uco.sdd.spring15.dj_drmr.OAuth2Fragment;

public class SoundcloudResource implements Parcelable {

	// static soundcloud values
	public static final String SOUNDCLOUD_URL = "http://api.soundcloud.com";
	public static final String CLIENT_ID = "0724aaed3681642b2c352cea90e79297";
	public static final String CLIENT_SECRET = "b18ec752d35fce1a99169a2120698bad";
	public static final String REDIRECT_URI_STRING = "djdrmr://soundcloud/callback";
	
	// static resource type values
	public static int RESOURCE_TYPE_USER = 1;
	public static int RESOURCE_TYPE_TRACK = 2;
	public static int RESOURCE_TYPE_PLAYLIST = 3;
	public static int RESOURCE_TYPE_GROUP = 4;
	public static int RESOURCE_TYPE_COMMENT = 5;
	public static int RESOURCE_TYPE_QUERY_RESULT = 6;
	
	// the Soundcloud wrapper (for HTTP talking to Soundcloud)
//	private File wrapperFile = new File(Environment.getExternalStorageDirectory(), "wrapper.ser");
	
	// instance vars for the individual resource
	private int type;
	private String resourceUrl;
	private String title;
	private String artist;
	
	private String soundcloudData = "none";
	private boolean hasData;
	
	private Token token;
	
	public SoundcloudResource() {
		this(null, null, null);
	}
	
	public SoundcloudResource(String url, String tokenAccess, String tokenRefresh) {
		this.resourceUrl = url;
		hasData = false;
		this.token = new Token(tokenAccess, tokenRefresh, "non-expiring");
		Log.d("SoundcloudResource", "access = " + token.access + ", refresh = " + token.refresh);
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getResourceUrl() {
		return this.resourceUrl;
	}
	
	// appends the given end of URL to the stored soundcloud URL
	// resourceUrl format example: "/tracks?client_id={YOURCLIENTID}&q=berlin&format=json&genres=techno"
	// 								to search for techno tracks with keyword "berlin" in json format
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	
	public String getSoundcloudData() {
		return this.soundcloudData;
	}
	
	public void pullData() {
		new AsyncCaller().execute();
	}
	
	public boolean hasData() {
		return this.hasData;
	}

	private class AsyncCaller extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			hasData = false;
			ApiWrapper wrapper;
			try {
				URI redirect = null;
				try {
					redirect = new URI(REDIRECT_URI_STRING);
				} catch (URISyntaxException e) {
					// invalid URI format
					e.printStackTrace();
				}
			    wrapper = new ApiWrapper(CLIENT_ID, CLIENT_SECRET, redirect, token);
				HttpResponse response = wrapper.get(Request.to(resourceUrl));
				/* below is from soundcloud api example */
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ||
						response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY /* 302 -expected */) {
					soundcloudData = Http.formatJSON(Http.getString(response));
//					System.out.println(soundcloudData);
					hasData = true;
				} else {
					Log.e("SoundcloudResource", "invalid status received: " + response.getStatusLine().getStatusCode());
				}
			} catch (Exception e) {
				Log.e("SoundcloudResource", "doInBackground couldn't get httpresponse");
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
