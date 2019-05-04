package com.application.ravnandroidclient.MainActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.ravnandroidclient.GlideApp;
import com.application.ravnandroidclient.R;
import com.application.ravnandroidclient.client.GiphyList;
import com.application.ravnandroidclient.client.GiphyModel;

public class GiphyAdapter extends RecyclerView.Adapter<GiphyAdapter.GiphyViewHolder> {

    private static final String TAG = "GiphyAdapter";
    private GiphyList mGiphyList;
    private Context mContext;
    GiphyClickedListener mGiphyClickedListener;

    public GiphyAdapter(Context context, GiphyClickedListener giphyClickedListener) {
        mContext = context;
        mGiphyClickedListener = giphyClickedListener;
    }


    public class GiphyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public GiphyModel mGiphyModel;
        public ImageView mIvGiphy;
        public TextView mTvSortInfo;

        public GiphyViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mIvGiphy = v.findViewById(R.id.iv_giphy);
            mTvSortInfo = v.findViewById(R.id.tv_sort_info);
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

        holder.mGiphyModel = mGiphyList.models.get(position);

        Log.d(TAG, "GiphyAdapter loading: " + mGiphyList.models.get(position).src);
        GlideApp.with(mContext)
                .load(mGiphyList.models.get(position).src)
                .placeholder(circularProgressDrawable)
                .centerCrop()
                .into(holder.mIvGiphy);

        holder.mTvSortInfo.setText(mGiphyList.sortField + ": " +
                holder.mGiphyModel.getSortFieldValue(mGiphyList.getSortField()));

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
        if(mGiphyList == null) return 0;
        return mGiphyList.models.size();
    }

    public void updateList(GiphyList models) {
        mGiphyList = models;
        notifyDataSetChanged();
    }
}
