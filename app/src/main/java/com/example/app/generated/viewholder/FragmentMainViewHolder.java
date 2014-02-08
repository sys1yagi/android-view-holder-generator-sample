
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.app.MyTextView;
import com.example.app.R.layout;


/**
 * Auto-generated ViewHolder class which represents fragment_main.xml.
 * 
 */
public final class FragmentMainViewHolder {

    public final android.view.View view;
    public final TextView title;
    public final MyTextView sub;
    public final ImageView icon;
    public final android.view.View empty;

    public FragmentMainViewHolder(android.view.View rootView) {
        this.view = rootView;
        this.title = ((TextView) rootView.findViewById(com.example.app.R.id.title));
        this.sub = ((MyTextView) rootView.findViewById(com.example.app.R.id.sub));
        this.icon = ((ImageView) rootView.findViewById(com.example.app.R.id.icon));
        this.empty = rootView.findViewById(android.R.id.empty);
    }

    /**
     * Creates a viewholder, inflates fragment_main.xml, and binds the view object to the viewholder.
     * 
     */
    public static FragmentMainViewHolder from(Context context, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View rootView = inflater.inflate(layout.fragment_main, viewGroup, false);
        FragmentMainViewHolder holder = new FragmentMainViewHolder(rootView);
        rootView.setTag(holder);
        return holder;
    }

    /**
     * Same as <code>.from(context, viewGroup)</code> method but does nothing if <code>convertView</code> is supplied. It is designed to use in <code>ListAdapter#getView()</code>.
     * 
     */
    public static FragmentMainViewHolder from(Context context, android.view.View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            return FragmentMainViewHolder.from(context, viewGroup);
        } else {
            return ((FragmentMainViewHolder) convertView.getTag());
        }
    }

}
