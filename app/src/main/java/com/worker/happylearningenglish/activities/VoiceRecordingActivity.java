package com.worker.happylearningenglish.activities;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.record.RecordEntry;
import com.worker.happylearningenglish.utils.FileUtils;

import java.io.File;
import java.io.IOException;

public class VoiceRecordingActivity extends AppCompatActivity {

    boolean isRecording = false;
    MediaRecorder recorder;
    int counter = 1;
    File fileCounter = new File(FileUtils.getPath() + "record/counter.txt");
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recording);
        setTitle(R.string.title_voicerecord_act);
        titleTV = (TextView) findViewById(R.id.record_title_ed);
        initRecorder();
    }

    private void initRecorder() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File path = new File(FileUtils.getPath() + "tmp/tmp.3gp");
        if (path.isFile()) path.delete();
        recorder.setOutputFile(path.getPath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot get recording", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fileCounter.isFile()) {
            try {
                counter = Integer.valueOf(FileUtils.readFromFile(fileCounter.getPath()));
            } catch (IOException e) {
                e.printStackTrace();
                counter = 1;
            }
        }
        titleTV.setText("Record #" + counter);
    }

    private void save() {
        try {
            recorder.stop();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Cannot get recording", Toast.LENGTH_SHORT).show();
            return;
        }
        recorder.release();
        CharSequence title = titleTV.getText();
        CharSequence desc = ((TextView) findViewById(R.id.record_desc_ed)).getText();
        if (new RecordEntry(title, desc, null, null).save()) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Saving failed.", Toast.LENGTH_SHORT);
        }
        counter++;
        try {
            FileUtils.writeToFile(fileCounter.getPath(), String.valueOf(counter));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot save record counter.", Toast.LENGTH_LONG).show();
        }
        finish();
    }


    public void switchRecording(View v) {
        if (R.id.record_start_stop_bt == v.getId()) {
            if (!isRecording)
                recorder.start();
            else
                save();
            isRecording = !isRecording;
            ((ImageButton) findViewById(R.id.record_start_stop_bt)).setImageResource(isRecording ? R.drawable.stop_record : R.drawable.stop_record);
            ((TextView) findViewById(R.id.record_start_stop_tv)).setText(isRecording ? R.string.stop_recording : R.string.start_recording);
        }
    }
}
