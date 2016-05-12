package com.worker.happylearningenglish.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.worker.happylearningenglish.fragment.FuntionAdapter;
import com.worker.happylearningenglish.R;
import com.worker.happylearningenglish.fragment.OnBackPressedListener;
import com.worker.happylearningenglish.test.TestDatabase;
import com.worker.happylearningenglish.utils.FileUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static ViewPager pager;
    private static Context context;
    private FragmentPagerAdapter collectionAdapter;
    private TabLayout tabLayout;
    private ImageView shipImageView;
    private ImageView logo;

    public static Context getContext() {
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.splash_screen);
        shipImageView = (ImageView) findViewById(R.id.splash_ship_iv);
        shipImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_open));
        shipImageView.setVisibility(View.VISIBLE);
        logo = (ImageView) findViewById(R.id.splash_logo_iv);
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.move_down));
        logo.setVisibility(View.VISIBLE);
        TestDatabase.storeDatabase();
    }

    public void realOnCreate(View v) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = (ViewPager) findViewById(R.id.main_pager_vg);
        collectionAdapter = new FuntionAdapter(getSupportFragmentManager());
        pager.setAdapter(collectionAdapter);
        tabLayout = (TabLayout) findViewById(R.id.main_tb);
        tabLayout.setupWithViewPager(pager);
        initDirTree();
    }

    private void initDirTree() {
        if (new File(FileUtils.getPath()).isDirectory()) return;
        String[] PATHS = {"listen", "read", "record", "tmp"};
        for (String i : PATHS) {
            String path = FileUtils.getPath() + i;
            new File(path).mkdirs();
            Log.d("ENGLISHTOOL", "Created folder " + path);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }

    public void startRecording(View v) {
//        startActivity(new Intent(this, VoiceRecordingActivity.class));
    }

    public void openAbout(MenuItem i) {
        Toast.makeText(this, "Nothing here", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (collectionAdapter == null) confirmExit();
        else if (((OnBackPressedListener) collectionAdapter.getItem(pager.getCurrentItem())).onBackPressed())
            return;
        else confirmExit();
    }

    private void confirmExit() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirm_exit);
        dialog.setTitle(R.string.confirm);
        ((Button) dialog.findViewById(R.id.confirm_yes)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                finish();
            }
        });
        ((Button) dialog.findViewById(R.id.confirm_no)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        ((OnClickListener) collectionAdapter.getItem(pager.getCurrentItem())).onClick(view);
    }
}
