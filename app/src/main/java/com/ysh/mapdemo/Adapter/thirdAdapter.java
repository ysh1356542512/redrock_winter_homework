package com.ysh.mapdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.poisearch.PoiSearch;
import com.ysh.mapdemo.Bean.third_bean;
import com.ysh.mapdemo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class thirdAdapter extends RecyclerView.Adapter<thirdAdapter.MyViewHolder> {
    List<third_bean> allWords = new ArrayList<>();
    private Context mContext;
    public void setAllWords(List<third_bean> allWords){
        this.allWords = allWords;
    }

    public thirdAdapter(Context context,List<third_bean> list){
        this.mContext = context;
        this.allWords = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_third_rv,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        third_bean third_bean = allWords.get(position);
        holder.imageView.setImageResource(third_bean.getId());
        holder.textView.setText(third_bean.getText());
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.third_iv);
            textView = itemView.findViewById(R.id.third_tv);
        }
    }
}
