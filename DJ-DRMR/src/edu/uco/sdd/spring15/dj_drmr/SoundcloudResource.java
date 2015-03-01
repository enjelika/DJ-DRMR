package edu.uco.sdd.spring15.dj_drmr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import android.os.AsyncTask;
import android.util.Log;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Http;
import com.soundcloud.api.Request;

public class SoundcloudResource {

	// static soundcloud values
	public static final String soundcloudUrl = "http://api.soundcloud.com";
	public static final String clientId = "0724aaed3681642b2c352cea90e79297";
	public static final String clientSecret = "b18ec752d35fce1a99169a2120698bad";
	
	// static resource type values
	public static int RESOURCE_TYPE_USER = 1;
	public static int RESOURCE_TYPE_TRACK = 2;
	public static int RESOURCE_TYPE_PLAYLIST = 3;
	public static int RESOURCE_TYPE_GROUP = 4;
	public static int RESOURCE_TYPE_COMMENT = 5;
	
	// instance vars for the individual resource
	private int type;
	private String resourceUrl;
	private ApiWrapper wrapper;
	
	private String soundcloudData;
	
	public SoundcloudResource() {
		this(null);
	}
	
	public SoundcloudResource(String url) {
		wrapper = new ApiWrapper(clientId, clientSecret, null, null);
		this.resourceUrl = url;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getResourceUrl() {
		return this.resourceUrl;
	}
	
	// appends the given end of URL to the stored soundcloud URL
	// @param endOfUrl format = "/tracks?client_id={YOURCLIENTID}&q=berlin&format=json&genres=techno"
	// 								to search for techno tracks with keyword "berlin" in json format
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	
	public String getSoundcloudData() {
//		StringBuilder sb = new StringBuilder();
//		try {
//			HttpResponse response = wrapper.get(Request.to(resourceUrl));
//			// error is happening in line above
//			/* below is from soundcloud api example */
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				System.out.println(Http.formatJSON(Http.getString(response)));
//			} else {
//				System.out.println("invalid status received");
//			}
			/* below is from stackoverflow example */
//			HttpEntity entity = response.getEntity();
//			InputStream is = entity.getContent();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			String line = null;
//			try {
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//			} catch (IOException e) {
//				Log.e("SoundcloudResource", "getSoundcloudData couldn't read response contents");
//			}
//		} catch (Exception e) {
//			Log.e("SoundcloudResource", "getSoundcloudData couldn't get httpresponse");
//			e.printStackTrace();
//		}
//		return sb.toString();
		new AsyncCaller().execute();
		return "";
	}

	private class AsyncCaller extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				wrapper.login("melicentking@gmail.com", "DjDrmrIsAwesom3!@");
			} catch (Exception e) {
				Log.e("SoundcloudResource", "login error");
				e.printStackTrace();
			}
			try {
				HttpResponse response = wrapper.get(Request.to(resourceUrl));
				// error is happening in line above
				/* below is from soundcloud api example */
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					System.out.println(Http.formatJSON(Http.getString(response)));
				} else {
					System.out.println("invalid status received: " + response.getStatusLine().getStatusCode());
				}
			} catch (Exception e) {
				Log.e("SoundcloudResource", "doInBackground couldn't get httpresponse");
			}
			return null;
		}
	}
}