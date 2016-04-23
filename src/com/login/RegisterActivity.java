package com.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	EditText username;
	EditText password;
	EditText age;
	RadioGroup sex;	
	Button register;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		findViews();
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name=username.getText().toString().trim();
				String pass=password.getText().toString().trim();
				String agestr=age.getText().toString().trim();
				String sexstr=((RadioButton)RegisterActivity.this.findViewById(sex.getCheckedRadioButtonId())).getText().toString();
				UserService uService=new UserService(RegisterActivity.this);
				if(uService.chekFromDb(name)){
					Toast.makeText(RegisterActivity.this, "���ʺ��ѱ�ע��,����������", Toast.LENGTH_SHORT).show();
				}else if(isNum(agestr)==false){
					Toast.makeText(RegisterActivity.this, "����������������", Toast.LENGTH_SHORT).show();
				}else if(name.length()<6 || pass.length()<6){
					Toast.makeText(RegisterActivity.this, "�ʺŻ�����ĳ��Ȳ���С��6���ַ�", Toast.LENGTH_SHORT).show();
					
				}else{
				User user=new User();
				user.setUsername(name);
				user.setPassword(pass);
				user.setAge(Integer.parseInt(agestr));
				user.setSex(sexstr);
				uService.register(user);
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
				}
			}
		});
	}
	private void findViews() {
		username=(EditText) findViewById(R.id.usernameRegister);
		password=(EditText) findViewById(R.id.passwordRegister);
		age=(EditText) findViewById(R.id.ageRegister);
		sex=(RadioGroup) findViewById(R.id.sexRegister);
		register=(Button) findViewById(R.id.Register);
	}
	
	private boolean isNum(String str){	//�ж������Ƿ�Ϊ������
		String pat="\\d{1,3}";	//������ʽ��ֻ��������(\d)�ұ������1-3��({1,3})
		Pattern p=Pattern.compile(pat);
		Matcher m=p.matcher(str);
		return m.matches();	//���ƥ�䣬����true����֮����false
	}

}
