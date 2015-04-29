package edu.uco.sdd.spring15.dj_drmr;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.soundcloud.api.Token;

import edu.uco.sdd.spring15.dj_drmr.DjdrmrMain.WelcomeFragment;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener{
	
	private EditText user, pass;
	private Button mSubmit/*, mRegister*/;
	
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
  
    private static final String LOGIN_URL = "http://www.raybvisions.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
	
    Boolean isInternetPresent = false;
	CheckConnection cd;
	
	private boolean hasToken = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
//		mRegister = (Button)findViewById(R.id.register);
		
		//register listeners
		mSubmit.setOnClickListener(this);
//		mRegister.setOnClickListener(this);
		cd = new CheckConnection(getApplicationContext());
		
		if(getIntent().getStringExtra("user") != null){
			String rUser = getIntent().getStringExtra("user");
			String rPassword = getIntent().getStringExtra("pass");
			user.setText(rUser);
			pass.setText(rPassword);
		}
		
		// TODO: check shared prefs for token
		SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.shared_prefs), 0);
		String access = prefs.getString("access", "");
	    String refresh = prefs.getString("refresh", "");
		if (access != null && !access.isEmpty()) {
			Token token = new Token(access, refresh, "non-expiring");
			hasToken = true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login:
			isInternetPresent = cd.isConnectingToInternet();
			if(!isInternetPresent){
				Toast.makeText(getApplicationContext(), "You don't have internet connection. Please connect to the internet", Toast.LENGTH_SHORT).show();
			} else { //new AttemptLogin().execute();
				if (hasToken) {
					// start the main activity
					Intent intent2 = new Intent(this, DjdrmrMain.class);
			        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(intent2);
				} else {
					// open the sign-in dialog
					FragmentTransaction ft = getFragmentManager().beginTransaction();
			        ft.addToBackStack(null);
					OAuth2Fragment newFragment = new OAuth2Fragment();
			        newFragment.show(ft, "dialog");
				}
			}
			break;
		default:
			break;
		}
	}		 
}
