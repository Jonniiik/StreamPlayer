package com.example.AudioStream.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AudioStream.ModeliTunesApi.Result;
import com.example.AudioStream.R;
import com.squareup.picasso.Picasso;

public class RvSongsAdapter extends RecyclerView.Adapter<RvSongsAdapter.MyViewHolder> {

    private OnFragment1DataListener mListener;

    private Context mContext;
    private Result mResult;

    public RvSongsAdapter(Context mContext, Result mResult) {
        this.mContext = mContext;
        this.mResult = mResult;
    }

    @NonNull
    @Override
    public RvSongsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_list_soungs, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvSongsAdapter.MyViewHolder holder, final int position) {
        holder.textViewNameBand.setText(new StringBuilder(mResult.getResults().get(position).getArtistName()));
        holder.textViewNameSound.setText(String.valueOf(mResult.getResults().get(position).getTrackName()));
        Picasso.get().load(mResult.getResults().get(position).getArtworkUrl60()).into(holder.imageViewImageAlbum);

        holder.linearLayoutListSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageURL = mResult.getResults().get(position).getArtworkUrl100();
                String artistURL = mResult.getResults().get(position).getPreviewUrl();
                mListener.onOpenFragment(imageURL, artistURL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResult.getResults().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewImageAlbum;
        TextView textViewNameSound;
        TextView textViewNameBand;
        LinearLayout linearLayoutListSounds;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewImageAlbum = (ImageView) itemView.findViewById(R.id.imageViewImageAlbum);
            textViewNameBand = (TextView) itemView.findViewById(R.id.textViewNameBand);
            textViewNameSound = (TextView) itemView.findViewById(R.id.textViewNameSound);
            linearLayoutListSounds = (LinearLayout) itemView.findViewById(R.id.linearLayoutListSounds);

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mContext = recyclerView.getContext();
        if (mContext instanceof OnFragment1DataListener) {
            mListener = (OnFragment1DataListener) mContext;
        } else {
            throw new RuntimeException(mContext.toString()
                    + " must implement OnFragment1DataListener");
        }
    }
}
