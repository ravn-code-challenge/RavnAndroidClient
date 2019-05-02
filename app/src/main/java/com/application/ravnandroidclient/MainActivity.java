package com.application.ravnandroidclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.application.ravnandroidclient.DetailActivity.AddActivity;
import com.application.ravnandroidclient.DetailActivity.UpdateActivity;
import com.application.ravnandroidclient.client.Client;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Client mClient = Client.getClient();
    Context mContext;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

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

        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.disconnect:
                        Log.d(TAG, "Disconnect called");
                        mClient.disconnect();
                        break;
                    case R.id.connect:
                        Log.d(TAG, "Connect called");
                        mClient.connect();
                        break;
                    case R.id.list_data:
                        Log.d(TAG, "List Data called");
                        mClient.list();
                        break;
                    case R.id.add_data:
                        Log.d(TAG, "Add data called");
                        startActivity(AddActivity.getUpdateIntent(mContext));
                        break;
                    case R.id.sort_data:
                        Log.d(TAG, "Sort Data");
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
