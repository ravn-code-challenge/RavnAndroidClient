package com.application.ravnandroidclient.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.ClientApi;
import com.application.ravnandroidclient.client.GiphyModel;

public class AddActivity extends EditActivity {

    private static final String TAG = "AddActivity";

    AppCompatActivity appCompatActivity;
    AddAsyncTask mAddAsyncTask;

    public static Intent getIntent(Context context) {
        return new Intent(context, AddActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appCompatActivity = this;

        mIvGiphy.setVisibility(View.GONE);
        findViewById(R.id.frame_giphy).setVisibility(View.GONE);
        mTvDate.setVisibility(View.GONE);
        mTvViews.setVisibility(View.GONE);

        mBtUpdate.setText("Add");
        mBtDelete.setText("Cancel");

        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add button clicked");
                GiphyModel model =  getValidModelFromFields();
                if(model != null) {
                    //Since we're adding, we should ensure viewcount is now zero
                    model.viewCount = 0;
                    mAddAsyncTask = new AddAsyncTask(model);
                    mAddAsyncTask.execute();
                }

            }
        });

        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appCompatActivity.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAddAsyncTask != null) {
            mAddAsyncTask.cancel(true);
        }
    }


    class AddAsyncTask extends AsyncTask<Void, Void, String> {

        GiphyModel mGiphyModel;

        AddAsyncTask(GiphyModel giphyModel) {
            mGiphyModel = giphyModel;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return ClientApi.getClient().add(mGiphyModel);
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                toastUser(result);
                mAddAsyncTask = null;
            }
            else {
                finishActivityFromAsyncTask();
            }
        }
    }
}
