package com.example.gorunning;

import cn.bmob.v3.Bmob;

import com.baidumap.LocationFragment;
import com.example.mmusic.MusicFragment;
import com.myinfo.MyInfo;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends BaseActivity implements OnClickListener {
	private LocationFragment locationFram;
	private MusicFragment musicFram;
	private MyInfo myInfoFram;
	private View locationView;
	private View musicView;
	private View infoView;
	private ImageView img1,img2,img3;
	private TextView text1,text2,text3;
	private FragmentManager framMag;
	private NetworkReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(MainActivity.this,"b300f3479f34cbcfd5ca5644d026abba");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initBroadCast();
        //此方法用来同步本地和服务器的时间，避免出现由用户修改手机系统时间造成的sdk time error的问题
        Bmob.getInstance().synchronizeTime(this);
        initView();  
        framMag=getFragmentManager();
        setTab(0);	//第一次启动时，选中第一个页面
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
	
	private void initBroadCast(){	//注册广播，检测网络状态
		IntentFilter iFilter = new IntentFilter();  
		iFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mReceiver = new NetworkReceiver();  
		registerReceiver(mReceiver, iFilter);
	}
	
	 class NetworkReceiver extends BroadcastReceiver {  
	    public void onReceive(Context context, Intent intent) {  
	        String action = intent.getAction();  
	        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
	        	ConnectivityManager connMag=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	        	NetworkInfo networkInfo=connMag.getActiveNetworkInfo();
	        	if(networkInfo != null && networkInfo.isAvailable()){
	        		String name=networkInfo.getTypeName();
	        		Toast.makeText(MainActivity.this, "当前网络为"+name,Toast.LENGTH_SHORT).show();
	        	}else{
	        		Toast.makeText(MainActivity.this, "网络出错，请检查网络",Toast.LENGTH_SHORT).show();
	        	}	   
	        }  
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
	protected void onDestory() {
		// TODO Auto-generated method stub
		super.onDestory();
		unregisterReceiver(mReceiver);
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
        return super.onOptionsItemSelected(item);
    }


}
