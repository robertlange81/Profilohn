package com.profilohn.Activities;
import com.profilohn.R;

import android.app.Activity;
import android.os.Bundle;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;


public class CalculateActivity extends Activity {

    // private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate);

        /*
        wv = (WebView) findViewById(R.id.webview_calc);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebClient());

        wv.loadUrl("http://robert-lange.eu/loader2.html");*/
    }

    /*
    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/

}
