
package com.example.app.generated.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.example.app.R.id;
import com.example.app.R.layout;


/**
 * Auto-generated ViewHolder class which represents activity_main.xml.
 * 
 */
public final class ActivityMainViewHolder {

    public final View view;
    public final FrameLayout container;

    public ActivityMainViewHolder(View rootView) {
        this.view = rootView;
        this.container = ((FrameLayout) rootView.findViewById(id.container));
    }

    /**
     * Creates a viewholder, inflates activity_main.xml, and binds the view object to the viewholder.
     * 
     */
    public static ActivityMainViewHolder from(Context context, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(layout.activity_main, viewGroup, false);
        ActivityMainViewHolder holder = new ActivityMainViewHolder(rootView);
        rootView.setTag(holder);
        return holder;
    }

    /**
     * Same as <code>.from(context, viewGroup)</code> method but does nothing if <code>convertView</code> is supplied. It is designed to use in <code>ListAdapter#getView()</code>.
     * 
     */
    public static ActivityMainViewHolder from(Context context, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            return ActivityMainViewHolder.from(context, viewGroup);
        } else {
            return ((ActivityMainViewHolder) convertView.getTag());
        }
    }

}
