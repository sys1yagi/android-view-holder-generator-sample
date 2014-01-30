
package com.example.app.generated.viewholder;

import android.view.View;
import android.widget.ImageView;

public class FragmentMainViewHolder {

    public android.widget.TextView title;
    public android.widget.TextView sub;
    public ImageView icon;

    public static FragmentMainViewHolder from(View rootView) {
        FragmentMainViewHolder holder = new FragmentMainViewHolder();
        holder.title = ((android.widget.TextView) rootView.findViewById(com.example.app.R.id.title));
        holder.sub = ((android.widget.TextView) rootView.findViewById(com.example.app.R.id.sub));
        holder.icon = ((ImageView) rootView.findViewById(com.example.app.R.id.icon));
        return holder;
    }

}
