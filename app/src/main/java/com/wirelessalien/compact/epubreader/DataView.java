package com.wirelessalien.compact.epubreader;

import android.content.Context;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

//Panel specialized in visualizing HTML-data
public class DataView extends ViewPanel {
	protected WebView view;
	protected String data;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.activity_data_view, container, false);
		view = v.findViewById(R.id.Viewport);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupWebView();
		loadData(data);
	}

	private void setupWebView() {
		view.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					navigator.setBookPage(url);
				} catch (Exception e) {
					errorMessage(getString(R.string.error_LoadPage));
				}
				return true;
			}
		});
	}

	public void loadData(String source) {
		data = source;
		if (isCreated()) {
			Context context = view.getContext();
			if (context != null) {
				view.loadData(data, context.getString(R.string.textOrHTML), null);
			}
		}
	}

	@Override
	public void saveState(@NonNull Editor editor) {
		super.saveState(editor);
		editor.putString("data" + index, data);
	}

	@Override
	public void loadState(@NonNull SharedPreferences preferences) {
		super.loadState(preferences);
		loadData(preferences.getString("data" + index, ""));
	}

	private boolean isCreated() {
		return view != null && getActivity() != null;
	}
}

