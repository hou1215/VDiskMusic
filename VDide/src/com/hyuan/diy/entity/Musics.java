package com.hyuan.diy.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Musics implements Serializable{
	private int 		id;
	private String 		name;
	private String 		singer;
	private String 		data;
	private long 		time;
	private String 		imgUrl;
	private String 		musicUrl;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	@Override
	public String toString() {
		return "Musics [id=" + id + ", name=" + name + ", singer=" + singer + ", data=" + data + ", time=" + time
				+ ", imgUrl=" + imgUrl + ", musicUrl=" + musicUrl + "]";
	}
	public Musics(int id, String name, String singer, String data, long time, String imgUrl, String musicUrl) {
		super();
		this.id = id;
		this.name = name;
		this.singer = singer;
		this.data = data;
		this.time = time;
		this.imgUrl = imgUrl;
		this.musicUrl = musicUrl;
	}
	public Musics() {
		super();
	}
	
	
}
