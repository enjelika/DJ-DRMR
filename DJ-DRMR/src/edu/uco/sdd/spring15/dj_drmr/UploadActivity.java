package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Params;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;

import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;

public class UploadActivity extends Activity {
	private Button btChoose;
	private TextView txtFileChose;
	private EditText editTextTags;
	private Button btUpload;
	private Song song;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.btChoose = (Button) findViewById(R.id.btnUpload);
		this.txtFileChose = (TextView) findViewById(R.id.txtFileChose);
		this.editTextTags = (EditText) findViewById(R.id.editTextTags);
		this.btUpload = (Button) findViewById(R.id.uploadBtn);
		
		this.btUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				song.setTags(editTextTags.getText().toString());
				
                try {
                	ApiWrapper wrapper = new ApiWrapper(SoundcloudResource.CLIENT_ID, SoundcloudResource.CLIENT_SECRET,  null,  null);
					Token token = wrapper.login("renan.kub@gmail.com", "soundcloud");
					song.getSong().setReadable(true, false);
					HttpResponse resp = wrapper.post(Request.to(Endpoints.TRACKS)
		            .add(Params.Track.TITLE, "test.mp3")
		            .add(Params.Track.TAG_LIST, "test")
		            .withFile(Params.Track.ASSET_DATA, song.getSong()));
					
					Toast.makeText(getApplicationContext(), "Uploading", Toast.LENGTH_SHORT).show();
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					editTextTags.setText("");
					txtFileChose.setText("Choose File");
				}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.upload, menu);
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
}
