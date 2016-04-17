package com.example.mmusic;

import java.util.List;

import com.example.gorunning.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MusicFragment extends Fragment implements OnItemClickListener {
	private ListView listView;
	private List<MusicInfo> musicList;
	private mAdapter musicAdapter;
	
	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		musicList=getMusic.getMusicInfo(activity);
		musicAdapter=new mAdapter(activity,musicList);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View musicView=inflater.inflate(R.layout.music_fram, container, false);
		listView=(ListView)musicView.findViewById(R.id.list_view);
		listView.setAdapter(musicAdapter);
		listView.setOnItemClickListener(this);
		return musicView;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
