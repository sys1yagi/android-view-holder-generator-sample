
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.app.MyTextView;
import com.example.app.R.layout;

public class FragmentMainViewHolder {

    public LinearLayout view;
    public TextView title;
    public MyTextView sub;
    public ImageView icon;

    public static FragmentMainViewHolder from(Context context, ViewGroup rootView) {
        FragmentMainViewHolder holder = new FragmentMainViewHolder();
        android.view.View view = android.view.View.inflate(context, layout.fragment_main, rootView);
        holder.title = ((TextView) view.findViewById(com.example.app.R.id.title));
        holder.sub = ((MyTextView) view.findViewById(com.example.app.R.id.sub));
        holder.icon = ((ImageView) view.findViewById(com.example.app.R.id.icon));
        return holder;
    }

}
