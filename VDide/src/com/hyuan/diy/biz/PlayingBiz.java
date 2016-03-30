package com.hyuan.diy.biz;

public interface PlayingBiz {

	public void start(int position);
	public void start(String url,String fileName);
	public void pause();
	public void next();
	public void prev();
	public void restart();
	public void seekTo(float progress);
	public void stop();
	
}
