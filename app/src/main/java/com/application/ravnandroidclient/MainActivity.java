package com.application.ravnandroidclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Client mClient = new Client();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

            }
        });
        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.bt_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.bt_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.bt_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
