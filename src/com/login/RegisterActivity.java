package com.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import com.example.gorunning.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	EditText myUsername;
	EditText password,pass_again;
	EditText age;
	RadioGroup sex;	
	Button register,r_back;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(RegisterActivity.this,"b300f3479f34cbcfd5ca5644d026abba");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_activity);
		findViews();
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name=myUsername.getText().toString().trim();
				String pass=password.getText().toString().trim();
				String pass_ag=pass_again.getText().toString().trim();
				String agestr=age.getText().toString().trim();
				String sexstr=((RadioButton)RegisterActivity.this
						.findViewById(sex.getCheckedRadioButtonId()))
						.getText().toString();				
				 if(isNum(agestr)==false){
					Toast.makeText(RegisterActivity.this, "年龄请输入正整数", Toast.LENGTH_SHORT).show();
				}else if(name.length()<6 || pass.length()<6){
					Toast.makeText(RegisterActivity.this, "帐号或密码的长度不能小于6个字符", Toast.LENGTH_SHORT).show();	
				}else if(!pass.equals(pass_ag)){
					Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
				}else{
				MyUser myUser=new MyUser();
				myUser.setUsername(name);
				myUser.setPassword(pass);
				myUser.setAge(Integer.parseInt(agestr));
				myUser.setSex(sexstr);
				myUser.signUp(RegisterActivity.this,new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(RegisterActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
						Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(RegisterActivity.this, "该帐号已被注册!", Toast.LENGTH_SHORT).show();
					}
				});
				}
	}
	});
		r_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
}
	private void findViews() {
		// TODO Auto-generated method stub
		myUsername=(EditText) findViewById(R.id.usernameRegister);
		password=(EditText) findViewById(R.id.passwordRegister);
		pass_again=(EditText)findViewById(R.id.pass_again);
		age=(EditText) findViewById(R.id.ageRegister);
		sex=(RadioGroup) findViewById(R.id.sexRegister);
		register=(Button) findViewById(R.id.Register);
		r_back=(Button)findViewById(R.id.r_back);
	}
	
	private boolean isNum(String str){	//判断年龄是否为正整数
		String pat="\\d{1,3}";	//正则表达式，只能是数字(\d)且必须出现1-3次({1,3})
		Pattern p=Pattern.compile(pat);
		Matcher m=p.matcher(str);
		return m.matches();	//如果匹配，返回true，反之返回false
	}
}
