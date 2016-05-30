package com.example.rssreadertutby.rss;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class RssItem implements Parcelable{

	private String title;
	private String category;
	private String link;
	private String description;
	
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public RssItem(String title, String category, String link,
			String description, String imageURl) {
		super();
		this.title = title;
		this.category = category;
		this.link = link;
		this.description = description;
		this.imageUrl = imageURl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
	
	
}
