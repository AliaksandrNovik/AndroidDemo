package com.example.rssreadertutby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.rssreadertutby.adapter.LazyAdapter;
import com.example.rssreadertutby.rss.RssDataBaseHandler;
import com.example.rssreadertutby.rss.RssFeed;
import com.example.rssreadertutby.rss.RssParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AndroidRSSReaderApplicationActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Array list for list view
	ArrayList<HashMap<String, String>> rssFeedList;

	RssParser rssParser = new RssParser();

	RssFeed rssFeed;

	// button add new website
	ImageButton btnAddSite;

	// array to trace sqlite ids
	String[] sqliteIds;
	EditText txtUrl;
	TextView lblMessage;
	Button btnCancel;

	public static String TAG_ID = "id";
	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";
	private static String KEY_IMAGE_FEED = "image url";

	// List view
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_site);
		txtUrl = (EditText) findViewById(R.id.txtUrl);
		btnAddSite = (ImageButton) findViewById(R.id.btnAddSiteMenu);
		lblMessage = (TextView) findViewById(R.id.lblMessage);
		btnCancel = (Button) findViewById(R.id.btnCancel);
	
		loadStoreSites load = new loadStoreSites();
		load.execute();
		// Cancel button click event
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		// Hashmap for ListView
		rssFeedList = new ArrayList<HashMap<String, String>>();


		lv = (ListView) findViewById(R.id.listViewTitle1); 

		// selecting single ListView item



		/**
		 * Add new website button click event listener
		 * */
		



		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				TextView textView = ((TextView) view.findViewById(R.id.link));
				String url = textView.getText().toString();
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
				// passing sqlite row id
				in.putExtra("rssUrl", url);
				startActivity(in);
			}
		});
	}

	/**
	 * Response from AddNewSiteActivity.java
	 * if response is 100 means new site is added to sqlite
	 * reload this activity again to show 
	 * newly added website in listview
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		/*    if (resultCode == 100) {
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }*/
	}

	/**
	 * Building a context menu for listview
	 * Long press on List row to see context menu
	 * */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("Delete");
		menu.add(Menu.NONE, 0, 0, "Delete Feed");
	}

	/**
	 * Responding to context menu selected option
	 * */

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		// check for selected option
		if(menuItemIndex == 0){
			// user selected delete
			// delete the feed
			RssDataBaseHandler rssDb = new RssDataBaseHandler(getApplicationContext());
			WebSite site = new WebSite();
			site.setId(Integer.parseInt(sqliteIds[info.position]));
			rssDb.deleteSite(site);
			//reloading same activity again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

		return true;
	}

	class loadStoreSites extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(
					AndroidRSSReaderApplicationActivity.this);
			pDialog.setMessage("Loading websites ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// updating UI from Background Thread

			RssDataBaseHandler rssDb = new RssDataBaseHandler(
					getBaseContext());

			// listing all websites from SQLite
			List<WebSite> siteList = rssDb.getAllSites();

			sqliteIds = new String[siteList.size()];

			// loop through each website
			for (int i = 0; i < siteList.size(); i++) {

				WebSite s = siteList.get(i);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_ID, String.valueOf(s.getId()));
				map.put(TAG_TITLE, s.getTitle());
				map.put(TAG_LINK, s.getRSSLink());
				map.put(KEY_IMAGE_FEED, s.getImageUrl());

				// adding HashList to ArrayList
				rssFeedList.add(map);

				// add sqlite id to array
				// used when deleting a website from sqlite
				sqliteIds[i] = String.valueOf(s.getId());
			}
			/**
			 * Updating list view with websites
			 * */
			runOnUiThread(new Runnable() {
				public void run() {

					LazyAdapter adapter = new LazyAdapter(AndroidRSSReaderApplicationActivity.this, rssFeedList);
					// updating listview
					lv.setAdapter(adapter);
					registerForContextMenu(lv);

					//stuff that updates ui

				}
			});


			return null;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String args) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
		}

	}

}
