package com.worker.happylearningenglish.rss;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
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
public class RSSAdapter extends ArrayAdapter<RSSEntry> {
    private final int thumbID;

    public RSSAdapter(Context context, int resource, int thumbID) {
        super(context, resource);
        this.thumbID = thumbID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.rss_item, parent, false);
        RSSEntry entry = getItem(position);
        ((ImageView) row.findViewById(R.id.rss_imgv)).setImageResource(thumbID);
        ((TextView) row.findViewById(R.id.rss_title_tv)).setText(entry.getTitle());
        TextView desc_tv = (TextView) row.findViewById(R.id.rss_desc_tv);
        CharSequence description = entry.getDescription();
        if (description.equals(""))
            desc_tv.setVisibility(View.GONE);
        else {
            Spanned html = Html.fromHtml((String) description);
            desc_tv.setText(html.subSequence(0, Math.min(60,html.length())) + " ...");
        }
        ((TextView) row.findViewById(R.id.rss_date_tv)).setText(entry.getDate());
        return row;
    }

}
