package com.ysh.mapdemo.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ysh.mapdemo.Bean.AddressBean;
import com.ysh.mapdemo.Interface.OnRecyclerItemClickListener;
import com.ysh.mapdemo.R;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.ViewHolder>{
    private Activity mActivity;
    private ArrayList<AddressBean> list;

    public MapAdapter(Activity activity, ArrayList<AddressBean> data) {
        this.mActivity = activity;
        this.list = data;
    }
    //声明自定义的监听接口
    private OnRecyclerItemClickListener monItemClickListener;

    //提供set方法供Activity或Fragment调用
    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        monItemClickListener=listener;
    }
//    public static interface OnItemClickListener {
//        void onItemClick(View view,int position);
//    }

//    protected OnItemClickListener mItemClickListener;

//    public interface OnItemClickListener {
//        void onItemClick(String name,String content);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mItemClickListener = listener;
//    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.map_item, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (list.size() !=0){
            viewHolder.mMapname.setText(list.get(i).getTitle());
            viewHolder.mMapcontent.setText(list.get(i).getText());
        }
    }
    /**
     * 设置数据集
     * @param data
     */
    public void setData(ArrayList<AddressBean> data){
        this.list = data;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mMapname,mMapcontent;
        private final RelativeLayout relay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relay = itemView.findViewById(R.id.relay);
            mMapname = itemView.findViewById(R.id.map_title);
            mMapcontent = itemView.findViewById(R.id.map_content);
//            将监听传递给自定义接口
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monItemClickListener!=null){

                        monItemClickListener.onItemClick(getAdapterPosition(),list);
                    }
                }
            });
        }
    }
}
