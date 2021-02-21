package com.ysh.mapdemo.newFragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ysh.mapdemo.Bean.normalBean;
import com.ysh.mapdemo.FirstFragmentActivity;
import com.ysh.mapdemo.R;
import com.ysh.mapdemo.Room.PoiPlace;
import com.ysh.mapdemo.Room.PoiPlaceRepository;
import com.ysh.mapdemo.Room.PoiPlaceViewModel;
import com.ysh.mapdemo.RouteActivity;
import com.ysh.mapdemo.newActivity;
import com.ysh.mapdemo.overlay.WalkRouteOverlay;
import com.ysh.mapdemo.utils.MapUtil;

import org.checkerframework.checker.linear.qual.Linear;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.ysh.mapdemo.utils.MapUtil.convertToLatLng;
import static com.ysh.mapdemo.utils.MapUtil.convertToLatLonPoint;
import static com.ysh.mapdemo.utils.MapUtil.getFriendlyLength;


public class FirstFragment extends Fragment implements
        AMapLocationListener,LocationSource, AMap.OnMapClickListener,RouteSearch.OnRouteSearchListener,GeocodeSearch.OnGeocodeSearchListener,
AMap.OnMarkerClickListener,AMap.OnMarkerDragListener,AMap.InfoWindowAdapter{

    private ConstraintLayout constraintLayout1,constraintLayout2;
    private TextView poi_tv,poi_address,poi_distance,poi_zhoubian,poi_star,poi_share,poi_car;
    private ImageView poi_iv,poi_ivzb,poi_ivstar,poi_ivShare,poi_ivCar;
    private Button mBtn1,mBtn2;
    private ImageButton mIBtn3;

    private PoiPlaceRepository poiPlaceRepository;
    private PoiPlaceViewModel poiPlaceViewModel;

    private Button mBtn;

    private LatLng mLatLng;
    private LatLng MyLatLng;
    //浮动按钮  清空地图标点
    private FloatingActionButton fabClearMarker;
    private FloatingActionButton return_myplace;
    private FloatingActionButton start_route;
    //标点列表
    private List<Marker> markerList = new ArrayList<>();
    private String MarkerClickTitle;
    private String MarkerClickAdress;
    private Marker marker;

    private String keyWord = "";
    //地理编码搜索
    private GeocodeSearch geocodeSearch;
    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;
    private String cityCode;


    private TextView searchTv;
    private ImageView searchIv;
    //起点
    private LatLonPoint mStartPoint;
    //终点
    private LatLonPoint mEndPoint;

    //路线搜索对象
    private RouteSearch routeSearch;

    private static final String TAG = "RouteActivity";
    //地图
    private MapView mapView;
    //地图控制器
    private AMap aMap = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //位置更改监听
    private OnLocationChangedListener mListener;
    //定义一个UiSettings对象
    private UiSettings mUiSettings;
    //定位样式
    private MyLocationStyle myLocationStyle = new MyLocationStyle();

    public FirstFragment() {

    }


    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        poiPlaceRepository = new PoiPlaceRepository(getActivity());
        initPoiView();
        initPoiListener();
        mBtn = getView().findViewById(R.id.button);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), newActivity.class);
                startActivity(intent);
            }
        });
        searchTv = getView().findViewById(R.id.first_search_tv);
        searchIv = getView().findViewById(R.id.first_search_iv);
        fabClearMarker = getView().findViewById(R.id.fab_clear_marker);
        return_myplace = getView().findViewById(R.id.return_myplace);
        start_route = getView().findViewById(R.id.start_route);
        start_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEndPoint = convertToLatLonPoint(mLatLng);
