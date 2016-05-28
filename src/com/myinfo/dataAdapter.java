package com.myinfo;

import java.util.List;

import com.baidumap.UserData;
import com.example.gorunning.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class dataAdapter extends BaseAdapter {
	private UserData userData;
	private List<UserData> list_data;
	private Context cnt;
	
	public dataAdapter(Context cnt,List<UserData> list_data){
		this.cnt=cnt;
		this.list_data=list_data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_data.size();
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

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		userData=list_data.get(position);
		View view;
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			view=LayoutInflater.from(cnt).inflate(R.layout.data_item,null);
			viewHolder=new ViewHolder();
			viewHolder.d_distance=(TextView)view.findViewById(R.id.data_distance);
			viewHolder.d_time=(TextView)view.findViewById(R.id.data_time);
			viewHolder.d_date=(TextView)view.findViewById(R.id.data_date);
			view.setTag(viewHolder);
		}else{
			view=convertView;
			viewHolder=(ViewHolder)view.getTag();
		}
		viewHolder.d_distance.setText(userData.getDistance());
		viewHolder.d_time.setText(userData.getTime());
		viewHolder.d_date.setText(userData.getCreatedAt());
		return view;
	}
	
	class ViewHolder{		//内部类 提升ListView的运行效率
		TextView d_distance;
		TextView d_time;
		TextView d_date;
	}

}
