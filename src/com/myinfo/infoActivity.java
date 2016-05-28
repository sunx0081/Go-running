package com.myinfo;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import com.example.gorunning.BaseActivity;
import com.example.gorunning.R;
import com.login.MyUser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak") 
    public class infoActivity extends BaseActivity implements OnClickListener {
	private TextView info_name;
	private TextView info_age;
	private TextView info_sex;
	private ImageView edit_info;
	private ImageView info_back;
	private MyUser userInfo;
	public static final int SHOW_INFO=1;
	
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_INFO:
				initView();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bmob.initialize(infoActivity.this,"b300f3479f34cbcfd5ca5644d026abba");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_activity);
		getUserInfo();
		
	}
	
	private void initView(){
		info_name=(TextView)findViewById(R.id.info_name);
		info_age=(TextView)findViewById(R.id.info_age);
		info_sex=(TextView)findViewById(R.id.info_sex);
		edit_info=(ImageView)findViewById(R.id.edit_info);
		info_back=(ImageView)findViewById(R.id.info_back);
		info_back.setOnClickListener(this);
		edit_info.setOnClickListener(this);
		
		info_name.setText(userInfo.getUsername());
		info_age.setText(userInfo.getAge().toString());
		info_sex.setText(userInfo.getSex());
	}
	
	private void getUserInfo(){
		userInfo=BmobUser.getCurrentUser(this,MyUser.class);	//获取当前用户信息
		if(userInfo!=null){
			Message message=new Message();
			message.what=SHOW_INFO;
			handler.sendMessage(message);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.edit_info:
			Intent intent=new Intent(infoActivity.this,editInfoActivity.class);
			startActivityForResult(intent, 100);
			break;
		case R.id.info_back:
			finish();
		default:
			break;
		
		}		
	}
	
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		if(requestCode==100)
			refresh();	
 }

	private void refresh() {
		// TODO Auto-generated method stub
		getUserInfo();
		initView();
	}

}
