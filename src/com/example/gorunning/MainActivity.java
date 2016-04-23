package com.example.gorunning;

import com.baidumap.LocationFragment;
import com.example.mmusic.MusicFragment;
import com.example.mmusic.PlayService;
import com.myinfo.MyInfo;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


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
	private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();  
		bind_service();	//�󶨷���
        getName();
        framMag=getFragmentManager();
        setTab(0);	//��һ������ʱ��ѡ�е�һ��ҳ��
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
		switch(v.getId()){			//������µ�������������Ӧ�Ľ���
		case R.id.location_view:
			setTab(0);
			break;
		case R.id.music_view:
			setTab(1);
			break;
		case R.id.info_view:
			 Intent intent=new Intent("com.action.GET_NAME");	//���͹㲥
			 intent.putExtra("username",username);
			 LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcastSync(intent);
			setTab(2);
			break;
		default:
			break;
		}
		
	}
	
	private String getName(){
		 Intent intent=getIntent();
	     username=intent.getStringExtra("username");
	     return username;
	}
	
	 private void setTab(int i) {		//�ڽ��涯̬������Ƭ
			// TODO Auto-generated method stub
		 clearTab();		//����ϴε�״̬
		 FragmentTransaction transaction=framMag.beginTransaction();	//��������
		 hideFragment(transaction);	  //�����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
		 switch(i){
		 case 0:
			 img1.setImageResource(R.drawable.location2);	//����ı�ͼ��״̬
			 text1.setTextColor(Color.WHITE);				//����ı�����״̬
			 if(locationFram==null){	//Ϊ�գ���������ӵ�������
				 locationFram=new LocationFragment();
				 transaction.add(R.id.content,locationFram);
			 }else{
				 transaction.show(locationFram);	//��ʾ����
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
		 transaction.commit();	//�ύ����
		}

	private void hideFragment(FragmentTransaction transaction) {	//������Ƭ
		// TODO Auto-generated method stub
		if(locationFram!=null)
			transaction.hide(locationFram);
		if(musicFram!=null)
			transaction.hide(musicFram);
		if(myInfoFram!=null)
			transaction.hide(myInfoFram);
	}


	private void clearTab() {		//�ָ���ʼ״̬
		// TODO Auto-generated method stub
		img1.setImageResource(R.drawable.location1);
		text1.setTextColor(Color.parseColor("#a9b7b7"));	//��ɫ
		img2.setImageResource(R.drawable.music1);
		text2.setTextColor(Color.parseColor("#a9b7b7"));
		img3.setImageResource(R.drawable.info1);
		text3.setTextColor(Color.parseColor("#a9b7b7"));
		
	}
	
	private void bind_service(){
		ServiceConnection connection=new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				
			}
		};
		Intent bindIntent=new Intent(this,PlayService.class);
		bindService(bindIntent,connection,BIND_AUTO_CREATE);
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
