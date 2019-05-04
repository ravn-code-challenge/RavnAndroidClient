package com.application.ravnandroidclient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.application.ravnandroidclient.client.ClientApi;
import com.application.ravnandroidclient.client.GiphyList;


public class SortActivity extends AppCompatActivity {

    Spinner fieldSpinner;
    Spinner sortTypeSpinner;
    SortAsyncTask mSortAsyncTask;

    public static Intent getIntent(Context context) {
        return new Intent(context, SortActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpFieldAdapter();
        setUpSortAdapter();

        Button sortButton = findViewById(R.id.bt_run_sort);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSortAsyncTask = new SortAsyncTask(fieldSpinner.getSelectedItemPosition(),
                        sortTypeSpinner.getSelectedItemPosition());
                mSortAsyncTask.execute();
            }
        });

        Button cancelButton = findViewById(R.id.bt_sort_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setUpFieldAdapter() {
        fieldSpinner = findViewById(R.id.spinner_field);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_fields, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter);
        fieldSpinner.setSelection(ClientApi.getClient().mGiphyList.getSortType().ordinal());
    }

    public void setUpSortAdapter() {
        sortTypeSpinner = findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortTypeSpinner.setAdapter(adapter);
        sortTypeSpinner.setSelection(ClientApi.getClient().mGiphyList.getSortType().ordinal());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSortAsyncTask.cancel(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void finishAdd() {
        finish();
    }

    class SortAsyncTask extends AsyncTask<Void, Void, Boolean> {

        int sortField = 0;
        int sortType = 0;

        SortAsyncTask(int sortField, int sortType) {
            this.sortField = sortField;
            this.sortType = sortType;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            GiphyList.SortField sortFieldEnum = GiphyList.SortField.values()[sortField];
            GiphyList.SortType sortTypeEnum = GiphyList.SortType.values()[sortType];
            return ClientApi.getClient().sort(sortFieldEnum, sortTypeEnum);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            finishAdd();
        }
    }

}