package com.myinfo;

import com.example.gorunning.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyInfo extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View infoView=inflater.inflate(R.layout.my_info_fram, container,false);
		return infoView;
	}

}
