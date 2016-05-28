package com.login;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer age;
	private String sex;
	
	public void setAge(Integer age){
		this.age=age;
	}
	
	public Integer getAge(){
		return age;
	}
	
	public void setSex(String sex){
		this.sex=sex;
	}
	
	public String getSex(){
		return sex;
	}
}
