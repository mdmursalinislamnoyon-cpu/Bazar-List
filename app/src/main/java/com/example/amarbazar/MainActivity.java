
package com.example.amarbazar;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.content.Context;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(
            android.graphics.Color.rgb(23, 107, 82)
        );
        getWindow().setNavigationBarColor(
            android.graphics.Color.rgb(23, 107, 82)
        );

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);

        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        webView.addJavascriptInterface(
            new PrintBridge(), "AndroidPrint"
        );

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("file:///android_asset/index.html");
    }

    private class PrintBridge {
        @JavascriptInterface
        public void printPage() {
            runOnUiThread(() -> {
                webView.postDelayed(() -> {
                    PrintManager printManager =
                        (PrintManager) getSystemService(
                            Context.PRINT_SERVICE
                        );

                    if (printManager != null) {
                        String jobName = getString(R.string.app_name);

                        printManager.print(
                            jobName,
                            webView.createPrintDocumentAdapter(jobName),
                            new PrintAttributes.Builder().build()
                        );
                    }
                }, 500);
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
