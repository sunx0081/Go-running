package com.myinfo;


import java.lang.reflect.Field;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.example.gorunning.ActivityCollertor;
import com.example.gorunning.R;
import com.example.mmusic.PlayService;
import com.login.LoginActivity;
import com.login.MyUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfo extends Fragment implements OnClickListener{
	private Button bt_info,bt_pass,bt_data,bt_exit;
	private TextView error_pass;
	private EditText edit_pass;
	private String old_pass,new_pass;
	
	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bmob.initialize(getActivity(),"b300f3479f34cbcfd5ca5644d026abba");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View infoView=inflater.inflate(R.layout.my_info_fram, container,false);
		bt_data=(Button)infoView.findViewById(R.id.my_data);
		bt_exit=(Button)infoView.findViewById(R.id.exit);
		bt_info=(Button)infoView.findViewById(R.id.my_info);
		bt_pass=(Button)infoView.findViewById(R.id.edit_pass);
		bt_data.setOnClickListener(this);
		bt_exit.setOnClickListener(this);
		bt_info.setOnClickListener(this);
		bt_pass.setOnClickListener(this);
		return infoView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.my_info:
			Intent intent1=new Intent(getActivity(),infoActivity.class);
			startActivity(intent1);
			break;
		case R.id.my_data:
			Intent intent2=new Intent(getActivity(),dataActivity.class);
			startActivity(intent2);
			break;
		case R.id.edit_pass:
			oldPass_dialog(getActivity());
			break;
		case R.id.exit:
			Intent intent=new Intent();
			intent=new Intent(getActivity(),PlayService.class);
			getActivity().stopService(intent);	//销魂播放服务
			ActivityCollertor.finshAll();		//销毁所有活动
			intent=new Intent(getActivity(),LoginActivity.class);
			getActivity().startActivity(intent);
			break;
		}	
	}
	
	@SuppressLint("InflateParams") 
		private void oldPass_dialog(Context context){		//输入旧密码的弹框
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.edit_pass,null);
		edit_pass=(EditText)view.findViewById(R.id.edit_pass);
		error_pass=(TextView)view.findViewById(R.id.error_pass);
		AlertDialog.Builder dialog=new AlertDialog.Builder(context);
		dialog.setTitle("请输入旧的密码：");
		dialog.setCancelable(false);
		dialog.setView(view);
		dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				old_pass=edit_pass.getText().toString().trim();
				checkPass(old_pass);
				openDialog(dialog);
				error_pass.setText("密码错误，重新输入");	
			}		
		});
		dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				closeDialog(dialog);
			}
		});
		dialog.show();
	}
		
	private void checkPass(String oldPwd){		//验证旧密码是否正确
		MyUser myUser=BmobUser.getCurrentUser(getActivity(),MyUser.class);
		BmobQuery<MyUser> query=new BmobQuery<MyUser>();
		query.addWhereEqualTo("password",oldPwd);
		Log.d("oldPass",oldPwd);
		query.addWhereEqualTo("username",myUser.getUsername());
		Log.d("username",myUser.getUsername());
		query.findObjects(getActivity(), new FindListener<MyUser>() {
			
			@Override
			public void onSuccess(List<MyUser> arg0) {
				// TODO Auto-generated method stub
				if(arg0.size()==1){
					newPass_dialog(getActivity());
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	@SuppressLint("InflateParams") 
	private void newPass_dialog(Context context){	//输入新密码的弹框
	LayoutInflater inflater=LayoutInflater.from(context);
	View view=inflater.inflate(R.layout.edit_pass,null);
	edit_pass=(EditText)view.findViewById(R.id.edit_pass);
	AlertDialog.Builder dialog=new AlertDialog.Builder(context);
	dialog.setTitle("请输入新的密码：");
	dialog.setCancelable(false);
	dialog.setView(view);
	dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			new_pass=edit_pass.getText().toString().trim();
			Log.d("newPass",new_pass);
			Log.d("oldPpass",old_pass);
			updatePass(old_pass,new_pass);
		}		
	});
	dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
		}
	});
	dialog.show();
}
	
	private void updatePass(String oldPwd,String newPwd){	//修改密码
		MyUser.updateCurrentUserPassword(getActivity(), oldPwd, newPwd,new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "修改密码成功，请重新登录！",Toast.LENGTH_SHORT).show();
				MyUser.logOut(getActivity());	//清除缓存用户对象
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				getActivity().startActivity(intent);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "修改密码失败！",Toast.LENGTH_SHORT).show();
			}
		} );
	}
	
	private void closeDialog(DialogInterface dialog){	//关闭弹框
		try {	//下面三句控制弹框的关闭

            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,true);	//true表示要关闭弹框
         } catch (Exception e) {
            e.printStackTrace();
         }
	}
	
	private void openDialog(DialogInterface dialog){	//不关闭弹框
		try {	//下面三句控制弹框的关闭

            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog,false);	//false表示不要关闭弹框
         } catch (Exception e) {
            e.printStackTrace();
         }
	}
	

}
