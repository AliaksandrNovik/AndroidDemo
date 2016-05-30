package com.example.rssreadertutby.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.rssreadertutby.ListRSSItemsActivity;
import com.example.rssreadertutby.R;
import com.example.rssreadertutby.image.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter{

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageLoader; 

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		  inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.rss_item_list_row, null);

		TextView title = (TextView)vi.findViewById(R.id.title); 
		TextView link = (TextView)vi.findViewById(R.id.link);
		TextView category = (TextView)vi.findViewById(R.id.category); 
		ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); 

		HashMap<String, String> rssItem = new HashMap<String, String>();
		rssItem = data.get(position);

		// Setting all values in listview
		title.setText(rssItem.get(ListRSSItemsActivity.TAG_TITLE));
		link.setText(rssItem.get(ListRSSItemsActivity.TAG_LINK));
		category.setText(rssItem.get(ListRSSItemsActivity.TAG_CATEGORY));
		imageLoader.DisplayImage(rssItem.get(ListRSSItemsActivity.KEY_THUMB_URL), thumb_image);
		return vi;
	}



}
