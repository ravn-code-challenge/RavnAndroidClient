package com.application.ravnandroidclient.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.GiphyModel;

import java.util.Date;


public class EditActivity extends AppCompatActivity {



    private static final String UPDATE_OR_ADD_KEY = "update_or_add";

    protected ImageView mIvGiphy;

    protected ProgressBar mProgressBar;

    protected TextInputLayout mTlTitle;
    protected TextInputLayout mTlAuthor;
    protected TextInputLayout mTlGiphySrc;

    protected TextInputEditText mEtTitle;
    protected TextInputEditText mEtAuthor;
    protected TextInputEditText mEtGiphySrc;

    protected TextView mTvViews;
    protected TextView mTvDate;
    protected Button mBtUpdate;
    protected Button mBtDelete;

    protected Context mContext;


    public static Intent getAddIntent(Context context) {
        Intent intent = new Intent(context, EditActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =  this;
        setContentView(R.layout.activity_detail);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.INVISIBLE);

        mIvGiphy = findViewById(R.id.iv_giphy);

        mTlTitle = findViewById(R.id.tl_title);
        mTlAuthor = findViewById(R.id.tl_author);
        mTlGiphySrc = findViewById(R.id.tl_src);

        mEtTitle = findViewById(R.id.et_title);
        mEtAuthor = findViewById(R.id.et_author);
        mEtGiphySrc = findViewById(R.id.et_src);
        mBtUpdate = findViewById(R.id.bt_update);
        mBtDelete = findViewById(R.id.bt_delete);
        mTvViews = findViewById(R.id.tv_views);
        mTvDate = findViewById(R.id.tv_date);


    }

    /**
     * If a valid model exists from fields it will return it.
     * If no valid model exists, it will return null
     * Meant to be called by children
     * @return
     */
    protected GiphyModel getValidModelFromFields() {
        GiphyModel model = new GiphyModel();

        model.title =  (mEtTitle.getText() != null) ?  mEtTitle.getText().toString() : null;
        model.author =  (mEtAuthor.getText() != null) ?  mEtAuthor.getText().toString() : null;
        model.src = (mEtGiphySrc.getText() != null) ? mEtGiphySrc.getText().toString() : null;


        boolean isValid = true;

        if(model.title == null || model.title.isEmpty()) {
            mTlTitle.setError("Title cannot be blank");
            isValid = false;
        }
        if(model.author == null || model.author.isEmpty()) {
            mTlAuthor.setError("Author cannot be blank");
            isValid = false;
        }
        if(model.src == null || model.src.isEmpty()) {
            mTlGiphySrc.setError("Cannot have empty giphy");
            isValid = false;
        }

        if(isValid) {
            //Timestamp and return
            model.date = new Date();
            model.viewOrder = -1;
            return model;

        }
        else {
            return null;
        }
    }
}
