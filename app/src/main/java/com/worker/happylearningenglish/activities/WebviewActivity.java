package com.worker.happylearningenglish.activities;

import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.worker.happylearningenglish.R;


public class WebviewActivity extends AppCompatActivity {

    String url;
    ClipboardManager clipboardManager;
    private WebView webview;
    private TextView linkEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        setTitle("Surf the news");
        url = getIntent().getStringExtra("url");
        webview = (WebView) findViewById(R.id.webview_wv);
        linkEdit = ((TextView) findViewById(R.id.webview_link_ed));
        linkEdit.setText(url);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        webview.loadUrl(url.trim());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webview.destroy();
    }

    public void loadURL(View v) {
        webview.loadUrl(linkEdit.getText().toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }


    public void reloadPage(MenuItem i) {
        webview.reload();
    }

    public void stopLoadingPage(MenuItem i) {
        webview.stopLoading();
    }


}
