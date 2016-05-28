package com.baidumap;

import cn.bmob.v3.BmobObject;

public class UserData extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String distance;
	private String speed;
	private String time;
	
	public void setUsername(String username){
		this.username=username;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setDistance(String distance){
		this.distance=distance;
	}
	
	public String getDistance(){
		return distance;
	}
	
	public void setSpeed(String speed){
		this.speed=speed;
	}
	
	public String getSpeed(){
		return speed;
	}
	
	public void setTime(String time){
		this.time=time;
	}
	
	public String getTime(){
		return time;
	}
	

}
