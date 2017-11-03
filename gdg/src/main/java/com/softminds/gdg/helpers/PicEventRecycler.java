package com.softminds.gdg.helpers;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softminds.gdg.R;

import java.util.List;

public class PicEventRecycler extends RecyclerView.Adapter<PicEventRecycler.ImageHolder> {


    private List<String> urls;
    private Context ctx;

    public PicEventRecycler(List<String> url){
        urls = url;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.event_pics_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        Glide.with(ctx).applyDefaultRequestOptions(RequestOptions.centerInsideTransform()).load(urls.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    static class ImageHolder extends RecyclerView.ViewHolder {

        ImageView image;

        ImageHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pics_adapter_source);
        }
    }
}
