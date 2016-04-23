package com.myinfo;

import com.example.gorunning.BaseActivity;
import com.example.gorunning.R;
import android.os.Bundle;
import android.view.Window;

public class dataActivity extends BaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.data_activity);
	}

}
