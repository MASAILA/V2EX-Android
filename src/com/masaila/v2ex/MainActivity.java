package com.masaila.v2ex;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends Activity {

	private WebView webView;
	private Button buttonGetCookie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (!"".equals(PreferenceManager.getDefaultSharedPreferences(
				MainActivity.this).getString("cookie", ""))) {
			startActivity(new Intent(MainActivity.this, TopicsActivity.class));
			finish();
		}
		init();
	}

	private void init() {
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl("http://www.v2ex.com/signin");
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

		});
		CookieSyncManager.createInstance(MainActivity.this);
		CookieSyncManager.getInstance().startSync();

		buttonGetCookie = (Button) findViewById(R.id.button_getCookie);
		buttonGetCookie.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CookieManager cookieManager = CookieManager.getInstance();
				System.err.println();
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(MainActivity.this);
				preferences
						.edit()
						.putString("cookie",
								cookieManager.getCookie("www.v2ex.com"))
						.commit();
				CookieSyncManager.getInstance().stopSync();
				startActivity(new Intent(MainActivity.this,
						TopicsActivity.class));
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
