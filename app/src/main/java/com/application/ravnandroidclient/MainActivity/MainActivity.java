package com.application.ravnandroidclient.MainActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.application.ravnandroidclient.DetailActivity.AddActivity;
import com.application.ravnandroidclient.DetailActivity.UpdateActivity;
import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.SortActivity;
import com.application.ravnandroidclient.client.ClientApi;
import com.application.ravnandroidclient.client.ClientPush;
import com.application.ravnandroidclient.client.ClientSubscriber;
import com.application.ravnandroidclient.client.GiphyList;
import com.application.ravnandroidclient.client.GiphyModel;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ClientSubscriber {

    private static final String TAG = "MainActivity";

    ClientApi mClient = ClientApi.getClient();
    Context mContext;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private RecyclerView mRecyclerView;
    private GiphyAdapter mGiphyAdapter;

    ConnectAsyncTask mConnectAsyncTask;
    DisconnectAsyncTask mDisconnectAsyncTask;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.disconnect();
        mClient.removeSubscriber(this);
        if(mConnectAsyncTask != null) mConnectAsyncTask.cancel(true);
        if(mDisconnectAsyncTask != null) mDisconnectAsyncTask.cancel(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mClient.registerSubscriber(this);

        dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();


        mRecyclerView = findViewById(R.id.giphy_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mGiphyAdapter = new GiphyAdapter(this, new GiphyClickedListener() {
            @Override
            public void onGiphyClicked(GiphyModel model) {
                startActivity(UpdateActivity.getIntent(mContext, model.id));
            }
        });
        mRecyclerView.setAdapter(mGiphyAdapter);

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
                        mDisconnectAsyncTask = new DisconnectAsyncTask();
                        mDisconnectAsyncTask.execute();
                        break;
                    case R.id.connect:
                        Log.d(TAG, "Connect called");
                        mConnectAsyncTask = new ConnectAsyncTask();
                        mConnectAsyncTask.execute();
                        break;
                    case R.id.list_data:
                        Log.d(TAG, "List Data called");
                        mClient.list();
                        break;
                    case R.id.add_data:
                        Log.d(TAG, "Add data called");
                        startActivity(AddActivity.getIntent(mContext));
                        break;
                    case R.id.sort_data:
                        Log.d(TAG, "Sort Data");
                        startActivity(SortActivity.getIntent(mContext));
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

    @Override
    public void updateGiphyModels(GiphyList newList) {
        Log.d(TAG, "We got new list in Mainactiity");
        if(mGiphyAdapter != null) {
            mGiphyAdapter.updateList(newList);
        }
    }

    void toastUser(String resultMessage) {
        if(resultMessage != null) {
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        }
    }

    class ConnectAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiResult = ClientApi.getClient().connect();
            String pushResult = ClientPush.getClient().connect();

            if(apiResult != null) return apiResult;
            if(pushResult != null) return pushResult;
            return null;
        }

        @Override
        protected void onPostExecute(String resultText) {
            toastUser(resultText);

        }
    }


    class DisconnectAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String apiResult = ClientApi.getClient().disconnect();
            String pushResult = ClientPush.getClient().disconnect();

            if(apiResult != null) return apiResult;
            if(pushResult != null) return pushResult;
            return null;
        }

        @Override
        protected void onPostExecute(String resultText) {
            toastUser(resultText);

        }
    }
}
