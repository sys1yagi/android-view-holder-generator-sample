
package com.example.app.generated.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import com.example.app.R.id;

public class ActivityMainViewHolder {

    public FrameLayout container;

    public static ActivityMainViewHolder from(View rootView) {
        ActivityMainViewHolder holder = new ActivityMainViewHolder();
        holder.container = ((FrameLayout) rootView.findViewById(id.container));
        return holder;
    }

}
