package com.ysh.mapdemo.Interface;

import com.ysh.mapdemo.Bean.AddressBean;

import java.util.ArrayList;
import java.util.List;

public interface OnRecyclerItemClickListener {
    //RecyclerView的点击事件，将信息回调给view
    void onItemClick(int Position, ArrayList<AddressBean> dataBeanList);

}
