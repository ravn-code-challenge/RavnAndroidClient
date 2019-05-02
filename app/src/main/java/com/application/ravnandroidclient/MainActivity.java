package com.application.ravnandroidclient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.application.ravnandroidclient.DetailActivity.AddActivity;
import com.application.ravnandroidclient.DetailActivity.UpdateActivity;
import com.application.ravnandroidclient.client.Client;

public class MainActivity extends AppCompatActivity {

    Client mClient = new Client();
    Context mContext;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.disconnect();
            }
        });

        findViewById(R.id.bt_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.connect();
            }
        });

        findViewById(R.id.bt_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClient.list();
            }
        });
        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AddActivity.getUpdateIntent(mContext));
            }
        });
        findViewById(R.id.bt_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(UpdateActivity.getUpdateIntent(mContext));
            }
        });
        findViewById(R.id.bt_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }
}
