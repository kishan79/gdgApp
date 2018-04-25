
/*
*   Copyright (c) Ashar Khan 2017. <ashar786khan@gmail.com>
*    This file is part of Google Developer Group's Android Application.
*   Google Developer Group 's Android Application is free software : you can redistribute it and/or modify
*    it under the terms of GNU General Public License as published by the Free Software Foundation,
*   either version 3 of the License, or (at your option) any later version.
*
*   This Application is distributed in the hope that it will be useful
*    but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
*   or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General  Public License for more details.
*
*   You should have received a copy of the GNU General Public License along with this Source File.
*   If not, see <http:www.gnu.org/licenses/>.
 */

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
