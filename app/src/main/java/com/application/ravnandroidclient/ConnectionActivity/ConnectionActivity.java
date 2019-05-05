package com.application.ravnandroidclient.ConnectionActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.ClientApi;
import com.application.ravnandroidclient.client.ClientPush;

public class ConnectionActivity extends AppCompatActivity {



    ConnectionTask mConnectionTask;
    ConnectAsyncTask mConnectAsyncTask;
    EditText mEtConnection;
    Button btConnection;
    Button btCancel;

    public static Intent getIntent(Context context) {
        return new Intent(context, ConnectionActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtConnection = findViewById(R.id.et_connection);
        mEtConnection.setText(ClientApi.getClient().currentAddress);

        btConnection = findViewById(R.id.bt_connect);
        btConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = mEtConnection.getText().toString();
                mConnectAsyncTask = new ConnectAsyncTask(address);
                mConnectAsyncTask.execute();

            }
        });

        btCancel = findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mConnectAsyncTask != null) mConnectAsyncTask.cancel(true);
    }

    void toastUser(String resultMessage) {
        if(resultMessage != null) {
            Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class ConnectAsyncTask extends AsyncTask<Void, Void, String> {

        String ipAddress;

        public ConnectAsyncTask(String ipAddress) {
            this.ipAddress = ipAddress;
        }


        @Override
        protected String doInBackground(Void... voids) {
            String apiResult = ClientApi.getClient().connect(ipAddress);
            String pushResult = ClientPush.getClient().connect(ipAddress);

            if(apiResult != null) return apiResult;
            if(pushResult != null) return pushResult;
            return null;
        }

        @Override
        protected void onPostExecute(String resultText) {
            toastUser(resultText);
            if(resultText == null) {
                finish();
            }

        }
    }



}
