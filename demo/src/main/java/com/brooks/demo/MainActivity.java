package com.brooks.demo;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.brooks.demo.dummy.DummyContent;
public class MainActivity extends AppCompatActivity{
    private AppCompatButton bt_autoload;
    private AppCompatButton bt_manualload;
    private AppCompatEditText items;
    private AppCompatEditText pages;
    private AppCompatButton bt_user_pic;
    private AppCompatButton bt_mc_auto;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("LoadMoreRecyclerView");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        bt_autoload=(AppCompatButton)findViewById(R.id.bt_autoload);
        bt_manualload=(AppCompatButton)findViewById(R.id.bt_manualload);
        bt_user_pic=(AppCompatButton)findViewById(R.id.bt_use_pic);
        bt_mc_auto=(AppCompatButton)findViewById(R.id.bt_mc_auto);
        items=(AppCompatEditText)findViewById(R.id.items);
        pages=(AppCompatEditText)findViewById(R.id.pages);
        bt_autoload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String itemsText=items.getText().toString();
                String pagesText=pages.getText().toString();
                DummyContent dummyContent=new DummyContent();
                try {
                    dummyContent.setData(Integer.parseInt(itemsText),Integer.parseInt(pagesText));
                    startActivity(new Intent(MainActivity.this,AutoLoadActivity.class));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"数据不合法",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_manualload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String itemsText=items.getText().toString();
                String pagesText=pages.getText().toString();
                DummyContent dummyContent=new DummyContent();
                try {
                    dummyContent.setData(Integer.parseInt(itemsText),Integer.parseInt(pagesText));
                    startActivity(new Intent(MainActivity.this,ManualloadActivity.class));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"数据不合法",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_user_pic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String itemsText=items.getText().toString();
                String pagesText=pages.getText().toString();
                DummyContent dummyContent=new DummyContent();
                try {
                    dummyContent.setData(Integer.parseInt(itemsText),Integer.parseInt(pagesText));
                    startActivity(new Intent(MainActivity.this,AutoLoadUsePicActivity.class));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"数据不合法",Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_mc_auto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String itemsText=items.getText().toString();
                String pagesText=pages.getText().toString();
                DummyContent dummyContent=new DummyContent();
                try {
                    dummyContent.setData(Integer.parseInt(itemsText),Integer.parseInt(pagesText));
                    startActivity(new Intent(MainActivity.this,McAutoLoadActivity.class));
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"数据不合法",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
