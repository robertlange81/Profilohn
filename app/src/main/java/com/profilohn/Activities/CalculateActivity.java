package com.profilohn.Activities;
import com.profilohn.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CalculateActivity extends Activity {

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate);

        wv = (WebView) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebClient());

        wv.loadUrl("https://www.google.com");
        //wv.loadData("<html><body style='background: #000'><h1>Test</h1></body></html>", "text/html", "UTF-8");
    }


    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
