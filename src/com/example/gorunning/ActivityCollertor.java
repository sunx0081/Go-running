package com.example.gorunning;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollertor {		//活动管理器
	public static List<Activity> activitys=new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){		//添加活动
		activitys.add(activity);
	}
	
	public static void removeActivity(Activity activity){	//移出活动
		activitys.remove(activity);
	}
	
	public static void finshAll(){			//结束活动
		for(Activity activity:activitys){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	

}
