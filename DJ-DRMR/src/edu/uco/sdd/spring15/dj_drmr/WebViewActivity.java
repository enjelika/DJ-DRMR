package edu.uco.sdd.spring15.dj_drmr;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.util.EncodingUtils;

import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Token;

import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
 
public class WebViewActivity extends Activity {
 
	private WebView webView;
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		String username, password;
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		
		//Register register = new Register();
		
		//username = getIntent().getStringExtra("user");
		//password = getIntent().getStringExtra("pass");
		//System.out.println(username);
		//Toast.makeText(getBaseContext(), username, Toast.LENGTH_SHORT);
		/*String url = "https://soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
				"//soundcloud.com/soundcloud-callback.html&highlight=signup";
		
		
		
		 /*String postData = "username="+username+"&password="+password;
	            webView.postUrl(url,EncodingUtils.getBytes(postData, "UTF-8"));
	           */
	    
		webView.loadUrl("https://soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
				"//soundcloud.com/soundcloud-callback.html&highlight=signup");
		
		
		URI redirect = null;
		try {
			redirect = new URI(SoundcloudResource.REDIRECT_URI_STRING);
		} catch (URISyntaxException e) {
			// invalid URI format
			e.printStackTrace();
		}
		final ApiWrapper wrapper = new ApiWrapper(SoundcloudResource.CLIENT_ID, SoundcloudResource.CLIENT_SECRET, redirect, null);
		webView.setWebViewClient(new WebViewClient(){
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	            String url2="https://www.soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
//				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
//				"//soundcloud.com/soundcloud-callback.html&highlight=signup";
//	             // all links  with in your site will be open inside the webview 
//	             //links that start your domain example(http://www.example.com/)
//	            if (url != null && url.startsWith(url2)){
//	                return false;
//	                } 
//	           // all links that points outside the site will be open in a normal android browser
//	          else  {
//	                view.getContext().startActivity(
//	                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//	                return true;
//	                }
	        	
	        	Log.d("WebViewActivity", "shouldOverrideUrlLoading");
	        	if (url.contains(SoundcloudResource.REDIRECT_URI_STRING)) {
	        		Uri result = Uri.parse(url);
                    String error = result.getQueryParameter("error");
                    final String code = result.getQueryParameter("code");
                    if(error == null && code != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Token token = wrapper.authorizationCode(code, Token.SCOPE_NON_EXPIRING);
                                    Log.d("WebViewActivity", "got token");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }}).start();
                    }
	        	}
	        	return true;
	        }
	    });
 
	}
 
}
