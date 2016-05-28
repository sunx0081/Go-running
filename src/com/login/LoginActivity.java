package com.login;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

import com.example.gorunning.MainActivity;
import com.example.gorunning.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity implements OnClickListener {
	private EditText username;
	private EditText password;
	private Button login,register;
	private CheckBox rem_pass;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private String name;
	private String pass;
	private Boolean isRemeber;
	
	@SuppressLint("HandlerLeak") 
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				Log.d("lg","00");
				login.setText("登录");
				Toast.makeText(LoginActivity.this, "请打开网络连接！",Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Log.d("lg","03");
				login.setText("登录");
				Toast.makeText(LoginActivity.this, "帐号或密码不能为空", Toast.LENGTH_SHORT).show();
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Bmob.initialize(LoginActivity.this,"b300f3479f34cbcfd5ca5644d026abba");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		findViews();
		if(isRemeber){
			getUserInfo();
		}
		
	}
	private void findViews() {
		pref=PreferenceManager.getDefaultSharedPreferences(this);
		editor=pref.edit();
		rem_pass=(CheckBox)findViewById(R.id.remember_pass);
		isRemeber=pref.getBoolean("remPass",false);
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
		login=(Button) findViewById(R.id.login);
		register=(Button) findViewById(R.id.register);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
	 }
			
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.login:
					login.setText("登录中...");
					name=username.getText().toString();
					pass=password.getText().toString();
					if(isNetworkEnable()==false){
						new Thread(new Runnable() {		//启动线程
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message message=new Message();
								message.what=0;
								handler.sendMessage(message);
							}
						}).start();
					}else if(name.equals("")||pass.equals("")){
						new Thread(new Runnable() {		//启动线程
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message message=new Message();
								message.what=1;
								handler.sendMessage(message);
							}
						}).start();
					}else{
					MyUser myUser=new MyUser();
					myUser.setUsername(name);
					myUser.setPassword(pass);
					myUser.login(LoginActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							if(rem_pass.isChecked()){	//检查复选框是否被选中
								saveUserInfo(name,pass);
							}else{
								editor.clear();		//清除缓存数据
								editor.commit();
							}
							Intent intent=new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							login.setText("登录");
							Toast.makeText(LoginActivity.this, "帐号或密码错误", Toast.LENGTH_LONG).show();

						}
					});
					}
					break;
				case R.id.register:
					Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
					startActivity(intent);
					break;
			}
				
	}
	private void getUserInfo(){		//从SharedPreferences获取数据，并填充
		String name=pref.getString("name",null);
		String pass=pref.getString("pass",null);
		username.setText(name);
		password.setText(pass);
		rem_pass.setChecked(true);
	}
	
	private void saveUserInfo(String username,String password){		//将数据保存在SharedPreferences中	
		editor.putString("name",username);
		editor.putString("pass", password);
		editor.putBoolean("remPass", true);
		editor.commit();
	}
	
	private boolean isNetworkEnable(){	//判断是否有网络
		ConnectivityManager cm=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWork=cm.getActiveNetworkInfo();
		if(netWork!=null)
			return netWork.isAvailable();
		else return false;
	}

}
