
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import com.example.app.R.id;
import com.example.app.R.layout;

public class ActivityMainViewHolder {

    public android.widget.FrameLayout view;
    public android.widget.FrameLayout container;

    public static ActivityMainViewHolder from(Context context, ViewGroup rootView) {
        ActivityMainViewHolder holder = new ActivityMainViewHolder();
        android.view.View view = android.view.View.inflate(context, layout.activity_main, rootView);
        holder.container = ((android.widget.FrameLayout) view.findViewById(id.container));
        return holder;
    }

}
