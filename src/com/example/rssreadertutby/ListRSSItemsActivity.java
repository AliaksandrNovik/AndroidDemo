package com.example.rssreadertutby;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.example.rssreadertutby.adapter.LazyAdapter;
import com.example.rssreadertutby.rss.RssFeed;
import com.example.rssreadertutby.rss.RssItem;
import com.example.rssreadertutby.rss.RssParser;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.*;

public class ListRSSItemsActivity extends ListActivity {


	// Progress Dialog
	private ProgressDialog pDialog;

	// Array list for list view
	ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

	RssParser rssParser = new RssParser();

	List<RssItem> rssItems = new ArrayList<RssItem>();
	List<RssItem> rssItemsUpdated = new ArrayList<RssItem>();

	RssFeed rssFeed;

	public static String TAG_TITLE = "title";
	public static String TAG_LINK = "link";
	public static String TAG_DESRIPTION = "description";
	public static String TAG_PUB_DATE = "pubDate";
	public static String TAG_CATEGORY = "category"; // not used
	public static final String KEY_THUMB_URL = "thumb_url";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_rssitems);

		Intent i = getIntent();

		ListView lv = getListView();
		ImageButton btnAddSite = (ImageButton) findViewById(R.id.btnAddSiteMenu);

		btnAddSite.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), AddNewSiteActivity.class);
				startActivityForResult(i, 100);
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent in = new Intent(getApplicationContext(), DisPlayWebPageActivity.class);
				// getting page url
				TextView textView =  (TextView)view.findViewById(R.id.link);
				String pageUrl = textView.getText().toString();
				Toast.makeText(getApplicationContext(), pageUrl, Toast.LENGTH_SHORT).show();
				in.putExtra("page_url", pageUrl);
				startActivity(in);
			}
		});

		class LoadRSSFeedItems extends AsyncTask<String, String, String> {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(
						ListRSSItemsActivity.this);
				pDialog.setMessage("Loading recent articles...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();

			}

			@Override
			protected String doInBackground(String... args) {
				// list of rss items
				Intent input = getIntent();
				String url = input.getStringExtra("rssUrl");
				if(url == null){
					rssItems = rssParser.getRSSFeedItems("http://news.tut.by/rss/index.rss");
				}else{
					rssItems = rssParser.getRSSFeedItems(url);

				}

				for(RssItem item : rssItems){
					HashMap<String, String> map = new HashMap<String, String>();

					map.put(TAG_TITLE, item.getTitle());
					map.put(TAG_LINK, item.getLink());
					map.put(TAG_CATEGORY, item.getCategory());
					map.put(KEY_THUMB_URL, item.getImageUrl());
					String description = item.getDescription();
					if(description.length() > 100){
						description = description.substring(0, 97) + "..";
					}
					map.put(TAG_DESRIPTION, description);

					rssItemList.add(map);
				}

				runOnUiThread(new Runnable() {
					public void run() {
						LazyAdapter adapter = new LazyAdapter(ListRSSItemsActivity.this, rssItemList);
						setListAdapter(adapter);
					}
				});
				return null;
			}

			protected void onPostExecute(String args) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
			}
		}
		LoadRSSFeedItems l = new LoadRSSFeedItems();
		l.execute();

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.startService) {
			item.setIcon(getResources().getDrawable(R.drawable.ic_pause_black_24dp));
			new Thread(new Runnable() {
				public void run() {
					Intent myIntent = new Intent(getApplicationContext(), UpdateNewsFeedService.class);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),  0, myIntent, 0);

					AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.add(Calendar.MILLISECOND, 60); // first time
					long frequency= 60 * 1000; // in ms 
					
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, pendingIntent);           
					myIntent.putExtra("itemList", rssItemList);
					startService(myIntent);
				}
			}).start();

		}

		if (id == R.id.stopService) {
			item.setIcon(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
			new Thread(new Runnable() {
				public void run() {
					stopService(new Intent(ListRSSItemsActivity.this, UpdateNewsFeedService.class));
				}
			}).start();

		}


		return super.onOptionsItemSelected(item);
	}


}
