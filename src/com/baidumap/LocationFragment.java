package com.baidumap;

import com.baidu.mapapi.map.MapView;
import com.example.gorunning.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View locationView=inflater.inflate(R.layout.location_fram, container,false);
		return locationView;
	}

}
