package com.baidumap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.gorunning.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LocationFragment extends Fragment implements OnClickListener {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button bt_location,bt_start,bt_stop;
	private TextView tx_distance,tx_speed;
	private LocationClient mLocationClient;
	BitmapDescriptor mCurrentMarker;
	boolean isFirstLoc = true;	// 是否首次定位
	List<LatLng> points = new ArrayList<LatLng>();
	private OverlayOptions options;
	private Date startTime;
	private Date endTime;
	private double distance=0;
	private double speed;
	Handler handler = new Handler();	// 定时器相关，定时检查mLocationClient是否启动
	boolean isStopLocClient = false;	// 是否停止定位服务
	private boolean isDraw=false;
	
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
		bt_location.setOnClickListener(this);
		bt_start.setOnClickListener(this);
		bt_stop.setOnClickListener(this);
		mMapView=(MapView)locationView.findViewById(R.id.map_view);
		mBaiduMap=mMapView.getMap();
		initLocation();	// 初始化定位信息
		return locationView;
	}
	
	private void initLocation() {
		// TODO Auto-generated method stub
		// 开启定位图层
				mBaiduMap.setMyLocationEnabled(true);
				// 定位初始化
				mLocationClient = new LocationClient(getActivity());
				mLocationClient.registerLocationListener(new MyLocationListenner());	//注册监听器
				LocationClientOption option = new LocationClientOption();
				//高精度定位模式：会同时使用网络定位和GPS定位,优先返回最高精度的定位结果
				option.setLocationMode(LocationMode.Hight_Accuracy);
				option.setOpenGps(true);// 打开gps
				option.setIsNeedAddress(true); //返回的定位结果包含地址信息  
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);	//发起定位请求时间间隔(单位ms)
				option.disableCache(true);//禁止启用缓存定位
				mLocationClient.setLocOption(option);
				MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(19f);	//设置地图缩放级别
				mBaiduMap.animateMapStatus(u);
	}
	
	class MyRunable implements Runnable {

		public void run() {
			if (!mLocationClient.isStarted()) {
				mLocationClient.start();
			}
			if (!isStopLocClient) {
				handler.postDelayed(this, 1000);
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
					.latitude(location.getLatitude())	//获取纬度
					.longitude(location.getLongitude())	//获取经度
					.build();	//构建生成定位数据对象
			mBaiduMap.setMyLocationData(locData);
			LatLng point = new LatLng(location.getLatitude(),
					location.getLongitude());
			points.add(point);
			if (isFirstLoc) {
				points.add(point);
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);	//定位到该点
			}
			if(isDraw){
			if(points.size()>0){
				drawMyRoute(points);
			}
			if(points.size()>2){
				int i=points.size()-2;
				int j=points.size()-1;
				LatLng gp1=points.get(i);
				LatLng gp2=points.get(j);
				distance+=DistanceUtil.getDistance(gp1,gp2);
				tx_distance.setText("路程："+String.format("%.2f",distance)+" M");
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
			if(!isGpsEnable()){
				AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
				dialog.setTitle("提醒：");
				dialog.setMessage("为了更精确定位请打开GPS！");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
						startActivityForResult(intent,0);
					}
				});
				dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				dialog.show();
			}
				isStopLocClient = false;
			if (!mLocationClient.isStarted()) {
				mLocationClient.start();				
			// 启动计时器(每1秒检测一次)
			handler.postDelayed(new MyRunable(), 1000);
			}
			break;
		case R.id.bt_start:
			isDraw=true;
			drawStart(points);
			startTime=new Date(System.currentTimeMillis());
			break;
		case R.id.bt_stop:
			isDraw=false;
			isFirstLoc=true;
			isStopLocClient = true;
			endTime=new Date(System.currentTimeMillis());
			double df=(endTime.getTime()-startTime.getTime())/1000f;
			speed=distance/df;
			tx_speed.setText("速率："+String.format("%.2f",speed)+" M/S");
			if (mLocationClient.isStarted()) {
				// 绘制终点
				drawEnd(points);
				mLocationClient.stop();

			}
			break;
		}
		
	}
	
	protected void drawStart(List<LatLng> points2) {	//绘制起点
		double myLat = 0.0;
		double myLng = 0.0;
		int i = points2.size()-1;
		LatLng ll = points2.get(i);
		myLat= ll.latitude;
		myLng= ll.longitude;
		LatLng startPoint = new LatLng(myLat, myLng);
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
	
	private boolean isNetworkEnable(){	//判断是否有网络
		ConnectivityManager cm=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netWork=cm.getActiveNetworkInfo();
		if(netWork!=null)
			return netWork.isAvailable();
		else return false;
	}
	
	private boolean isGpsEnable(){		//判断是否有GPS
		LocationManager locaManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
		return locaManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
