package com.example.rssreadertutby;

public class WebSite {

	private int id;
	private String link;
	private String rssLink;
	private String description;
	private String title;
	private String imageUrl;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getRssLink() {
		return rssLink;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getRSSLink() {
		return rssLink;
	}
	public void setRssLink(String rssLink) {
		this.rssLink = rssLink;
	}
	public WebSite() {
		super();
	}
	public WebSite(String link, String rssLink, String description, String title, String imageUrl) {
		super();
		this.link = link;
		this.rssLink = rssLink;
		this.description = description;
		this.title = title;
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
