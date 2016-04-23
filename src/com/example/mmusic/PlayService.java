package com.example.mmusic;

import java.io.IOException;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class PlayService extends Service {
	private MediaPlayer mPlayer;
	private List<MusicInfo> musicList;
	private String url;
	private int sPosition;
	private String msg;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new Binder();
	}
	
	public void onCreate(){
		super.onCreate();
		mPlayer=new MediaPlayer();
		musicList=getMusic.getMusicInfo(PlayService.this);
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {	//һ�׸貥����Ϻ�Ķ���
				// TODO Auto-generated method stub
				sPosition++;
				if(sPosition<=musicList.size()-1){
					Intent intent=new Intent("com.action.UPDATE_MUSIC");
					intent.putExtra("sPosition", sPosition);
					LocalBroadcastManager.getInstance(PlayService.this).sendBroadcast(intent);
					url=musicList.get(sPosition).getUrl();
					Play_music();
				}else{
					sPosition=0;
					Intent intent=new Intent("com.action.UPDATE_MUSIC");
					intent.putExtra("sPosition", sPosition);
					LocalBroadcastManager.getInstance(PlayService.this).sendBroadcast(intent);
					url=musicList.get(sPosition).getUrl();
					Play_music();
				}
			}
		});
		
	}
	
	public int onStartCommand(Intent intent,int flags,int startId){
		url=intent.getStringExtra("url");
		sPosition=intent.getIntExtra("aposition", -1);
		msg=intent.getStringExtra("msg");
		if(msg.equals("isPlay")){
			Play_music();
		}else if(msg.equals("before_music")){	//��һ��
			Before_music();
		}else if(msg.equals("next_music")){		//��һ��
			Next_music();
		}else if(msg.equals("music_pause")){	//��ͣ
			Pause_music();
		}else if(msg.equals("music_play")){		//��������
			Go_start();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void onDestory(){
		super.onDestroy();
	}
	
	public void Play_music(){
		mPlayer.reset();
		try {
			mPlayer.setDataSource(url);
			mPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mPlayer.start();
	}
	
	public void Pause_music(){
		if(mPlayer!=null && mPlayer.isPlaying()){
			mPlayer.pause();
		}
	}
	
	public void Go_start(){
		if(!mPlayer.isPlaying())
			mPlayer.start();
	}
	
	public void Next_music(){
		Play_music();
	}
	
	public void Before_music(){
		Play_music();
	}

}
