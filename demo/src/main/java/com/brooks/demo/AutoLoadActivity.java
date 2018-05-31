package com.brooks.demo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.brooks.demo.dummy.DummyContent;
import com.brooks.loadmorerecyclerview.LoadMoreRecyclerView;
public class AutoLoadActivity extends AppCompatActivity{

    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private int page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoload);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("AutoLoad");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //加载RecyclerView
        loadRecyclerView();
        //加载swipeRefreshLayout
        loadRefreshLayout();
    }
    private void loadRecyclerView(){
        recyclerView=(LoadMoreRecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        myItemRecyclerViewAdapter=new MyItemRecyclerViewAdapter(DummyContent.generateData(page));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadMoreEnable(DummyContent.hasMore(page));
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener(){
            @Override
            public void onLoadMore(){
                recyclerView.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        swipeRefreshLayout.setRefreshing(false);
                        myItemRecyclerViewAdapter.addDatas(DummyContent.generateData(++page));
                        recyclerView.notifyMoreFinish(DummyContent.hasMore(page));
                    }
                },1000);
            }
        });
        myItemRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void loadRefreshLayout(){
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_color1,R.color.refresh_color2,
                R.color.refresh_color3,R.color.refresh_color4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                page=0;
                myItemRecyclerViewAdapter=new MyItemRecyclerViewAdapter(DummyContent.generateData(page));
                recyclerView.setAdapter(myItemRecyclerViewAdapter);
                recyclerView.setLoadMoreEnable(DummyContent.hasMore(page));
                recyclerView.setLoadingMore(false);
                myItemRecyclerViewAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
