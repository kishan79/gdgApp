package com.softminds.gdg.helpers;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.softminds.gdg.utils.RecyclerItemClickListener;


public class RecyclerItemClick implements RecyclerView.OnItemTouchListener {

    private RecyclerItemClickListener listener;

    private GestureDetector detector;

    public RecyclerItemClick(Context ctx, RecyclerItemClickListener l) {
        listener = l;
        detector = new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && listener != null && detector.onTouchEvent(e)) {
            listener.OnItemClick(rv.getChildAdapterPosition(child), child);
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