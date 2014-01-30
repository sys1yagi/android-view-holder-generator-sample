
package com.example.app.generated.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app.MyTextView;

public class FragmentMainViewHolder {

    public TextView title;
    public MyTextView sub;
    public ImageView icon;

    public static FragmentMainViewHolder from(View rootView) {
        FragmentMainViewHolder holder = new FragmentMainViewHolder();
        holder.title = ((TextView) rootView.findViewById(com.example.app.R.id.title));
        holder.sub = ((MyTextView) rootView.findViewById(com.example.app.R.id.sub));
        holder.icon = ((ImageView) rootView.findViewById(com.example.app.R.id.icon));
        return holder;
    }

}
