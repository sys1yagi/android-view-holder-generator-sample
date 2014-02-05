
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.app.R.id;
import com.example.app.R.layout;

public final class ActivityMainViewHolder {

    public final View view;
    public final FrameLayout container;

    public ActivityMainViewHolder(View rootView) {
        this.view = rootView;
        this.container = ((FrameLayout) rootView.findViewById(id.container));
    }

    public static ActivityMainViewHolder from(Context context, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(layout.activity_main, viewGroup, false);
        ActivityMainViewHolder holder = new ActivityMainViewHolder(rootView);
        rootView.setTag(holder);
        return holder;
    }

    public static ActivityMainViewHolder from(Context context, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            return ActivityMainViewHolder.from(context, viewGroup);
        } else {
            return ((ActivityMainViewHolder) convertView.getTag());
        }
    }

}
