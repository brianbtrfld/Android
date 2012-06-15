package net.briangbutterfield.research.html5;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class HTML5ResearchActivity extends Activity
{
	
	WebView webView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		webView = (WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/www/index.html");
		
	}
}