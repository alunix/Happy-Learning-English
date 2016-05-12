package com.worker.happylearningenglish.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.worker.happylearningenglish.R;

/**
 * Created by hataketsu on 5/8/16.
 */
public class SubLevelAdapter extends ArrayAdapter<SubLevelEntry> {

    public SubLevelAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.sublevel_item, parent, false);
        SubLevelEntry entry = getItem(position);
        TextView desc = (TextView) row.findViewById(R.id.sublevel_tv);
        desc.setText(entry.getDescription());
        return row;
    }
}
