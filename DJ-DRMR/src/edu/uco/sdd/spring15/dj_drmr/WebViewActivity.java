package edu.uco.sdd.spring15.dj_drmr;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
 
public class WebViewActivity extends Activity {
 
	private WebView webView;
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		String username, password;
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		
		//Register register = new Register();
		
		username = getIntent().getStringExtra("user");
		password = getIntent().getStringExtra("pass");
		//System.out.println(username);
		//Toast.makeText(getBaseContext(), username, Toast.LENGTH_SHORT);
		String url = "https://soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
				"//soundcloud.com/soundcloud-callback.html&highlight=signup";
		
		
		
		 String postData = "username="+username+"&password="+password;
	            webView.postUrl(url,EncodingUtils.getBytes(postData, "UTF-8"));
	            
	    Intent i = new Intent(this, Login.class);
	    i.putExtra("user", getIntent().getStringExtra("user"));
		i.putExtra("pass", getIntent().getStringExtra("pass"));
	    startActivity(i);
	    
//		webView.postData("https://soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
//				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
//				"//soundcloud.com/soundcloud-callback.html&highlight=signup");
		
		
		/*webView.setWebViewClient(new WebViewClient(){
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            String url2="https://www.soundcloud.com/connect?client_id=b45b1aa10f1ac2941910a7f0d10f8e28&response_type" +
				"=token&scope=non-expiring%20fast-connect%20purchase%20upload&display=next&redirect_uri=https%3A" +
				"//soundcloud.com/soundcloud-callback.html&highlight=signup";
	             // all links  with in your site will be open inside the webview 
	             //links that start your domain example(http://www.example.com/)
	            if (url != null && url.startsWith(url2)){
	                return false;
	                } 
	           // all links that points outside the site will be open in a normal android browser
	          else  {
	                view.getContext().startActivity(
	                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	                return true;
	                }
	        }
	    });*/
 
	}
 
}