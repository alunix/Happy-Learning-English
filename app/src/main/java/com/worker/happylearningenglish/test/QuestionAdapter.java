package com.worker.happylearningenglish.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.rss.PageEntry;

/**
 * Created by hataketsu on 5/9/16.
 */
public class QuestionAdapter extends ArrayAdapter<QuestionEntry> {
    public QuestionAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionEntry entry = getItem(position);
        if (entry.getView() != null) return entry.getView();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.question_item, parent, false);
        ((TextView) row.findViewById(R.id.question_content)).setText("Câu số " + (position + 1) + ": " + entry.getContent());
        ((RadioButton) row.findViewById(R.id.answer_a)).setText(entry.getAnswer(0));
        ((RadioButton) row.findViewById(R.id.answer_b)).setText(entry.getAnswer(1));
        ((RadioButton) row.findViewById(R.id.answer_c)).setText(entry.getAnswer(2));
        ((RadioButton) row.findViewById(R.id.answer_d)).setText(entry.getAnswer(3));
        entry.setView(row);
        return row;
    }
}
