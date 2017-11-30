
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
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.softminds.gdg.R;
import com.softminds.gdg.utils.Constants;
import com.softminds.gdg.utils.GdgEvents;
import com.softminds.gdg.utils.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShortEventAdapter extends RecyclerView.Adapter<ShortEventAdapter.Holder> {

    private List<GdgEvents> eventsAll;
    private Context parent;

    private Typeface typeface;
    public ShortEventAdapter(){
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent.getContext();
        typeface = Typeface.createFromAsset(parent.getContext().getAssets(), Constants.PathConstants.PRODUCT_SANS_FONT);
       return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.short_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GdgEvents events = eventsAll.get(position);
        holder.date.setText(SimpleDateFormat.getDateInstance().format(new Date(events.getTime())));
        holder.title.setText(events.getName());
        holder.venue.setText(events.getVenue());

        holder.title.setTypeface(typeface);
        holder.date.setTypeface(typeface);
        holder.venue.setTypeface(typeface);

        Glide.with(parent).applyDefaultRequestOptions(RequestOptions.centerInsideTransform()).load(events.getHeadIconUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        if (eventsAll == null)
            return 0;
        return eventsAll.size();
    }

    public void setData(List<GdgEvents> events){
        List<GdgEvents> eventsList = new ArrayList<>();
        for(GdgEvents events1  : events){
            if(events1.getTime() > System.currentTimeMillis()){
                eventsList.add(events1);
            }
        }
        this.eventsAll = eventsList;
        notifyDataSetChanged();
    }

    public List<GdgEvents> getData() {
        return eventsAll;
    }

    static class Holder extends  RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView date;
        TextView venue;
        CardView cardView;

        Holder(View itemView) {
            super(itemView);

         cardView =    itemView.findViewById(R.id.short_adapter_card);
         date =    itemView.findViewById(R.id.short_adapter_date);
         title =    itemView.findViewById(R.id.short_adapter_title);
         venue = itemView.findViewById(R.id.short_adapter_venue);
          icon =   itemView.findViewById(R.id.short_adapter_image);
        }
    }

}
