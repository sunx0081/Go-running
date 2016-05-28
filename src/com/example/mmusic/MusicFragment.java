package com.example.mmusic;

import java.util.List;

import com.example.gorunning.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
//import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.content.ServiceConnection;
import android.os.Bundle;
//import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MusicFragment extends Fragment implements OnItemClickListener, OnClickListener {
	private ListView listView;
	private List<MusicInfo> musicList;
	private mAdapter musicAdapter;
	private ImageView play;
	private ImageView before;
	private ImageView next;
	private TextView title;
	private TextView artist;
	private boolean isPlay=false;
	private boolean isPause=true;
	private int mPosition=0;
	private PlayBdReceive playBdReceiver;
	/*private PlayService.MyBinder binder;
	
	private ServiceConnection conn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder=(PlayService.MyBinder)service;
		}
	};*/
	
	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		musicList=getMusic.getMusicInfo(activity);
		musicAdapter=new mAdapter(activity,musicList);
		/*Intent bindIntent=new Intent(activity,PlayService.class);
		activity.bindService(bindIntent, conn, Context.BIND_AUTO_CREATE);*/
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBroadcast();
		//添加来电监听事件
		TelephonyManager telManager=(TelephonyManager)getActivity()
									.getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MyPhoneListener(),PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View musicView=inflater.inflate(R.layout.music_fram, container, false);
		title=(TextView)musicView.findViewById(R.id.mTitle);
		artist=(TextView)musicView.findViewById(R.id.mArtist);
		play=(ImageView)musicView.findViewById(R.id.play);
		before=(ImageView)musicView.findViewById(R.id.before);
		next=(ImageView)musicView.findViewById(R.id.next);
		play.setOnClickListener(this);
		before.setOnClickListener(this);
		next.setOnClickListener(this);
		listView=(ListView)musicView.findViewById(R.id.list_view);
		listView.setAdapter(musicAdapter);
		listView.setOnItemClickListener(this);
		MusicInfo musicInfo=musicList.get(0);
		title.setText(musicInfo.getTitle());
		artist.setText(musicInfo.getArtist());
		return musicView;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mPosition=position;
		if(musicList!=null){
		MusicInfo musicInfo=musicList.get(mPosition);
		title.setText(musicInfo.getTitle());
		artist.setText(musicInfo.getArtist());
		//binder.Play_music(mPosition);
		Intent intent=new Intent(getActivity(),PlayService.class);
		intent.putExtra("url", musicInfo.getUrl());
		intent.putExtra("aposition", mPosition);
		intent.putExtra("msg","isPlay");
		getActivity().startService(intent);
		play.setImageResource(R.drawable.pause);
		isPlay=true;
		isPause=false;
	}
 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.play:
			/*if(isFirst){
				first_play();
				isFirst=false;
				isPlay=true;
				isPause=false;
			}else*/ 
			  if(isPlay){
				play.setImageResource(R.drawable.play);
				Intent intent=new Intent(getActivity(),PlayService.class);
				intent.putExtra("msg","music_pause");
				getActivity().startService(intent);
				//binder.Pause_music();
				isPlay=false;
				isPause=true;
			}else if(isPause){
				play.setImageResource(R.drawable.pause);
				Intent intent=new Intent(getActivity(),PlayService.class);
				intent.putExtra("msg","music_play");
				getActivity().startService(intent);
				//binder.Go_start();
				isPlay=true;
				isPause=false;
			}
			break;
		case R.id.before:
			if(isPlay)
				before_music();
			else if(isPause){
				play.setImageResource(R.drawable.pause);
				isPlay=true;
				isPause=false;
				before_music();	
			}
			break;
		case R.id.next:
			if(isPlay)
				next_music();
			else if(isPause){
				play.setImageResource(R.drawable.pause);
				isPlay=true;
				isPause=false;
				next_music();
			}
			break;
		}
		
	}

	private void next_music() {
		// TODO Auto-generated method stub
		mPosition=mPosition+1;
		if(mPosition<=musicList.size()-1){
			MusicInfo musicInfo=musicList.get(mPosition);
			title.setText(musicInfo.getTitle());
			artist.setText(musicInfo.getArtist());
			Intent intent=new Intent(getActivity(),PlayService.class);
			intent.putExtra("url", musicInfo.getUrl());
			intent.putExtra("aposition", mPosition);
			intent.putExtra("msg","next_music");
			getActivity().startService(intent);
			//binder.Play_music(mPosition);
		}else{
			mPosition=musicList.size()-1;
			Toast.makeText(getActivity(), "这是最后一首歌了！",Toast.LENGTH_SHORT).show();
		}
		
	}

	private void before_music() {
		// TODO Auto-generated method stub
		mPosition=mPosition-1;
		if(mPosition>=0){
			MusicInfo musicInfo=musicList.get(mPosition);
			title.setText(musicInfo.getTitle());
			artist.setText(musicInfo.getArtist());
			Intent intent=new Intent(getActivity(),PlayService.class);
			intent.putExtra("url", musicInfo.getUrl());
			intent.putExtra("aposition", mPosition);
			intent.putExtra("msg","before_music");
			getActivity().startService(intent);
			//binder.Play_music(mPosition);
		}else{
			mPosition=0;
			Toast.makeText(getActivity(), "这是第一首歌了！",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	class MyPhoneListener extends PhoneStateListener{	//电话监听器，监听电话状态
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			boolean isFree=false;
			switch(state){
			case TelephonyManager.CALL_STATE_RINGING:	//来电响铃状态
			case TelephonyManager.CALL_STATE_OFFHOOK:	//接听状态
				if(isPlay){		//来电音乐播放暂停
					play.setImageResource(R.drawable.play);
					Intent intent=new Intent(getActivity(),PlayService.class);
					intent.putExtra("msg","music_pause");
					getActivity().startService(intent);
					isPlay=false;
					isPause=true;
					isFree=true;
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:		//挂断空闲状态
				if(isFree){	//挂断电话音乐继续播放
					play.setImageResource(R.drawable.pause);
					Intent intent=new Intent(getActivity(),PlayService.class);
					intent.putExtra("msg","music_play");
					getActivity().startService(intent);
					isPlay=true;
					isPause=false;
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void initBroadcast(){	//注册本地广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.action.UPDATE_MUSIC");
		playBdReceiver=new PlayBdReceive();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(playBdReceiver, intentFilter);
	}
	
	
	class PlayBdReceive extends BroadcastReceiver{	//接受本地广播

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals("com.action.UPDATE_MUSIC")){
				mPosition=intent.getIntExtra("sPosition", -1);
				MusicInfo musicInfo=musicList.get(mPosition);
				if(mPosition>=0){
					title.setText(musicInfo.getTitle());
					artist.setText(musicInfo.getArtist());
					Log.d("BroadCast","is receive");
				}
			}
		}
		
	}
	
	@Override
	public void onDestroy() {	//注销广播
		// TODO Auto-generated method stub
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(playBdReceiver);
		Log.d("BroadCast","is onDestory");
	}
	
}
