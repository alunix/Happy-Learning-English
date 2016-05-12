package com.worker.happylearningenglish.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.worker.happylearningenglish.R;


/**
 * Created by hataketsu on 4/15/16.
 */
public class PageAdapter extends ArrayAdapter<PageEntry> {
    public PageAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_item, parent, false);
        PageEntry entry = getItem(position);
        ((ImageView) row.findViewById(R.id.row_thumb_iv)).setImageResource(entry.getImageID());
        ((TextView) row.findViewById(R.id.row_title_tv)).setText(entry.getTitle());
        return row;
    }
}
