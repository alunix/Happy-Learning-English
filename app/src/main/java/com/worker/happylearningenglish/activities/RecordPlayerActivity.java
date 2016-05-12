package com.worker.happylearningenglish.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.worker.happylearningenglish.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class RecordPlayerActivity extends AppCompatActivity {

    MediaPlayer player;
    boolean isPlaying = false;
    TextView timer_tv;
    Timer timer = new Timer();
    private String title, desc, url;
    private SeekBar seekBar;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player == null) return;
            int currentMiliPosition = player.getCurrentPosition();
            seekBar.setProgress(currentMiliPosition);
            timer_tv.setText(miliToMS(currentMiliPosition) + "/" + miliToMS(player.getDuration()));
        }
    };
    private boolean isStreaming;
    private ImageView button;
    private TextView loading_tv;
    ;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        player.release();
        player = null;
        Log.d("PLAYER", "destroyed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_player);
        setTitle(R.string.title_record_player_act);
        receiveIntent();
        ((TextView) findViewById(R.id.player_title_tv)).setText(title);
        ((TextView) findViewById(R.id.player_desc_tv)).setText(Html.fromHtml(desc));
        button = ((ImageView) findViewById(R.id.player_start_bt));
        timer_tv = (TextView) findViewById(R.id.player_timer_tv);
        loading_tv = (TextView) findViewById(R.id.player_loading_tv);
        if (!isStreaming) loading_tv.setVisibility(View.INVISIBLE);
        seekBar = (SeekBar) findViewById(R.id.player_sb);
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            if (isStreaming) {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                loading_tv.setVisibility(View.VISIBLE);
                timer_tv.setText("Buffering...");
                button.setEnabled(false);
                player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        loading_tv.setText("" + i + "%");
                    }
                });
                new Thread(new Runnable() {
                    boolean error = false;

                    @Override
                    public void run() {
                        try {
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                            error = true;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (error) {
                                    Toast.makeText(getApplication(), "Cannot load url " + url, Toast.LENGTH_LONG);
                                    return;
                                }
                                button.setEnabled(true);
                                timer_tv.setText("Ready to rock " + miliToMS(player.getDuration()));
                                seekBar.setMax(player.getDuration());
                            }
                        });
                    }
                }).start();

            } else {
                player.prepare();
                timer_tv.setText(miliToMS(player.getDuration()));
                seekBar.setMax(player.getDuration());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot load url " + url, Toast.LENGTH_LONG);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!player.isPlaying()) return;
                runOnUiThread(runnable);
            }
        }, 0, 200);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        url = intent.getStringExtra("url");
        Log.d("MP3", url);
        isStreaming = intent.getBooleanExtra("stream", false);
    }

    public void goPlay(View v) {
        seekBar.setMax(player.getDuration());
        Log.d("PLAYER", url);
        if (!isPlaying) {
            player.start();
        } else {
            player.pause();
        }
        isPlaying = !isPlaying;
        button.setImageResource(isPlaying ? R.mipmap.ic_pause : R.mipmap.ic_play);
    }

    public String miliToMS(int mili) {
        int sec = mili / 1000;
        int min = sec / 60;
        sec = sec % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
