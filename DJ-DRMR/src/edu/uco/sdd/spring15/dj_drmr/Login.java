package edu.uco.sdd.spring15.dj_drmr;

import com.soundcloud.api.Token;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //Custom Toast variables
    private Toast toast;
    private TextView text;    
	
    Boolean isInternetPresent = false;
	CheckConnection cd;
	
	private boolean hasToken = false;
    
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		//Custom Toast
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, 
									   (ViewGroup) findViewById(R.id.toast_layout_root));
		
		text = (TextView) layout.findViewById(R.id.toast_txt);
		toast = new Toast(getBaseContext());
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		
		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
		
		//register listeners
		mSubmit.setOnClickListener(this);
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
				String message = "You don't have internet connection. Please connect to the internet";
				text.setText(message);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
//				Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
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
