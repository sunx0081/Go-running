package com.example.mmusic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class getMusic {
 public static List<MusicInfo> getMusicInfo(Context ctx){
		String[] projection={MediaStore.Audio.Media.TITLE,  //设置查询的内容
							 MediaStore.Audio.Media.ARTIST,		 
							 MediaStore.Audio.Media.DURATION,
							 MediaStore.Audio.Media.DATA};
							 		 		 	
		Cursor cursor=ctx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
													,projection,null,null
													,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<MusicInfo> mMusicInfo=new ArrayList<MusicInfo>();
		cursor.moveToFirst();
		for(int i=0;i<cursor.getCount();i++){
			MusicInfo musicInfo=new MusicInfo();			
		        String title = cursor.getString((cursor   
		            .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));//音乐标题  
		        String artist = cursor.getString(cursor  
		            .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));//歌手  
		        int duration = cursor.getInt(cursor  
		            .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));//时长  
		        String url=cursor.getString((cursor   
			            .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));//路径
		    	musicInfo.setTitle(title);
		    	musicInfo.setArtist(artist);
		    	musicInfo.setDuration(duration);
		    	musicInfo.setUrl(url);
		    	mMusicInfo.add(musicInfo);
		    	cursor.moveToNext();
		}
		return mMusicInfo;
	}
}
