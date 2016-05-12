package com.worker.happylearningenglish.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.activities.RecordPlayerActivity;
import com.worker.happylearningenglish.record.RecordAdapter;
import com.worker.happylearningenglish.record.RecordEntry;
import com.worker.happylearningenglish.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by hataketsu on 4/23/16.
 */
public class RecordingFragment extends android.support.v4.app.Fragment implements OnBackPressedListener {
    View rootView;
    RecordAdapter recordAdapter;
    private FrameLayout container;
    private ListView listView;
    private View contentLayout;
    private View blankLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.recording_fragment, container, false);
            recordAdapter = new RecordAdapter(getActivity(), -1);
            init();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFileList();
    }

    private void refreshFileList() {
        container.removeAllViews();
        File[] files = new File(FileUtils.getPath() + "record").listFiles();
        ArrayList<File> folderList = new ArrayList<>();
        for (File i : files) if (i.isDirectory()) folderList.add(i);
        if (folderList.size() == 0) {
            container.addView(blankLayout);
        } else {
            container.addView(contentLayout);
            recordAdapter.clear();
            for (final File i : folderList) {
                RecordEntry entry = RecordEntry.fromFile(i);
                if (entry != null)
                    recordAdapter.add(entry);
            }
        }
    }

    private void init() {
        container = (FrameLayout) rootView.findViewById(R.id.recording_container_fl);
        blankLayout = rootView.inflate(getContext(), R.layout.recording_blank, null);
        contentLayout = rootView.inflate(getContext(), R.layout.recording_contents, null);
        listView = (ListView) contentLayout.findViewById(R.id.recording_lv);
        listView.setAdapter(recordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RecordEntry entry = (RecordEntry) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), RecordPlayerActivity.class);
                intent.putExtra("title", entry.getTitle());
                intent.putExtra("desc", entry.getDescription());
                intent.putExtra("url", entry.getUrl());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int pos, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.confirm);
                builder.setMessage(R.string.delete_record);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RecordEntry entry = (RecordEntry) adapterView.getItemAtPosition(pos);
                        entry.delete();
                        recordAdapter.remove(entry);
                        if (recordAdapter.getCount() == 0) {
                            container.removeAllViews();
                            container.addView(blankLayout);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }


    @Override
    public boolean onBackPressed() {

        return false;
    }
}
