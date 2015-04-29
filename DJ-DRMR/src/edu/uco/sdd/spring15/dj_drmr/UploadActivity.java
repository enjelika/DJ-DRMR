package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
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

import com.soundcloud.api.Token;

public class UploadActivity extends Activity {
	private Button btChoose;
	private TextView txtFileChose;
	private EditText editTextTags;
	private Button btUpload;
	private Song song;
	private EditText newName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.btChoose = (Button) findViewById(R.id.btnUpload);
		this.txtFileChose = (TextView) findViewById(R.id.txtFileChose);
		this.editTextTags = (EditText) findViewById(R.id.editTextTags);
		this.btUpload = (Button) findViewById(R.id.uploadBtn);
		this.newName = (EditText) findViewById(R.id.newName);
		
		this.btUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				song.setTags(editTextTags.getText().toString());
				
                try {
                	SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), 0);
    				String access = prefs.getString("access", "");
    			    String refresh = prefs.getString("refresh", "");
    			    Token token = new Token(access, refresh, "non-expiring");
    			    SongDAO.upload(token, song);
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					Toast.makeText(getApplicationContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
					newName.setText("");
					editTextTags.setText("");
					txtFileChose.setText("Choose File");
				}
			}
		});
		
		this.btChoose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("Android/data/edu.uco.sdd.spring15.dj_drmr");
				 Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
			        fileintent.setDataAndType(uri, "audio/mpeg");
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
	        this.song = new Song(input, this.newName.getText().toString());
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}