package com.example.mmusic;

public class MusicInfo {
	private String title;
	private String artist;
	private int duration;	//Ê±³¤
	private String url;
	
	public MusicInfo(){}
	
	public MusicInfo(String title,String artist,int duration,String url){
		this.title=title;
		this.artist=artist;
		this.duration=duration;
		this.url=url;
	}
	
	
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getArtist(){
		return artist;
	}
	public void setArtist(String artist){
		this.artist=artist;
	}
	
	public int getDuration(){
		return duration;
	}
	public void setDuration(int duration){
		this.duration=duration;
	}
	
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url=url;
	}

	public String toString(){
		return "MusicInfo [title="+title+",artist="+artist+",duration="+duration+",url="+url+"]";
	}
}
