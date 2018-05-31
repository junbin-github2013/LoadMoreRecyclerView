package com.brooks.demo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brooks.demo.dummy.DummyContent;

import java.util.List;
/**
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DummyContent.DummyItem> mValues;
    public MyItemRecyclerViewAdapter(List<DummyContent.DummyItem> items){
        mValues=items;
    }
    public void setData(List<DummyContent.DummyItem> datas){
        mValues=datas;
    }
    public void addDatas(List<DummyContent.DummyItem> datas){
        mValues.addAll(datas);
    }

    public DummyContent.DummyItem getItemData(int position) {
        if (position < mValues.size()) {
            return mValues.get(position);
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
        ViewHolder mHolder=(ViewHolder)holder;
        mHolder.mItem=mValues.get(position);
        mHolder.mContentView.setText(mValues.get(position).content);
    }
    @Override
    public int getItemCount(){
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;
        public TextView mContentView;
        public DummyContent.DummyItem mItem;
        public ViewHolder(View view){
            super(view);
            mView=view;
            mContentView=(TextView)view.findViewById(R.id.content);
        }
        @Override
        public String toString(){
            return mContentView.getText().toString();
        }
    }
}
