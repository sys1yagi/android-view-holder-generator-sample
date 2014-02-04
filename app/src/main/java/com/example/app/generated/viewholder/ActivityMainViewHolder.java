
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.example.app.R.id;
import com.example.app.R.layout;

public class ActivityMainViewHolder {

    public final android.widget.FrameLayout view;
    public final android.widget.FrameLayout container;

    public ActivityMainViewHolder(android.widget.FrameLayout rootView) {
        this.view = rootView;
        this.container = ((android.widget.FrameLayout) rootView.findViewById(id.container));
    }

    public static ActivityMainViewHolder from(Context context, ViewGroup viewGroup) {
        android.widget.FrameLayout rootView = ((android.widget.FrameLayout) View.inflate(context, layout.activity_main, viewGroup));
        ActivityMainViewHolder holder = new ActivityMainViewHolder(rootView);
        return holder;
    }

    public static ActivityMainViewHolder from() {
        ActivityMainViewHolder holder = null;
        return holder;
    }

}
