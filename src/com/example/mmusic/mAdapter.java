package com.example.mmusic;

import java.util.List;

import com.example.gorunning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mAdapter extends BaseAdapter {		//自定义的适配器
	
	private MusicInfo musicInfo;
	private List<MusicInfo> mInfoList;
	private Context ctx;
		
	public mAdapter(Context ctx,List<MusicInfo> mInfoList){	//构造函数
		this.ctx=ctx;
		this.mInfoList=mInfoList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		musicInfo=mInfoList.get(position);
		ViewHolder viewHolder = null;
		View view;
		if(convertView==null){
		viewHolder=new ViewHolder();
		view=LayoutInflater.from(ctx).inflate(R.layout.music_item,null);
		viewHolder.mTitle=(TextView)view.findViewById(R.id.title);
		viewHolder.mArtist=(TextView)view.findViewById(R.id.artist);
		viewHolder.mDuration=(TextView)view.findViewById(R.id.duration);
		view.setTag(viewHolder);	//将ViewHolder存在View中
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();	//重新获取ViewHolder
		}
		viewHolder.mTitle.setText(musicInfo.getTitle());
		viewHolder.mArtist.setText(musicInfo.getArtist());
		viewHolder.mDuration.setText(toTime(musicInfo.getDuration()));	//必须将int转换成String，否则报错
		return view;
	}

	class ViewHolder{		//内部类 提升ListView的运行效率
		TextView mTitle;
		TextView mArtist;
		TextView mDuration;
	}
	
	public static String toTime(int duration){		//将时长转换成分：秒的形式
		int time=duration/1000;
		int minute=time/60;
		int second=time%60;
		return String.format("%02d:%02d",minute,second);	//返回结果用string的format方法把时间转换成字符类型
	}


}
