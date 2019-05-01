package com.application.ravnandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.application.ravnandroidclient.client.GiphyModel;

public class DetailActivity extends AppCompatActivity {

    public static Intent getUpdateIntent(Context context) {
        return new Intent(context, DetailActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        GiphyModel model = new GiphyModel();
        model.id = 1;
    }
}
