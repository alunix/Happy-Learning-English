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
import com.worker.happylearningenglish.utils.GoogleTranslator;


public class WebviewActivity extends AppCompatActivity implements ClipboardManager.OnPrimaryClipChangedListener {

    String url;
    ClipboardManager clipboardManager;
    private WebView webview;
    private TextView linkEdit;
    private boolean hasFocus;
    private GoogleTranslateTask googleTask;

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
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(this);

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        this.hasFocus = hasFocus;
    }

    public void reloadPage(MenuItem i) {
        webview.reload();
    }

    public void stopLoadingPage(MenuItem i) {
        webview.stopLoading();
    }

    @Override
    public void onPrimaryClipChanged() {
        if (hasFocus) {
            CharSequence text = clipboardManager.getPrimaryClip().getItemAt(0).getText();
            Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT).show();
            if (googleTask == null)
                googleTask = new GoogleTranslateTask();
//            googleTask.execute(text.toString());
        }
    }

    class GoogleTranslateTask extends AsyncTask<String, String, String> {
        AlertDialog alertDialog;

        @Override
        protected String doInBackground(String... strings) {
            for (String i : strings)
                return GoogleTranslator.translate(i);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (alertDialog == null) {
                alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Translating result");
                alertDialog.setMessage(s);
            }
        }
    }
}
