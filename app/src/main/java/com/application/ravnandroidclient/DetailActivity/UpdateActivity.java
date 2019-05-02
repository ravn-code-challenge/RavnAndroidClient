package com.application.ravnandroidclient.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.text.format.DateFormat;
import android.view.View;

import com.application.ravnandroidclient.GlideApp;
import com.application.ravnandroidclient.client.GiphyModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateActivity extends EditActivity {

    public static Intent getUpdateIntent(Context context) {
        Intent intent = new Intent(context, UpdateActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        GiphyModel model = new GiphyModel();
        model.id = 1;
        model.type = "GIF";
        model.title = "Aquaman old chum";
        model.author = "Stan Lee";
        model.src = "https://media0.giphy.com/media/Hz6upbrOw14sM/200w.gif";
        model.date = new Date();
        model.viewCount = 0;
        model.viewOrder = -1;

        GlideApp.with(this)
                .load("https://media0.giphy.com/media/Hz6upbrOw14sM/200w.gif")
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(mIvGiphy);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTime(model.date);
        String date = DateFormat.format("MM/dd/yy", cal).toString();

        mTvViews.setText("views: " + Integer.toString(model.viewCount));
        mTvDate.setText("Updated: " + date);
        mEtTitle.setText(model.title);
        mEtAuthor.setText(model.author);
        mEtGiphySrc.setText(model.src);


    }
}