//                startRouteSearch();
//                start_route.hide();
            }
        });
        return_myplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.startLocation();
            }
        });
        fabClearMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllMarker();
            }
        });
        Bundle bundle = this.getArguments();//得到从Activity传来的数据
        String mess = null;
        if (bundle != null) {
            mess = bundle.getString("data");
        }
        searchTv.setText(normalBean.str);
        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FirstFragmentActivity.class);
                startActivity(intent);
            }
        });
        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FirstFragmentActivity.class);
                startActivity(intent);
            }
        });
        //初始化定位
        initLocation();
        //初始化地图
        initMap(savedInstanceState);

        //启动定位
        mLocationClient.startLocation();

        initListener();
        initRoute();
        if(normalBean.str.equals(" ")){
            Toast.makeText(getActivity(),"ttt",Toast.LENGTH_SHORT).show();
        }else{
            updateAddress();
        }

    }
    public void initPoiView(){
        constraintLayout1 = getView().findViewById(R.id.first_poi_1);
        constraintLayout2 = getView().findViewById(R.id.first_poi_2);
        poi_address = getView().findViewById(R.id.first_poi_address);
        poi_distance = getView().findViewById(R.id.first_poi_distance);
        poi_ivzb = getView().findViewById(R.id.first_poi_zhoubian);
        poi_ivstar = getView().findViewById(R.id.first_poi_star);
        poi_ivShare = getView().findViewById(R.id.first_poi_share);
        poi_ivCar = getView().findViewById(R.id.first_poi_car);
        poi_iv = getView().findViewById(R.id.first_poi_iv);
        poi_zhoubian = getView().findViewById(R.id.first_poi_zhoubian_tv);
        poi_star = getView().findViewById(R.id.first_poi_star_tv);
        poi_share = getView().findViewById(R.id.first_poi_share_tv);
        poi_car = getView().findViewById(R.id.first_poi_car_tv);
        poi_tv = getView().findViewById(R.id.first_poi_tv);
        mBtn1 = getView().findViewById(R.id.firs_poi_add);
        mBtn2 = getView().findViewById(R.id.first_poi_route);
        mIBtn3 = getView().findViewById(R.id.first_poi_shtdn);
        constraintLayout1.setVisibility(View.INVISIBLE);
        constraintLayout2.setVisibility(View.INVISIBLE);

        poi_ivstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
    public void initPoiListener(){
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndPoint = convertToLatLonPoint(mLatLng);
                startRouteSearch();
                constraintLayout1.setVisibility(View.INVISIBLE);
                constraintLayout2.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void clearAllMarker() {
            if (markerList != null && markerList.size()>0){
                for (Marker markerItem : markerList) {
                    markerItem.remove();
                }
            }
            fabClearMarker.hide();
            constraintLayout1.setVisibility(View.INVISIBLE);
            constraintLayout2.setVisibility(View.INVISIBLE);
    }
    public void clearMarker() {
        if (markerList != null && markerList.size()>0){
            for (Marker markerItem : markerList) {
                markerItem.remove();
            }
        }
    }
    /**
     * Marker点击事件
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        //showMsg("点击了标点");
        //显示InfoWindow
        if (!marker.isInfoWindowShown()) {
            //显示
            marker.showInfoWindow();
        } else {
            //隐藏
            marker.hideInfoWindow();
        }
        return true;
    }
    /**
     * 开始拖动
     * @param marker
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG,"开始拖动");
    }

    /**
     * 拖动中
     * @param marker
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG,"拖动中");
    }

    /**
     * 拖动完成
     * @param marker
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        mLatLng = marker.getPosition();
        latlonToAddress(marker.getPosition());
        Log.d(TAG,"拖动完成");
    }
    /**
     * 添加地图标点
     *
     * @param latLng
     */
    private void addMarker(LatLng latLng) {
        latlonToAddress(latLng);
        mLatLng = latLng;

//        //添加标点
//        Marker marker = aMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(MarkerClickAdress).snippet("详细信息"));
//        marker.showInfoWindow();
//        //设置标点的绘制动画效果
//        Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
//        long duration = 1000L;
//        animation.setDuration(duration);
//        animation.setInterpolator(new LinearInterpolator());
//
//        marker.setAnimation(animation);
//        marker.startAnimation();
//        markerList.add(marker);
    }
    /**
     * 修改内容
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(Marker marker) {
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 修改背景
     *
     * @param marker
     */
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }
    /**
     * 渲染
     *
     * @param marker
     * @param view
     */
    private void render(Marker marker, View view) {
        ((ImageView) view.findViewById(R.id.badge))
                .setImageResource(R.drawable.icon_yuan);

        //修改InfoWindow标题内容样式
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        //修改InfoWindow片段内容样式
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                    snippetText.length(), 0);
            snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }
    private void latlonToAddress(LatLng latLng) {
        //位置点  通过经纬度进行构建
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        //逆编码查询  第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.AMAP);
        //异步获取地址信息
        geocodeSearch.getFromLocationAsyn(query);
    }


    private void initListener() {
        searchTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
//                    Toast.makeText(RouteActivity.this,"请输入搜索关键字",Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    doSearchQuery(keyWord);
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    //隐藏软键盘
//                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                    // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
//                    GeocodeQuery query = new GeocodeQuery(keyWord,cityCode);//城市编码);
//                    Log.d("String",toString().valueOf(cityCode));
//                    geocodeSearch.getFromLocationNameAsyn(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    public void updateAddress(){
        GeocodeQuery query = new GeocodeQuery(normalBean.str,cityCode);//城市编码);
        Log.d("String",toString().valueOf(cityCode));
        geocodeSearch.getFromLocationNameAsyn(query);
    }
