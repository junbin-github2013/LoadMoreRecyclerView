package com.brooks.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.brooks.demo.dummy.DummyContent;
import com.brooks.loadmorerecyclerview.McLoadMoreRecyclerView;

/**
 * 下拉到底部自动加载更多
 */
public class McAutoLoadActivity extends AppCompatActivity {

    private McLoadMoreRecyclerView recyclerView;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mc_autoload);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AutoLoadUsePictures");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = (McLoadMoreRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(DummyContent.generateData(page));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadMoreEnable(DummyContent.hasMore(page));
        recyclerView.setLoadMoreListener(new McLoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore(int lastPosition) {
                DummyContent.DummyItem item = myItemRecyclerViewAdapter.getItemData(lastPosition);
                if (item != null) {
                    Toast.makeText(McAutoLoadActivity.this, "" + item.content, Toast.LENGTH_SHORT).show();
                }
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myItemRecyclerViewAdapter.addDatas(DummyContent.generateData(++page));
                        recyclerView.notifyMoreFinish(DummyContent.hasMore(page));
                    }
                }, 3000);
            }
        });
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }
}
