package com.baidumap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.gorunning.R;
import com.login.MyUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class LocationFragment extends Fragment implements OnClickListener {
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	List<LatLng> points = new ArrayList<LatLng>();
	private OverlayOptions options;
	
	private Button bt_location,bt_start,bt_stop;
	private ImageView loc_menu;
	private TextView tx_distance,tx_speed,tx_time;
	private Date startTime;
	private Date endTime;
	private double distance=0;
	private double speed=0;
	private long time_sport=0;	//运动时间
	private Handler locHandler = new Handler();	// 定时器相关，定时检查mLocationClient是否启动
	boolean isStopLocClient=true;	// 停止定位服务
	private String s_distance,s_speed,time;
	private boolean isDraw=false;
	private boolean isFirst=true;
	private boolean isFrun=true;
	private boolean newLocation=false;
	private boolean isStart=false;
	private boolean isStop=true;
	private boolean isSave=false,flag=false;
	public static final int SHOW=1,CLEAR=0;
	
	@SuppressLint("HandlerLeak") 
	private Handler handler=new Handler(){			//更改UI
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW:
				tx_distance.setText("路程："+String.format("%.2f",distance)+" M");
				tx_speed.setText("速率："+String.format("%.2f",speed)+" M/S");
				tx_time.setText("时间："+toTime(time_sport));
				Log.d("loc","02");
				break;
			case CLEAR:
				mBaiduMap.clear();
				points.clear();		//此时points清空，不能马上调用run(),以免drawStart()出现数组越界的异常，要在定位回调中调用run()，此时points不为空
				flag=true;
				distance=0.00;
				speed=0.00;
				time_sport=0;
				tx_distance.setText(null);
				tx_speed.setText(null);
				tx_time.setText(null);
				mLocationClient.start();
				bt_start.setText("运动中");
				isStart=true;
				isStop=false;
				isDraw=true;
				Log.d("loc","01");
				break;
			default:
				break;
			}
		}
	};
	
	
	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getActivity().getApplicationContext());	//地图初始化	
		ShareSDK.initSDK(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View locationView=inflater.inflate(R.layout.location_fram, container,false);
		bt_location=(Button)locationView.findViewById(R.id.bt_location);
		bt_start=(Button)locationView.findViewById(R.id.bt_start);
		bt_stop=(Button)locationView.findViewById(R.id.bt_stop);
		tx_distance=(TextView)locationView.findViewById(R.id.distance);
		tx_speed=(TextView)locationView.findViewById(R.id.speed);
		tx_time=(TextView)locationView.findViewById(R.id.time_sport);
		loc_menu=(ImageView)locationView.findViewById(R.id.loc_menu);
		loc_menu.setOnClickListener(this);
		bt_location.setOnClickListener(this);
		bt_start.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		mMapView=(MapView)locationView.findViewById(R.id.map_view);
		initLocation();	// 初始化定位信息
		return locationView;
	}
	
	private void initLocation() {
				// TODO Auto-generated method stub
				mBaiduMap=mMapView.getMap();
				// 开启定位图层
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mLocationClient = new LocationClient(getActivity());
				mLocationClient.registerLocationListener(new MyLocationListenner());//注册监听器
				LocationClientOption option = new LocationClientOption();
				//高精度定位模式：会同时使用网络定位和GPS定位,优先返回最高精度的定位结果
				option.setLocationMode(LocationMode.Hight_Accuracy);
				option.setOpenGps(true);// 打开gps
				option.setIsNeedAddress(true); //返回的定位结果包含地址信息  
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);	//发起定位请求时间间隔(单位ms)
				option.disableCache(true);//禁止启用缓存定位
				option.setLocationNotify(true); //gps有效时按照1S1次频率输出GPS结果
				option.setIgnoreKillProcess(true); //定位SDK内部是一个SERVICE，并放到了独立进程,设置在stop的时候不杀死这个进程
				mLocationClient.setLocOption(option);
				mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(com.baidu.mapapi.map
						.MyLocationConfiguration.LocationMode.COMPASS, true,null ));
				MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(18f);	//设置地图缩放级别
				mBaiduMap.animateMapStatus(u);
				mLocationClient.start();
				isStopLocClient=false;
				if(isFirst){
					isFirst=false;
				 if(!isGpsEnable()){
					 Toast.makeText(getActivity(), "友情提醒：打开GPS可以让定位更准确！",Toast.LENGTH_SHORT).show();
				}
			}
	}
	
	
	class MyLocationListenner implements BDLocationListener{	//定位SDK监听函数

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			// 如果不显示定位精度圈，将accuracy赋值为0即可
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(0)	//定位精度(m)
					//.direction(50)
					.latitude(location.getLatitude())	//获取纬度
					.longitude(location.getLongitude())	//获取经度
					.build();	//构建生成定位数据对象
			mBaiduMap.setMyLocationData(locData);
			LatLng point = new LatLng(location.getLatitude(),
					location.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
			mBaiduMap.animateMapStatus(u);
			points.add(point);
			locHandler.postDelayed(new MyRunable(), 3000);	//3秒检测一次，定位是否开启
			if(isDraw){
				Log.d("loc","gg");
			if(points.size()>0){
				drawMyRoute(points);
				if(flag){
					run();	//在这调用是以免points中没有数据，drawStart()出现异常
					flag=false;
					Log.d("loc","03");
				}
			}
			if(points.size()>2){
				int i=points.size()-2;
				int j=points.size()-1;
				LatLng gp1=points.get(i);
				LatLng gp2=points.get(j);
				distance+=DistanceUtil.getDistance(gp1,gp2);
				endTime=new Date(System.currentTimeMillis());
				time_sport=(endTime.getTime()-startTime.getTime())/1000;
				speed=distance/time_sport;
				
				new Thread(new Runnable() {		//启动线程
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message message=new Message();
						message.what=SHOW;
						handler.sendMessage(message);
					}
				}).start();
			}
		 }
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

	}
	
	
	
	protected void drawMyRoute(List<LatLng> points2) {	//绘制运动轨迹
		OverlayOptions options = new PolylineOptions().color(0xAAFF0000)	//创建折线覆盖物,红色
				.width(7).points(points2);
		mBaiduMap.addOverlay(options);
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bt_location:
			if(newLocation){
				mLocationClient.start();
				newLocation=false;
			}
			else if(isStopLocClient){
				mLocationClient.start();
			}
			break;
		case R.id.bt_start:
			if(isFrun){
				Log.d("loc","05");
				isDraw=true;
				bt_start.setText("运动中");
				run();
				isStart=true;
				isStop=false;
				isFrun=false;
			}else{
				Log.d("loc","06");
				if(isStop){
					AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
					dialog.setTitle("提醒：");
					dialog.setMessage("重新开始,会清除本次的跑步数据，可以点击右上角上传数据！");
					dialog.setCancelable(false);
					dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isSave=false;
							new Thread(new Runnable() {		//启动线程
								public void run() {
									Message message=new Message();
									message.what=CLEAR;
									handler.sendMessage(message);
								}
							}).start();
							Log.d("loc","04");
						}
					});
					dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});	
					dialog.show();
				}else{
					Toast.makeText(getActivity(), "已经开始运动！",Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.bt_stop:
			if(isStart){
				bt_start.setText("开始");
				newLocation=true;
				isDraw=false;
				isStopLocClient = true;
				if (mLocationClient.isStarted()) {
					// 绘制终点
					drawEnd(points);
					mLocationClient.stop();
				}
				Toast.makeText(getActivity(), "运动结束，停止定位！",Toast.LENGTH_SHORT).show();
				isStart=false;
				isStop=true;
				isSave=true;
			}else{
				Toast.makeText(getActivity(), "请先点开始按钮，进行运动！",Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.loc_menu:
			showMenu(loc_menu);
			break;
		}
		
	}
	
	
	protected void run(){
			drawStart(points);
			startTime=new Date(System.currentTimeMillis());
			Toast.makeText(getActivity(), "运动开始，奔跑吧！",Toast.LENGTH_SHORT).show();
	}
	
	
	protected void drawStart(List<LatLng> points2) {	//绘制起点
		double myLat = 0.0;
		double myLng = 0.0;
		int i = points2.size()-1;
		LatLng ll = points2.get(i);
		myLat= ll.latitude;
		myLng= ll.longitude;
		LatLng startPoint = new LatLng(myLat, myLng);
		points.clear();
		points.add(startPoint);
		options = new DotOptions().center(startPoint).color(0xAA00ff00)	//画圆点,绿色
				.radius(15);
		mBaiduMap.addOverlay(options);

	}
	
	
	protected void drawEnd(List<LatLng> points2) {	//绘制终点
		double myLat = 0.0;
		double myLng = 0.0;
		int i = points2.size()-1;
		LatLng ll = points2.get(i);
		myLat= ll.latitude;
		myLng= ll.longitude;
			LatLng endPoint = new LatLng(myLat, myLng);
			options = new DotOptions().center(endPoint).color(0xAAff00ff)	//紫色
					.radius(15);
			mBaiduMap.addOverlay(options);
		}
	
	
	private boolean isGpsEnable(){		//判断是否有GPS
		LocationManager locaManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		return locaManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	class MyRunable implements Runnable {

		public void run() {
			if (!mLocationClient.isStarted()) {
				Log.d("run","kk");
				mLocationClient.start();
			}
			if (!isStopLocClient) {
				handler.postDelayed(this, 3000);
			}

		}

	}
	
	
	
	@SuppressLint("DefaultLocale")
	private void saveData(){			//上传数据
		transform();
		if(isSave){
			if(distance<100 || time_sport<60){
				Toast.makeText(getActivity(), "数据较小，不做上传保存处理！",Toast.LENGTH_SHORT).show();
			}else{
			MyUser myUser=BmobUser.getCurrentUser(getActivity(),MyUser.class);
			if(myUser!=null){
				String username=myUser.getUsername();
				UserData userData=new UserData();
				userData.setUsername(username);
				userData.setDistance(s_distance);
				userData.setSpeed(s_speed);
				userData.setTime(time);
				userData.save(getActivity(), new SaveListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "数据上传成功！",Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "数据上传失败！",Toast.LENGTH_SHORT).show();
					}
				});
			}
		  }
		}else {
			Toast.makeText(getActivity(), "请先完成跑步运动！",Toast.LENGTH_SHORT).show();
		}	
	}
	
	
	
	
	
	private void Share(){		//分享功能
		if(isSave){
			transform();
			OnekeyShare oks=new OnekeyShare();
			oks.disableSSOWhenAuthorize();
			oks.setTitle("和我一起 Go Running 吧！");
			oks.setTitleUrl("http://www.baidu.com");
			oks.setText("为了健康，奔跑吧！\n看我这次的跑步数据：\n跑步路程： "+s_distance
						+"\n跑步时间： "+time
						+"\n跑步速率： "+s_speed);
			oks.setComment("评论测试文本");
			oks.setUrl("http://www.baidu.com");
			oks.setSilent(false);
			oks.show(getActivity());
		}else{
			Toast.makeText(getActivity(), "请先完成跑步运动！",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void transform(){		//数据转换
		s_distance=String.format("%.2f M",distance);
		s_speed=String.format("%.2f M/S",speed);
		time=toTime(time_sport);
	}
	
	
	@SuppressLint("DefaultLocale") 
	private String toTime(long time){		//将时长转换成分：秒的形式
		long second=time%60;
		long minute=time/60;
		long hour=minute/60;
		minute=minute%60;
		return String.format("%02d:%02d:%02d",hour,minute,second);	//返回结果用string的format方法把时间转换成字符类型
	}
	
	
	private void showMenu(View view){	//菜单选项
		PopupMenu pMenu=new PopupMenu(getActivity(), view);
		pMenu.getMenuInflater().inflate(R.menu.my_menu,pMenu.getMenu());
		pMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {	//点击选项
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch(item.getItemId()){
				case R.id.share:
					Share();
					break;
				case R.id.save:
					saveData();
					break;
				case R.id.help:
					Intent intent=new Intent(getActivity(),helpActivity.class);
					getActivity().startActivity(intent);
					break;
				}
				return false;
			}
		});
		pMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {	//关闭选项
			
			@Override
			public void onDismiss(PopupMenu menu) {
				// TODO Auto-generated method stub
				
			}
		});
		pMenu.show();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ShareSDK.stopSDK(getActivity());
		// 退出时销毁定位
		mLocationClient.stop();
		isStopLocClient = true;
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
	}
	
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}
	
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isStopLocClient = true;	
		mLocationClient.stop();
	}

}
