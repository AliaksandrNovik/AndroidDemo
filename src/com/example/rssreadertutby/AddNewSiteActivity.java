package com.example.rssreadertutby;

import com.example.rssreadertutby.AndroidRSSReaderApplicationActivity.loadStoreSites;
import com.example.rssreadertutby.rss.RssDataBaseHandler;
import com.example.rssreadertutby.rss.RssFeed;
import com.example.rssreadertutby.rss.RssParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddNewSiteActivity extends Activity {

	Button btnSubmit;
	Button btnCancel;
	EditText txtUrl;
	TextView lblMessage;
	Switch switchWidget;
	ListView listSites;

	RssParser rssParser = new RssParser();

	RssFeed rssFeed;

	// Progress Dialog
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_site);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// buttons
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		lblMessage = (TextView) findViewById(R.id.lblMessage);
		switchWidget = (Switch) findViewById(R.id.switch1);
		listSites = (ListView) findViewById(R.id.listViewTitle1);
		
		
		
		switchWidget.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), AndroidRSSReaderApplicationActivity.class);
				startActivity(in);
			}
		});

		listSites.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
				// getting page url
				TextView textView =  (TextView)view.findViewById(R.id.page_url);
				String pageUrl = textView.getText().toString();
				Toast.makeText(getApplicationContext(), pageUrl, Toast.LENGTH_SHORT).show();
				in.putExtra("rssUrl", pageUrl);
				startActivity(in);
			}
		});

		// Submit button click event
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String url = txtUrl.getText().toString();

				// Validation url
				Log.d("URL Length", "" + url.length());
				// check if user entered any data in EditText
				if (url.length() > 0) {
					lblMessage.setText("");
					String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
					if (url.matches(urlPattern)) {
						// valid url
						new loadRSSFeed().execute(url);
					} else {
						// URL not valid
						lblMessage.setText("Please enter a valid url");
					}
				} else {
					// Please enter url
					lblMessage.setText("Please enter website url");
				}

			}
		});

		// Cancel button click event
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * Background Async Task to get RSS data from URL
	 * */
	class loadRSSFeed extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddNewSiteActivity.this);
			pDialog.setMessage("Fetching RSS Information ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Inbox JSON
		 * */
		@Override
		protected String doInBackground(String... args) {
			String url = args[0];
			rssFeed = rssParser.getRSSFeed(url);
			Log.d("rssFeed", " "+ rssFeed);
			if (rssFeed != null) {
				Log.e("RSS URL",
						rssFeed.getTitle() + ""  
								+ rssFeed.getCategory());
				RssDataBaseHandler rssDb = new RssDataBaseHandler(
						getBaseContext());
				rssDb.getAllSites();
				WebSite site = new WebSite(rssFeed.getTitle(), url, url,
						rssFeed.getCategory(), rssFeed.getImageUrl());
				rssDb.addSite(site);
				Intent i = getIntent();
				// send result code 100 to notify about product update
				setResult(100, i);
				finish();
			} else {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						lblMessage.setText("Rss url not found. Please check the url or try again");
					}
				});
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					if (rssFeed != null) {

					}

				}
			});

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
