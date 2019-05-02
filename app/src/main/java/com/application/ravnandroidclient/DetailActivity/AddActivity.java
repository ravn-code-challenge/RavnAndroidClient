package com.application.ravnandroidclient.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.application.ravnandroidclient.GlideApp;
import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.Client;
import com.application.ravnandroidclient.client.GiphyModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends EditActivity {

    public static Intent getUpdateIntent(Context context) {
        return new Intent(context, AddActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AppCompatActivity appCompatActivity = this;

        mIvGiphy.setVisibility(View.GONE);
        findViewById(R.id.frame_giphy).setVisibility(View.GONE);
        mTvDate.setVisibility(View.GONE);
        mTvViews.setVisibility(View.GONE);

        mBtUpdate.setText("Add");
        mBtDelete.setText("Cancel");

        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiphyModel model =  getValidModelFromFields();
                if(model != null) {

                    //Since we're adding, we should ensure viewcount is now zero
                    model.viewCount = 0;
                    new AddAsyncTask(model).execute();
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


    class AddAsyncTask extends AsyncTask<Void, Void, Boolean> {

        GiphyModel mGiphyModel;

        AddAsyncTask(GiphyModel giphyModel) {
            mGiphyModel = giphyModel;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return Client.getClient().add(mGiphyModel);
        }

        @Override
        protected void onPostExecute(Boolean result) {

        }
    }

}
