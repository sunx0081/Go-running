package com.example.gorunning;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityCollertor.addActivity(this);
	}
	
	protected void onDestory(){
		super.onDestroy();
		ActivityCollertor.removeActivity(this);
	}

}
