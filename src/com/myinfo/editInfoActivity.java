package com.myinfo;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.gorunning.R;
import com.login.LoginActivity;
import com.login.MyUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class editInfoActivity extends Activity implements OnClickListener {
	private ImageView edit_back,edit_save;
	private EditText edit_age,edit_sex;
	private MyUser myUser;
	private final int SHOW_INFO=1;
	
	@SuppressLint("HandlerLeak") 
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_INFO:
				initViews();
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
		Bmob.initialize(this,"b300f3479f34cbcfd5ca5644d026abba");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_info_activity);
		getUserInfo();
	}
	
	private void initViews() {
		// TODO Auto-generated method stub
		edit_age=(EditText)findViewById(R.id.edit_info_age);
		edit_sex=(EditText)findViewById(R.id.edit_info_sex);
		edit_back=(ImageView)findViewById(R.id.edit_back);
		edit_save=(ImageView)findViewById(R.id.edit_save);
		edit_save.setOnClickListener(this);
		edit_back.setOnClickListener(this);
		
		edit_age.setText(myUser.getAge().toString());
		edit_sex.setText(myUser.getSex());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.edit_save:
			saveInfo();
			break;
		case R.id.edit_back:
			finish();
			break;
		}
	}

	
	private void getUserInfo(){		//获取当前用户信息
		MyUser userInfo=BmobUser.getCurrentUser(this,MyUser.class);
		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
		query.addWhereEqualTo("objectId",userInfo.getObjectId());
		/*boolean isCache = query.hasCachedResult(editInfoActivity.this,MyUser.class);
		if(isCache){
			query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);	// 先从缓存取数据，如果没有的话，再从网络取。
		}else{
			query.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);	// 如果没有缓存的话，则先从网络中取
		}*/
		query.findObjects(this,new FindListener<MyUser>() {
			
			@Override
			public void onSuccess(List<MyUser> list) {
				// TODO Auto-generated method stub
				myUser=list.get(0);				
				Message message=new Message();
				message.what=SHOW_INFO;
				handler.sendMessage(message);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(editInfoActivity.this, "获取用户信息失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void saveInfo(){	//保存修改信息
		if(myUser==null){
			Toast.makeText(editInfoActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(this,LoginActivity.class);
			startActivity(intent);
		}else{
			myUser.setAge(Integer.parseInt(edit_age.getText().toString().trim()));
			myUser.setSex(edit_sex.getText().toString());
			AlertDialog.Builder dialog=new AlertDialog.Builder(editInfoActivity.this);
			dialog.setTitle("提醒：");
			dialog.setMessage("你确定要修改信息吗？");
			dialog.setCancelable(false);
			dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					myUser.update(editInfoActivity.this, myUser.getObjectId(),new UpdateListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Intent intent=new Intent(editInfoActivity.this,infoActivity.class);
							setResult(100,intent);
							finish();
							Toast.makeText(editInfoActivity.this, "修改信息成功！", Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(editInfoActivity.this, "修改信息失败！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			dialog.show();
			
		}
		
	}
}
