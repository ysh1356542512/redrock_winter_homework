package com.ysh.mapdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.ysh.mapdemo.Adapter.MapAdapter;
import com.ysh.mapdemo.Bean.AddressBean;
import com.ysh.mapdemo.Bean.normalBean;
import com.ysh.mapdemo.Interface.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class FirstFragmentActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    private AutoCompleteTextView mEt_keyword;
    private RecyclerView listView;
    private Button mBtnSearch;
    private ImageView mIvBack;

    private static final int REQUEST_PERMISSION_LOCATION = 0;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch       poiSearch;// POI搜索

    private List<AddressBean> mData = new ArrayList<>();
    private MapAdapter listAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_fragment);
        mEt_keyword = findViewById(R.id.forth_atCtv1);
        mIvBack = findViewById(R.id.return_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                Intent intent = new Intent(getApplicationContext(),newActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("data", mEt_keyword.getText().toString());
                intent.putExtra("index",bundle);
                normalBean.str = mEt_keyword.getText().toString();
                startActivity(intent);
            }
        });
        mBtnSearch = findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                Intent intent = new Intent(getApplicationContext(),newActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("data", mEt_keyword.getText().toString());
                intent.putExtra("index",bundle);
                normalBean.str = mEt_keyword.getText().toString();
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.forth_rv1);
        listView.setLayoutManager(new LinearLayoutManager(this));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION);

        } else {
            //监听EditText输入
            initListener();
        }
    }
    private void initListener() {
        mEt_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
                    Toast.makeText(getApplicationContext(),"请输入",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    doSearchQuery(keyWord);
                    mEt_keyword.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEt_keyword,0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String key) {
        currentPage = 0;
        //不输入城市名称有些地方搜索不到
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(key, "", "");
        // 设置每页最多返回多少条poiitem
        query.setPageSize(10);
        // 设置查询页码
        query.setPageNum(currentPage);

        //构造 PoiSearch 对象，并设置监听
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
        poiSearch.searchPOIAsyn();
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        //rCode 为1000 时成功,其他为失败
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            // 解析result   获取搜索poi的结果
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    ArrayList<AddressBean> data = new ArrayList<AddressBean>();//自己创建的数据集合
                    // 取得第一页的poiitem数据，页数从数字0开始
                    //poiResult.getPois()可以获取到PoiItem列表
                    List<PoiItem> poiItems = poiResult.getPois();

                    //若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
                    //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
                    List<String> suggestionKeywords =  poiResult.getSearchSuggestionKeywords();

                    //解析获取到的PoiItem列表
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();
                        //返回POI的名称
                        String title = item.getTitle();
                        //返回POI的地址
                        String text = item.getSnippet();
                        data.add(new AddressBean(lon, lat, title, text));
                    }
                    listAdapter = new MapAdapter(this, data);
                    listView.setAdapter(listAdapter);
                    listAdapter.setRecyclerItemClickListener(new OnRecyclerItemClickListener() {
                        @Override
                        public void onItemClick(int Position, ArrayList<AddressBean> dataBeanList) {
                            Log.d("String","123");
                            mEt_keyword.setText(dataBeanList.get(Position).getTitle()+dataBeanList.get(Position).getText());
                        }
                    });
                }
            } else {
                Toast.makeText(this,"无搜索结果",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"错误码"+rCode,Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {

    }
}