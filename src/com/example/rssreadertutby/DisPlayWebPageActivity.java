package com.example.rssreadertutby;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisPlayWebPageActivity extends Activity {

	 WebView webview;
     
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.webview);
			getActionBar().setDisplayHomeAsUpEnabled(true);
	        Intent in = getIntent();
	        String page_url = in.getStringExtra("page_url");
	         
	        webview = (WebView) findViewById(R.id.webpage);
	             
	        webview.setWebChromeClient(new WebChromeClient());
	        webview.loadUrl(page_url);
	  	  
	    }
	     
	    @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	            webview.goBack();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }
	     
	    private class DisPlayWebPageActivityClient extends WebViewClient {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            view.loadUrl(url);
	            return true;
	        }
	    }

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			  switch (item.getItemId()) {
			    // Respond to the action bar's Up/Home button
			    case android.R.id.home:
			        NavUtils.navigateUpFromSameTask(this);
			        return true;
			    }
			    return super.onOptionsItemSelected(item);
		}
}
