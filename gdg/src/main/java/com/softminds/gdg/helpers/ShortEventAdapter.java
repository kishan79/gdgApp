package com.softminds.gdg.helpers;

import android.content.Context;
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
import com.softminds.gdg.R;
import com.softminds.gdg.utils.GdgEvents;
import com.softminds.gdg.utils.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ShortEventAdapter extends RecyclerView.Adapter<ShortEventAdapter.Holder> {

    private List<GdgEvents> eventsAll;
    private Context parent;
    public ShortEventAdapter(List<GdgEvents> events){
        eventsAll = events;
    }
    public ShortEventAdapter(){
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent.getContext();
       return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.short_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        GdgEvents events = eventsAll.get(position);
        holder.date.setText(SimpleDateFormat.getDateTimeInstance().format(new Date(events.getTime())));
        holder.title.setText(events.getName());
        Glide.with(parent).asBitmap().load(events.getHeadIconUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return eventsAll.size();
    }

    public void setData(List<GdgEvents> events){
        this.eventsAll= events;
    }

    public List<GdgEvents> getData() {
        return eventsAll;
    }

    static class Holder extends  RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView date;
        CardView cardView;

        Holder(View itemView) {
            super(itemView);

         cardView =    itemView.findViewById(R.id.short_adapter_card);
         date =    itemView.findViewById(R.id.short_adapter_date);
         title =    itemView.findViewById(R.id.short_adapter_title);
          icon =   itemView.findViewById(R.id.short_adapter_image);
        }
    }

    public static class RecyclerItemClick implements RecyclerView.OnItemTouchListener{

        RecyclerItemClickListener listener;

        GestureDetector detector;

        public RecyclerItemClick(Context ctx,RecyclerItemClickListener l){
            listener = l;
            detector = new GestureDetector(ctx,new GestureDetector.SimpleOnGestureListener(){
               @Override
                public boolean onSingleTapUp(MotionEvent e){
                   return true;
               }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
           View child = rv.findChildViewUnder(e.getX(),e.getY());
           if(child!=null && listener !=null && detector.onTouchEvent(e)){
               listener.OnItemClick(rv.getChildAdapterPosition(child),child);
           }
           return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
