package com.example.gorunning;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollertor {		//�������
	public static List<Activity> activitys=new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){		//��ӻ
		activitys.add(activity);
	}
	
	public static void removeActivity(Activity activity){	//�Ƴ��
		activitys.remove(activity);
	}
	
	public static void finshAll(){			//�����
		for(Activity activity:activitys){
			if(!activity.isFinishing()){
				activity.finish();
			}
		}
	}
	
	

}
