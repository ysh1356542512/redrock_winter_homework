package com.ysh.mapdemo.newFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.ysh.mapdemo.Adapter.thirdAdapter;
import com.ysh.mapdemo.Bean.third_bean;
import com.ysh.mapdemo.R;

import java.util.ArrayList;
import java.util.List;


public class ThirdFragment extends Fragment {

    private RecyclerView mRvWhHor;
    private View mView1,mView2;
    private List<third_bean> list = new ArrayList<>();

    public ThirdFragment() {
        // Required empty public constructor
    }


    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
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
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initRecyclerView();
    }

    public void initData(){
        third_bean shoucang = new third_bean(R.drawable.third_forth_home,"我的家");
        third_bean mycar = new third_bean(R.drawable.third_forth_mycar,"我的车辆");
        third_bean company = new third_bean(R.drawable.third_forth_company,"我的公司");
        third_bean houuse = new third_bean(R.drawable.third_forth_house,"我的店铺");
        third_bean share = new third_bean(R.drawable.third_forth_share,"位置共享");
        third_bean give = new third_bean(R.drawable.third_forth_give,"我的贡献");
        third_bean interest = new third_bean(R.drawable.third_forth_interest,"高德趣行");
        third_bean recmd = new third_bean(R.drawable.third_forth_recmd,"评论");
        third_bean fankui = new third_bean(R.drawable.third_forth_fankui,"我的反馈");
        third_bean more = new third_bean(R.drawable.third_forth_more,"更多工具");
        list.add(shoucang);
        list.add(mycar);
        list.add(company);
        list.add(houuse);
        list.add(share);
        list.add(give);
        list.add(interest);
        list.add(recmd);
        list.add(fankui);
        list.add(more);
    }

    public void initRecyclerView(){
        mView1 = getView().findViewById(R.id.icon_bg);
        mView2 = getView().findViewById(R.id.main_line);
        mRvWhHor = getView().findViewById(R.id.third_forth_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParentFragment().getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvWhHor.setLayoutManager(linearLayoutManager);
        thirdAdapter thirdAdapter = new thirdAdapter(getContext(),list);
        mRvWhHor.setAdapter(thirdAdapter);

        setScrollBar();
    }
    private void setScrollBar() {

        mRvWhHor.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //当前RcyclerView显示区域的高度。水平列表屏幕从左侧到右侧显示范围
                int extent = recyclerView.computeHorizontalScrollExtent();
                //整体的高度，注意是整体，包括在显示区域之外的。
                int range = recyclerView.computeHorizontalScrollRange();

                //已经滚动的距离，为0时表示已处于顶部。
                int offset = recyclerView.computeHorizontalScrollOffset();
                if (extent==range){
                    mView2.setVisibility(View.GONE);
                }else {
                    mView2.setVisibility(View.VISIBLE);
                    //计算出溢出部分的宽度，即屏幕外剩下的宽度
                    float maxEndX = range - extent;
                    //计算比例
                    float proportion = offset / maxEndX;
                    int layoutWidth = mView1.getWidth();
                    int indicatorViewWidth = mView2.getWidth();
                    //可滑动的距离
                    int scrollableDistance = layoutWidth - indicatorViewWidth;
                    //设置滚动条移动
                    mView2.setTranslationX(scrollableDistance * proportion);
                }
            }

        });
    }
}