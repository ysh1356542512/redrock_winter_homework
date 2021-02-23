package com.ysh.mapdemo.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ysh.mapdemo.Interface.OnNormalItemClickListener;
import com.ysh.mapdemo.Interface.OnRecyclerItemClickListener;
import com.ysh.mapdemo.R;
import com.ysh.mapdemo.Room.PoiPlace;
import com.ysh.mapdemo.Room.PoiPlaceViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NormalAdapter extends ListAdapter<PoiPlace,NormalAdapter.NormalViewHolder> {
//    List<PoiPlace> allPoiPlaces = new ArrayList<>();
    boolean useCardView;
    private PoiPlaceViewModel poiPlaceViewModel;
    private OnNormalItemClickListener monItemClickListener;

    //提供set方法供Activity或Fragment调用
    public void setNormalItemClickListener(OnNormalItemClickListener listener){
        monItemClickListener=listener;
    }

    public NormalAdapter(boolean useCardView, PoiPlaceViewModel poiPlaceViewModel){
        super(new DiffUtil.ItemCallback<PoiPlace>() {
            @Override
            public boolean areItemsTheSame(@NonNull PoiPlace oldItem, @NonNull PoiPlace newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull PoiPlace oldItem, @NonNull PoiPlace newItem) {
                return (oldItem.getAddress().equals(newItem.getAddress())
                &&oldItem.getTitle().equals((newItem.getTitle()))
                &&oldItem.getLat()==newItem.getLat()
                &&oldItem.getLon()==newItem.getLon());
            }
        });
        this.useCardView = useCardView;
        this.poiPlaceViewModel = poiPlaceViewModel;
    }
//    public void setAllPoiPlaces(List<PoiPlace> allPoiPlaces){
//        this.allPoiPlaces = allPoiPlaces;
//    }

    @NonNull
    @Override
    public NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if(useCardView){
            itemView = layoutInflater.inflate(R.layout.item_card_2,parent,false);
        }else {
            itemView = layoutInflater.inflate(R.layout.item_normal_2, parent, false);
        }
        NormalViewHolder holder = new NormalViewHolder(itemView);
        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PoiPlace poiPlace = (PoiPlace)holder.itemView.getTag(R.id.word_for_view_holder);
                if(isChecked){
                    holder.mTv3.setVisibility(View.GONE);
                    poiPlace.setLatInvisible(true);
                    poiPlaceViewModel.updatePoiPlaces(poiPlace);
                }else{
                    holder.mTv3.setVisibility(View.VISIBLE);
                    poiPlace.setLatInvisible(false);
                    poiPlaceViewModel.updatePoiPlaces(poiPlace);
                }
            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NormalViewHolder holder, int position) {
//        if(position>=2) {
            PoiPlace poiPlace = getItem(position);
        double qianmifload = poiPlace.getLat();
        qianmifload = BigDecimal.valueOf(qianmifload)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .doubleValue();
        double qianmifload2 = poiPlace.getLon();
        qianmifload2 = BigDecimal.valueOf(qianmifload2)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .doubleValue();
            holder.itemView.setTag(R.id.word_for_view_holder,poiPlace);
            holder.mIv1.setImageResource(R.drawable.first_icon_star);
            holder.mTv2.setText(poiPlace.getAddress());
            holder.mTv3.setText("经度:"+qianmifload+","+"纬度:"+qianmifload2);
            if(poiPlace.isLatInvisible()){
                holder.mTv3.setVisibility(View.GONE);
                holder.aSwitch.setChecked(true);
            }else{
                holder.mTv3.setVisibility(View.VISIBLE);
                holder.aSwitch.setChecked(false);
            }
//        }

    }

//    @Override
//    public int getItemCount() {
//        return allPoiPlaces.size();
//    }

     public class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView mTv1,mTv2,mTv3;
        ImageView mIv1;
        Switch aSwitch;
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
//            mTv1 = itemView.findViewById(R.id.normal_number);
            mTv2 = itemView.findViewById(R.id.normal_tv1);
            mTv3 = itemView.findViewById(R.id.normal_tv2);
            mIv1 = itemView.findViewById(R.id.normal_number);
            aSwitch = itemView.findViewById(R.id.LatInvisiable);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monItemClickListener!=null){

                        monItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
