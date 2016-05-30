package com.example.rssreadertutby.rss;

import android.text.style.SuggestionSpan;

public class RssFeed {

	private String title;
	private String category;
	private String rssUrl;
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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


	public RssFeed(String title, String category, String rssUrl, String imageUrl) {
		super();
		this.title = title;
		this.category = category;
		this.rssUrl = rssUrl;
		this.imageUrl = imageUrl;
	}

	public String getRssUrl() {
		return rssUrl;
	}

	public void setRssUrl(String rssUrl) {
		this.rssUrl = rssUrl;
	}
	
}
