package com.example.gorunning;

import com.baidumap.LocationFragment;
import com.example.mmusic.MusicFragment;
import com.myinfo.MyInfo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {
	private LocationFragment locationFram;
	private MusicFragment musicFram;
	private MyInfo myInfoFram;
	private View locationView;
	private View musicView;
	private View infoView;
	private ImageView img1,img2,img3;
	private TextView text1,text2,text3;
	private FragmentManager framMag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        framMag=getFragmentManager();
        setTab(1);	//第一次启动时，选中第一个页面
    }


	private void initView() {
		// TODO Auto-generated method stub
		locationView=findViewById(R.id.location_view);
		musicView=findViewById(R.id.music_view);
		infoView=findViewById(R.id.info_view);
		img1=(ImageView)findViewById(R.id.img1);
		img2=(ImageView)findViewById(R.id.img2);
		img3=(ImageView)findViewById(R.id.img3);
		text1=(TextView)findViewById(R.id.text1);
		text2=(TextView)findViewById(R.id.text2);
		text3=(TextView)findViewById(R.id.text3);
		locationView.setOnClickListener(this);
		musicView.setOnClickListener(this);
		infoView.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){			//点击底下导航栏，加载相应的界面
		case R.id.location_view:
			setTab(0);
			break;
		case R.id.music_view:
			setTab(1);
			break;
		case R.id.info_view:
			setTab(2);
			break;
		default:
			break;
		}
		
	}
	
	 private void setTab(int i) {		//在界面动态加载碎片
			// TODO Auto-generated method stub
		 clearTab();		//清除上次的状态
		 FragmentTransaction transaction=framMag.beginTransaction();	//开启事务
		 hideFragment(transaction);	  //先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		 switch(i){
		 case 0:
			 img1.setImageResource(R.drawable.location2);	//点击改变图标状态
			 text1.setTextColor(Color.WHITE);				//点击改变文字状态
			 if(locationFram==null){	//为空，创建并添加到界面上
				 locationFram=new LocationFragment();
				 transaction.add(R.id.content,locationFram);
			 }else{
				 transaction.show(locationFram);	//显示界面
			 }
			 break;
		 case 1:
			 img2.setImageResource(R.drawable.music2);
			 text2.setTextColor(Color.WHITE);
			 if(musicFram==null){
				 musicFram=new MusicFragment();
				 transaction.add(R.id.content,musicFram);
			 }else{
				 transaction.show(musicFram);
			 }
			 break;
		 case 2:
			 img3.setImageResource(R.drawable.info2);
			 text3.setTextColor(Color.WHITE);
			 if(myInfoFram==null){
				 myInfoFram=new MyInfo();
				 transaction.add(R.id.content, myInfoFram);
			 }else{
				 transaction.show(myInfoFram);
			 }
			 break;
			 
		 } 
		 transaction.commit();	//提交事务
		}

	private void hideFragment(FragmentTransaction transaction) {	//隐藏碎片
		// TODO Auto-generated method stub
		if(locationFram!=null)
			transaction.hide(locationFram);
		if(musicFram!=null)
			transaction.hide(musicFram);
		if(myInfoFram!=null)
			transaction.hide(myInfoFram);
	}


	private void clearTab() {		//恢复初始状态
		// TODO Auto-generated method stub
		img1.setImageResource(R.drawable.location1);
		text1.setTextColor(Color.parseColor("#a9b7b7"));	//灰色
		img2.setImageResource(R.drawable.music1);
		text2.setTextColor(Color.parseColor("#a9b7b7"));
		img3.setImageResource(R.drawable.info1);
		text3.setTextColor(Color.parseColor("#a9b7b7"));
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
