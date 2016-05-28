package com.myinfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.baidumap.UserData;
import com.example.gorunning.BaseActivity;
import com.example.gorunning.R;
import com.login.MyUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class dataActivity extends BaseActivity {
	private ListView dList_view;
	private List<UserData> data_lsit=new ArrayList<UserData>();
	private dataAdapter mAdapter;
	private ImageView data_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bmob.initialize(this,"b300f3479f34cbcfd5ca5644d026abba");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.data_activity);
		dList_view=(ListView)findViewById(R.id.data_list);
		data_back=(ImageView)findViewById(R.id.data_back);
		data_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		getData();
		Log.d("data","002");
	}
	
	private void getData(){
		MyUser myUser=BmobUser.getCurrentUser(dataActivity.this,MyUser.class);
		if(myUser!=null){
			String username=myUser.getUsername();
			BmobQuery<UserData> query=new BmobQuery<UserData>();
			query.addWhereEqualTo("username",username);
			query.setLimit(100);
			query.findObjects(this,new FindListener<UserData>() {
				
				@Override
				public void onSuccess(List<UserData> arg0) {
					// TODO Auto-generated method stub
					for(UserData userData:arg0){
						userData.getDistance();
						userData.getTime();
						userData.getCreatedAt();
						data_lsit.add(userData);
					}
					mAdapter=new dataAdapter(dataActivity.this, data_lsit);
					dList_view.setAdapter(mAdapter);
					Log.d("data","001");
				}
				
				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Toast.makeText(dataActivity.this, "获取数据失败！",Toast.LENGTH_SHORT).show();
				}
			});
	}
  }
}
