package com.ysh.mapdemo.newFragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ysh.mapdemo.Adapter.NormalAdapter;
import com.ysh.mapdemo.Bean.normalBean;
import com.ysh.mapdemo.FirstFragmentActivity;
import com.ysh.mapdemo.Interface.OnNormalItemClickListener;
import com.ysh.mapdemo.R;
import com.ysh.mapdemo.Room.PoiPlace;
import com.ysh.mapdemo.Room.PoiPlaceViewModel;
import com.ysh.mapdemo.newActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {
    private ConstraintLayout constraintLayout1,constraintLayout2;
    private ImageButton mIBtnMenu;
    private FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    private Switch aSwitch;
    NormalAdapter normalAdapter;
    NormalAdapter cardAdapter;
    PoiPlaceViewModel poiPlaceViewModel;
    private LiveData<List<PoiPlace>> filterdPoiPlaces;
    private List<PoiPlace> allPoiPlaces;
    private DividerItemDecoration dividerItemDecoration;
    private Button mBtnCancel,mBtnChange,mBtnClear;
    private Boolean isChange;

    public SecondFragment() {
        // Required empty public constructor
    }


    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        poiPlaceViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(PoiPlaceViewModel.class);
        floatingActionButton = getView().findViewById(R.id.floatingActionButton);
        recyclerView = getView().findViewById(R.id.normal_rv);
        normalAdapter = new NormalAdapter(false,poiPlaceViewModel);
        cardAdapter = new NormalAdapter(true,poiPlaceViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(normalAdapter);
        constraintLayout1 = getView().findViewById(R.id.second_cnt_1);
        constraintLayout2 = getView().findViewById(R.id.second_cnt_2);
        mIBtnMenu = getView().findViewById(R.id.second_ibtn_menu);
        constraintLayout2.setVisibility(View.GONE);
        mBtnCancel = getView().findViewById(R.id.second_setting1_cancel);
        mBtnChange = getView().findViewById(R.id.second_setting1_change);
        mBtnClear = getView().findViewById(R.id.second_setting1_clear);
        dividerItemDecoration = new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL);
        mIBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout1.setAlpha((float) 0.5);
                constraintLayout2.setVisibility(View.VISIBLE);
            }
        });
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    recyclerView.setAdapter(cardAdapter);
//                    recyclerView.removeItemDecoration(dividerItemDecoration);
//                }else{
//                    recyclerView.setAdapter(normalAdapter);
//                    recyclerView.addItemDecoration(dividerItemDecoration);
//                }
//            }
//        });

        filterdPoiPlaces = poiPlaceViewModel.getAllPoiPlaceLive();

        filterdPoiPlaces.observe(getViewLifecycleOwner(), new Observer<List<PoiPlace>>() {
            @Override
            public void onChanged(List<PoiPlace> poiPlaces) {
                int temp = normalAdapter.getItemCount();
                allPoiPlaces = poiPlaces;
//                normalAdapter.setAllPoiPlaces(poiPlaces);
//                cardAdapter.setAllPoiPlaces(poiPlaces);
                if(temp!=poiPlaces.size()) {
                    if(temp<poiPlaces.size()) {
                        recyclerView.smoothScrollBy(0, 200);
                    }
                    normalAdapter.submitList(poiPlaces);
                    cardAdapter.submitList(poiPlaces);
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout1.setAlpha((float) 1.0);
                constraintLayout2.setVisibility(View.GONE);
            }
        });
        isChange = false;
        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChange){
                    recyclerView.setAdapter(cardAdapter);
                    recyclerView.removeItemDecoration(dividerItemDecoration);
                    isChange=true;
                }else{
                    recyclerView.setAdapter(normalAdapter);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    isChange=false;
                }
                constraintLayout1.setAlpha((float) 1.0);
                constraintLayout2.setVisibility(View.GONE);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START|ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                PoiPlace poiFrom = allPoiPlaces.get(viewHolder.getAdapterPosition());
//                PoiPlace poiTo = allPoiPlaces.get(target.getAdapterPosition());
//                int idTemp = poiFrom.getId();
//                poiFrom.setId(poiTo.getId());
//                poiTo.setId(idTemp);
//                poiPlaceViewModel.updatePoiPlaces(poiFrom,poiTo);
//                normalAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
//                cardAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                PoiPlace poiPlaceDelete = allPoiPlaces.get(viewHolder.getAdapterPosition());
                poiPlaceViewModel.deletePoiPlaces(poiPlaceDelete);
                Snackbar.make(requireActivity().findViewById(R.id.second_frame),"删除了一个地址",Snackbar.LENGTH_SHORT)
            .setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poiPlaceViewModel.insertPoiPlaces(poiPlaceDelete);
                }
            }).show();
            }
            Drawable icon = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_delete_forever_24);
            Drawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight()-icon.getIntrinsicHeight())/2;
                int iconLeft,iconRight,iconTop,iconBottom;
                int backTop,backBottom,backLeft,backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
                iconBottom = iconTop +icon.getIntrinsicHeight();
                if(dX>0){
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft()+(int)dX;
                    background.setBounds(backLeft,backTop,backRight,backBottom);
                    iconLeft = itemView.getLeft()+iconMargin;
                    iconRight = iconLeft+icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
                }else if(dX<0){
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight()+(int)dX;
                    background.setBounds(backLeft,backTop,backRight,backBottom);
                    iconRight = itemView.getRight()-iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);
                }else{
                    background.setBounds(0,0,0,0);
                    icon.setBounds(0,0,0,0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(recyclerView);
        normalAdapter.setNormalItemClickListener(new OnNormalItemClickListener() {
            @Override
            public void onItemClick(int Position) {
                Intent intent = new Intent(requireActivity(), newActivity.class);
                normalBean.lat = filterdPoiPlaces.getValue().get(Position).getLat();
                normalBean.lon = filterdPoiPlaces.getValue().get(Position).getLon();
                startActivity(intent);
            }
        });
        cardAdapter.setNormalItemClickListener(new OnNormalItemClickListener() {
            @Override
            public void onItemClick(int Position) {
                Intent intent = new Intent(requireActivity(), newActivity.class);
                normalBean.lat = filterdPoiPlaces.getValue().get(Position).getLat();
                normalBean.lon = filterdPoiPlaces.getValue().get(Position).getLon();
                startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poiPlaceViewModel.insertPoiPlaces(new PoiPlace("标题一","地址一",0,0));
                poiPlaceViewModel.insertPoiPlaces(new PoiPlace("标题二","地址二",0,0));
                poiPlaceViewModel.insertPoiPlaces(new PoiPlace("标题三","地址三",0,0));
            }
        });

    }

}