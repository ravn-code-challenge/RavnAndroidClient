package com.application.ravnandroidclient.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import com.application.ravnandroidclient.GlideApp;
import com.application.ravnandroidclient.client.Client;
import com.application.ravnandroidclient.client.GiphyModel;

import java.util.Calendar;
import java.util.Locale;

public class UpdateActivity extends EditActivity {

    private static final String TAG = "UpdateActivity";

    GiphyModel model;
    private static final String GIPHY_ID_KEY = "giphy_id";
    DeleteAsyncTask mDeleteAsyncTask;
    UpdateAsyncTask mUpdateAsyncTask;


    public static Intent getIntent(Context context, long id) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra(GIPHY_ID_KEY, id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mUpdateAsyncTask == null) {
                    GiphyModel modelFromFields =  getValidModelFromFields();
                    model.title = modelFromFields.title;
                    model.author = modelFromFields.author;
                    model.src = modelFromFields.src;
                    mUpdateAsyncTask = new UpdateAsyncTask(model);
                    mUpdateAsyncTask.execute();
                }

            }
        });

        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDeleteAsyncTask == null) {
                    mDeleteAsyncTask = new DeleteAsyncTask(model.id);
                    mDeleteAsyncTask.execute();
                }
            }
        });

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();


        model = Client.getClient().getModel(getIntent().getLongExtra(GIPHY_ID_KEY, 1));
        Log.d(TAG, "Model id: " + model.id);

        GlideApp.with(this)
                .load(model.src)
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(mIvGiphy);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(model.date);
        String date = DateFormat.format("MM/dd/yy", cal).toString();

        mTvViews.setText("views: " + Long.toString(model.viewCount));
        mTvDate.setText("Updated: " + date);
        mEtTitle.setText(model.title);
        mEtAuthor.setText(model.author);
        mEtGiphySrc.setText(model.src);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mUpdateAsyncTask != null) mUpdateAsyncTask.cancel(true );
        if(mDeleteAsyncTask != null) mDeleteAsyncTask.cancel(true );
    }

    public void finishTask() {
        finish();
    }

    class UpdateAsyncTask extends AsyncTask<Void, Void, Boolean> {

        GiphyModel mGiphyModel;

        UpdateAsyncTask(GiphyModel giphyModel) {
            mGiphyModel = giphyModel;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return Client.getClient().update(mGiphyModel);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            finishTask();
        }
    }

    class DeleteAsyncTask extends AsyncTask<Void, Void, Boolean> {

        long giphyId;

        DeleteAsyncTask(long giphyId) {
            this.giphyId = giphyId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "Deleting id: " + giphyId);
            return Client.getClient().remove(giphyId);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                finishTask();
            }
        }
    }
}
