
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.app.MyTextView;
import com.example.app.R.layout;

public class FragmentMainViewHolder {

    public final LinearLayout view;
    public final TextView title;
    public final MyTextView sub;
    public final ImageView icon;

    public FragmentMainViewHolder(LinearLayout rootView) {
        this.view = rootView;
        this.title = ((TextView) rootView.findViewById(com.example.app.R.id.title));
        this.sub = ((MyTextView) rootView.findViewById(com.example.app.R.id.sub));
        this.icon = ((ImageView) rootView.findViewById(com.example.app.R.id.icon));
    }

    public static FragmentMainViewHolder from(Context context, ViewGroup viewGroup) {
        LinearLayout rootView = ((LinearLayout) View.inflate(context, layout.fragment_main, viewGroup));
        FragmentMainViewHolder holder = new FragmentMainViewHolder(rootView);
        return holder;
    }

    public static FragmentMainViewHolder from() {
        FragmentMainViewHolder holder = null;
        return holder;
    }

}
