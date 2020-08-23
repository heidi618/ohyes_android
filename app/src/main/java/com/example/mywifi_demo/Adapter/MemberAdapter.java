package com.example.mywifi_demo.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mywifi_demo.Bean.MemberBean;

import java.util.List;

public class MemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<MemberBean> mMemberList;

    public MemberAdapter(Context context, List<MemberBean> memberList){
        mContext = context;
        mMemberList = memberList;
    }

    @Override
    public int getCount() {
        return mMemberList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMemberList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
