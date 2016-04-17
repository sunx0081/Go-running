package com.example.mmusic;

import java.util.List;

import com.example.gorunning.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mAdapter extends BaseAdapter {		//�Զ����������
	
	private MusicInfo musicInfo;
	private List<MusicInfo> mInfoList;
	private Context ctx;
		
	public mAdapter(Context ctx,List<MusicInfo> mInfoList){	//���캯��
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
		view.setTag(viewHolder);	//��ViewHolder����View��
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();	//���»�ȡViewHolder
		}
		viewHolder.mTitle.setText(musicInfo.getTitle());
		viewHolder.mArtist.setText(musicInfo.getArtist());
		viewHolder.mDuration.setText(toTime(musicInfo.getDuration()));	//���뽫intת����String�����򱨴�
		return view;
	}

	class ViewHolder{		//�ڲ��� ����ListView������Ч��
		TextView mTitle;
		TextView mArtist;
		TextView mDuration;
	}
	
	public static String toTime(int duration){		//��ʱ��ת���ɷ֣������ʽ
		int time=duration/1000;
		int minute=time/60;
		int second=time%60;
		return String.format("%02d:%02d",minute,second);	//���ؽ����string��format������ʱ��ת�����ַ�����
	}


}
