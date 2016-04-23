package com.login;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {
	private DatabaseHelper dbHelper;
	public UserService(Context context){
		dbHelper=new DatabaseHelper(context);
	}
	
	//��¼��
	public boolean login(String username,String password){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select * from user where username=? and password=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{username,password});		
		if(cursor.moveToFirst()==true){
			cursor.close();
			return true;
		}
		return false;
	}
	//ע����
	public boolean register(User user){
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="insert into user(username,password,age,sex) values(?,?,?,?)";
		Object obj[]={user.getUsername(),user.getPassword(),user.getAge(),user.getSex()};
		sdb.execSQL(sql, obj);	
		return true;
	}
	
	public boolean chekFromDb(String name){	//��֤�ʺ��Ƿ�ע��
		SQLiteDatabase sdb=dbHelper.getReadableDatabase();
		String sql="select username from user where username=?";
		Cursor cursor=sdb.rawQuery(sql, new String[]{name});
		if(cursor.moveToFirst()==true){		//���cursor���ϵ�һ��������,����ʺ��ѱ�ע��
			cursor.close();
			return true;
		}else
			return false;	
	}
}
