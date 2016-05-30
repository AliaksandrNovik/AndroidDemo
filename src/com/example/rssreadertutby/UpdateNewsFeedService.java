package com.example.rssreadertutby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.rssreadertutby.AddNewSiteActivity.loadRSSFeed;
import com.example.rssreadertutby.rss.RssItem;
import com.example.rssreadertutby.rss.RssParser;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class UpdateNewsFeedService  extends IntentService {

	public UpdateNewsFeedService() {
		super("ABCASDAD");


	}
	 @Override
	  protected void onHandleIntent(Intent intent) {
		 if (!rssItemsInner.get(0).getTitle().equals(rssItemsGlobal.get(0).getTitle())){
				sendNotif(rssItemsInner.get(0).getTitle());
			}
	  }

	NotificationManager nm;

	List<RssItem> rssItemsGlobal = new ArrayList<RssItem>();
	RssParser rssParser = new RssParser();
	List<RssItem> rssItemsInner = new ArrayList<RssItem>();

	public void onCreate() {
		super.onCreate();

		LoadRSSFeedItemsForGlobal load = new LoadRSSFeedItemsForGlobal();
		load.execute();

	}

	@SuppressWarnings("unchecked")
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		rssItemsGlobal = (List<RssItem>)intent.getParcelableExtra("itemList");
		LoadRSSFeedItemsForInner inner = new LoadRSSFeedItemsForInner();
		inner.execute();
		
		
		return 0;
	}

	class LoadRSSFeedItemsForGlobal extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			updateRss();
			return null;
		}

		protected List<RssItem> updateRss() {
			rssItemsGlobal = rssParser.getRSSFeedItems("http://news.tut.by/rss/index.rss");
			return rssItemsGlobal;
		}

	}

	class LoadRSSFeedItemsForInner extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			updateRssInner();
			return null;
		}


		protected List<RssItem> updateRssInner() {
			new Thread(new Runnable() {
				public void run() {
					rssItemsInner = rssParser.getRSSFeedItems("http://news.tut.by/rss/index.rss");
				}
			}).start();
			return rssItemsInner;

		}

	}




	void sendNotif(String rssItem) {
		Intent intent = new Intent(this, ListRSSItemsActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		Notification n  = new Notification.Builder(this)
		.setContentTitle(rssItem)
		.setContentText("Subject")
		.setSmallIcon(R.drawable.tutby)
		.setContentIntent(pIntent)
		.setAutoCancel(true)
		.build();

		NotificationManager notificationManager = 
				(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, n); 
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onDestroy(){
		super.onDestroy();
	}

}