//    protected void doSearchQuery(String key) {
//        currentPage = 0;
//        //不输入城市名称有些地方搜索不到
//        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query = new PoiSearch.Query(key, "", "");
//        // 设置每页最多返回多少条poiitem
//        query.setPageSize(10);
//        // 设置查询页码
//        query.setPageNum(currentPage);
//
//        //构造 PoiSearch 对象，并设置监听
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
//        poiSearch.searchPOIAsyn();
//    }
    /**
     * 改变地图中心位置
     * @param latLng 位置
     */
    private void updateMapCenter(LatLng latLng) {
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        CameraPosition cameraPosition = new CameraPosition(latLng, 16, 30, 0);
        //位置变更
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //改变位置
        aMap.animateCamera(cameraUpdate);
        aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker"));
    }
    /**
     * 坐标转地址
     * @param regeocodeResult
     * @param rCode
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        //解析result获取地址描述信息
        if(rCode == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            showMsg("地址："+regeocodeAddress.getFormatAddress());
            float distance = AMapUtils.calculateLineDistance(mLatLng,MyLatLng);
            float qianmifload = distance/1000;
            qianmifload = BigDecimal.valueOf(qianmifload)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
            //显示浮动按钮
            clearMarker();
            fabClearMarker.show();
            poi_address.setText(regeocodeAddress.getFormatAddress());
            Toast.makeText(getActivity(),regeocodeAddress.getFormatAddress(),Toast.LENGTH_SHORT).show();
//            +regeocodeAddress.getStreetNumber().getStreet()+regeocodeAddress.getStreetNumber().getNumber()

            start_route.show();
            //添加标点
//            Marker marker;
            if(qianmifload>1){
                poi_distance.setText("距您"+String.valueOf(qianmifload)+"千米");
                marker = aMap.addMarker(new MarkerOptions().draggable(true).position(mLatLng).title(regeocodeAddress.getFormatAddress()+regeocodeAddress.getStreetNumber().getStreet()+regeocodeAddress.getStreetNumber().getNumber()).snippet("距您"+String.valueOf(qianmifload)+"千米"));
            }else {
                poi_distance.setText("距您"+(int) distance + "米");
                marker = aMap.addMarker(new MarkerOptions().draggable(true).position(mLatLng).title(regeocodeAddress.getFormatAddress()).snippet("距您"+(int) distance + "米"));
            }
            marker.showInfoWindow();
            //设置标点的绘制动画效果
            Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
            long duration = 1000L;
            animation.setDuration(duration);
            animation.setInterpolator(new LinearInterpolator());

            marker.setAnimation(animation);
            marker.startAnimation();
            markerList.add(marker);
            constraintLayout1.setVisibility(View.VISIBLE);
            constraintLayout2.setVisibility(View.VISIBLE);

        }else {
            showMsg("获取地址失败");
        }
    }

    /**
     * 地址转坐标
     * @param geocodeResult
     * @param rCode
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (rCode == PARSE_SUCCESS_CODE) {
            List<GeocodeAddress> geocodeAddressList = geocodeResult.getGeocodeAddressList();
            if(geocodeAddressList!=null && geocodeAddressList.size()>0){
                LatLonPoint latLonPoint = geocodeAddressList.get(0).getLatLonPoint();
                //显示解析后的坐标
                LatLng latLng = new LatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude());
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                       addMarker(latLng);
                        updateMapCenter(latLng);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 3000);//3秒后执行TimeTask的run方法

                showMsg("坐标：" + latLonPoint.getLongitude()+"，"+latLonPoint.getLatitude());
            }

        } else {
            showMsg("获取坐标失败");
        }
    }
    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());//
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 初始化地图
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        mapView = getView().findViewById(R.id.map_view_fragment);
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();
        //设置最小缩放等级为16 ，缩放级别范围为[3, 20]
        aMap.setMinZoomLevel(16);
        //开启室内地图
        aMap.showIndoorMap(true);
        //实例化UiSettings类对象
        mUiSettings = aMap.getUiSettings();
        //隐藏缩放按钮 默认显示
        mUiSettings.setZoomControlsEnabled(false);
        //显示比例尺 默认不显示
        mUiSettings.setScaleControlsEnabled(true);
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        //设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        //地图点击监听
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMarkerDragListener(this);
        aMap.setInfoWindowAdapter(this);

        geocodeSearch = new GeocodeSearch(getActivity());
        geocodeSearch.setOnGeocodeSearchListener(this);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址

                String address = aMapLocation.getAddress();
                //获取纬度
                double latitude = aMapLocation.getLatitude();
                //获取经度
                double longitude = aMapLocation.getLongitude();
                Log.d(TAG, aMapLocation.getCity());
                Log.d(TAG,address);
                MyLatLng = new LatLng(latitude,longitude);
                //设置起点
                mStartPoint = convertToLatLonPoint(new LatLng(latitude, longitude));
                aMapLocation.getAccuracy();//获取精度信息

                //停止定位后，本地定位服务并不会被销毁
                mLocationClient.stopLocation();

                //显示地图定位结果
                if (mListener != null) {
                    if(searchTv.getText().equals("111")) {
                        GeocodeQuery query = new GeocodeQuery("温岭中学", "");
                        geocodeSearch.getFromLocationNameAsyn(query);
                    }else {
                        // 显示系统图标
                        mListener.onLocationChanged(aMapLocation);
                    }
                }

            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 点击地图
     */
    @Override
    public void onMapClick(LatLng latLng) {
//        //终点
//        mEndPoint = convertToLatLonPoint(latLng);
//        startRouteSearch();
        //添加标点
        updateMapCenter(latLng);
        this.aMap.clear();
        addMarker(latLng);
    }
    //地图点击事件为该地点的具体信息
    /**
     * 初始化路线
     */
    private void initRoute() {
        routeSearch = new RouteSearch(getActivity());
        routeSearch.setRouteSearchListener(this);
    }
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    /**
     * 步行规划路径结果
     *
     * @param walkRouteResult 结果
     * @param code            结果码
     */
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int code) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (code == AMapException.CODE_AMAP_SUCCESS) {
            if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                if (walkRouteResult.getPaths().size() > 0) {
                    final WalkPath walkPath = walkRouteResult.getPaths().get(0);
                    if (walkPath == null) {
                        return;
                    }
                    //绘制路线
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getActivity(), aMap, walkPath,
                            walkRouteResult.getStartPos(),
                            walkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                    int dis = (int) walkPath.getDistance();
                    int dur = (int) walkPath.getDuration();
                    String des = MapUtil.getFriendlyTime(dur) + "(" + MapUtil.getFriendlyLength(dis) + ")";
                    Log.d(TAG, des);

                } else if (walkRouteResult.getPaths() == null) {
                    showMsg("对不起，没有搜索到相关数据！");
                }
            } else {
                showMsg("对不起，没有搜索到相关数据！");
            }
        } else {
            showMsg("错误码；" + code);
        }
    }
    private void showMsg(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    /**
     * 开始路线搜索
     */
    private void startRouteSearch() {
        //在地图上添加起点Marker
        aMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        //在地图上添加终点Marker
        aMap.addMarker(new MarkerOptions()
                .position(convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));

        //搜索路线 构建路径的起终点
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        //构建步行路线搜索对象
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
        // 异步路径规划步行模式查询
        routeSearch.calculateWalkRouteAsyn(query);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}