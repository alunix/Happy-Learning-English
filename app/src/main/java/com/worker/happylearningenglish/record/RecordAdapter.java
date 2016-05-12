package com.worker.happylearningenglish.record;

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
public class RecordAdapter extends ArrayAdapter<RecordEntry> {
    public RecordAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.rss_item, parent, false);
        RecordEntry entry = getItem(position);
        ((ImageView) row.findViewById(R.id.rss_imgv)).setImageResource(R.drawable.record);
        ((TextView) row.findViewById(R.id.rss_title_tv)).setText(entry.getTitle());
        ((TextView) row.findViewById(R.id.rss_desc_tv)).setText(entry.getDescription());
        ((TextView) row.findViewById(R.id.rss_date_tv)).setText(entry.getDate());
        return row;
    }
}
