package com.myinfo;

import com.example.gorunning.ActivityCollertor;
import com.example.gorunning.R;
import com.example.mmusic.PlayService;
import com.login.LoginActivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyInfo extends Fragment implements OnClickListener{
	private Button bt_info,bt_share,bt_data,bt_exit;
	private TextView u_name;
	private String name;
	private NameBroadcast nameBr;
	
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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View infoView=inflater.inflate(R.layout.my_info_fram, container,false);
		bt_data=(Button)infoView.findViewById(R.id.my_data);
		bt_exit=(Button)infoView.findViewById(R.id.exit);
		bt_info=(Button)infoView.findViewById(R.id.my_info);
		bt_share=(Button)infoView.findViewById(R.id.my_share);
		u_name=(TextView)infoView.findViewById(R.id.user_name);
		bt_data.setOnClickListener(this);
		bt_exit.setOnClickListener(this);
		bt_info.setOnClickListener(this);
		bt_share.setOnClickListener(this);
		initBrCast();
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
		case R.id.my_share:
			Intent intent3=new Intent(getActivity(),dataActivity.class);
			startActivity(intent3);
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
	
	private void initBrCast(){			//注册广播
		IntentFilter inFi=new IntentFilter();
		inFi.addAction("com.action.GET_NAME");
		nameBr=new NameBroadcast();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(nameBr, inFi);
	}
	
	private void onDestory(){		//注销广播
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(nameBr);
	}
	
	public class NameBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals("com.action.GET_NAME")){
				name=intent.getStringExtra("username");
				u_name.setText(name);
			}
		}
		
	}
	

}
