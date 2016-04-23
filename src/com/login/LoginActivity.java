package com.login;


import com.example.gorunning.MainActivity;
import com.example.gorunning.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends Activity {
	private EditText username;
	private EditText password;
	private Button login,register;
	private CheckBox rem_pass;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		findViews();
	}
	private void findViews() {
		pref=PreferenceManager.getDefaultSharedPreferences(this);
		rem_pass=(CheckBox)findViewById(R.id.remember_pass);
		username=(EditText) findViewById(R.id.username);
		password=(EditText) findViewById(R.id.password);
		login=(Button) findViewById(R.id.login);
		register=(Button) findViewById(R.id.register);
		boolean isRemember=pref.getBoolean("remember_pass",false);
		login.setOnClickListener(new OnClickListener() {
			
		public void onClick(View v) {
				String name=username.getText().toString();
				String pass=password.getText().toString();
				//Log.i("TAG",name+"_"+pass);
				UserService uService=new UserService(LoginActivity.this);
				boolean flag=uService.login(name, pass);
				if(flag){
					//Log.i("TAG","µÇÂ¼³É¹¦");
					editor=pref.edit();
					if(rem_pass.isChecked()){	//¼ì²é¸´Ñ¡¿òÊÇ·ñ±»Ñ¡ÖÐ
						editor.putBoolean("remember_pass", true);
						editor.putString("name",name);
						editor.putString("pass", pass);
					}else{
						editor.clear();
					}
					editor.commit();
					Intent intent=new Intent(LoginActivity.this,MainActivity.class);
					intent.putExtra("username",name);
					startActivity(intent);
					finish();
				}else{
					//Log.i("TAG","µÇÂ¼Ê§°Ü");
					Toast.makeText(LoginActivity.this, "ÕÊºÅ»òÃÜÂë´íÎó", Toast.LENGTH_LONG).show();
				}
			}
		});
		if(isRemember){
			String name=pref.getString("name", "");
			String pass=pref.getString("pass", "");
			username.setText(name);
			password.setText(pass);
			rem_pass.setChecked(true);
		}
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

}
