package com.application.ravnandroidclient.MainActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.ravnandroidclient.GlideApp;
import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.GiphyModel;

import java.util.ArrayList;
import java.util.List;

public class GiphyAdapter extends RecyclerView.Adapter<GiphyAdapter.GiphyViewHolder> {

    private static final String TAG = "GiphyAdapter";
    private List<GiphyModel> mGiphyModels = new ArrayList<>();
    private Context mContext;
    GiphyClickedListener mGiphyClickedListener;

    public GiphyAdapter(Context context, GiphyClickedListener giphyClickedListener) {
        mContext = context;
        mGiphyClickedListener = giphyClickedListener;
    }


    public class GiphyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public GiphyModel mGiphyModel;
        public ImageView mImageView;

        public GiphyViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mImageView = v.findViewById(R.id.iv_giphy);
        }

        @Override
        public void onClick(View view) {
            mGiphyClickedListener.onGiphyClicked(mGiphyModel);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GiphyAdapter.GiphyViewHolder holder, int position) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(mContext);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        holder.mGiphyModel = mGiphyModels.get(position);

        Log.d(TAG, "GiphyAdapter loading: " + mGiphyModels.get(position).src);
        GlideApp.with(mContext)
                .load(mGiphyModels.get(position).src)
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(holder.mImageView);

    }

    @NonNull
    @Override
    public GiphyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.giphy_list_item, parent, false);
        GiphyViewHolder vh = new GiphyViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mGiphyModels.size();
    }

    public void updateList(List<GiphyModel> models) {
        mGiphyModels = models;
        notifyDataSetChanged();
    }
}
