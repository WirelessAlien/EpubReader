package com.wirelessalien.compact.epubreader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// Panel specialized in visualizing EPUB pages
public class BookView extends ViewPanel {
	public ViewStateEnum state = ViewStateEnum.books;
	protected String viewedPage;
	protected WebView view;
	protected float swipeOriginX, swipeOriginY;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.activity_book_view, container, false);
	}


	@Override
	public void onViewCreated(@NonNull View fragmentView, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(fragmentView, savedInstanceState);
		WebView webView = fragmentView.findViewById(R.id.Viewport);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setAllowFileAccessFromFileURLs(true);
		webSettings.setAllowUniversalAccessFromFileURLs(true);
		webSettings.setDomStorageEnabled(true);

		// ----- SWIPE PAGE
		webView.setOnTouchListener((v, event) -> {
			if (state == ViewStateEnum.books)
				swipePage(event);

			view = (WebView) v;
			return view.onTouchEvent(event);
		});

		// ----- NOTE & LINK
		webView.setOnLongClickListener(v -> {
			Message msg = new Message();
			Handler handler = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					String url = msg.getData().getString(getString(R.string.url));
					if (url != null) {
						navigator.setNote(url);
					}
				}
			};
			msg.setTarget(handler);
			webView.requestFocusNodeHref(msg);
			return false;
		});

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				try {
					navigator.setBookPage(url);
				} catch (Exception e) {
					errorMessage(getString(R.string.error_LoadPage));
				}
				return true;
			}
		});

		loadPage(viewedPage);
	}

	public void loadPage(String path) {
		viewedPage = path;
		if (created) {
			WebView webView = requireView().findViewById(R.id.Viewport);
			webView.loadUrl(path);
		}
	}

	// Change page
	protected void swipePage(@NonNull MotionEvent event) {
		int action = event.getActionMasked();

		switch (action) {
			case (MotionEvent.ACTION_DOWN):
				swipeOriginX = event.getX();
				swipeOriginY = event.getY();
				break;

			case (MotionEvent.ACTION_UP):
				int quarterWidth = (int) (screenWidth * 0.25);
				float diffX = swipeOriginX - event.getX();
				float diffY = swipeOriginY - event.getY();
				float absDiffX = Math.abs(diffX);
				float absDiffY = Math.abs(diffY);

				if ((diffX > quarterWidth) && (absDiffX > absDiffY)) {
					try {
						navigator.goToNextChapter();
					} catch (Exception e) {
						errorMessage(getString(R.string.error_cannotTurnPage));
					}
				} else if ((diffX < -quarterWidth) && (absDiffX > absDiffY)) {
					try {
						navigator.goToPrevChapter();
					} catch (Exception e) {
						errorMessage(getString(R.string.error_cannotTurnPage));
					}
				}
				break;
		}
	}

	@Override
	public void saveState(@NonNull Editor editor) {
		super.saveState(editor);
		editor.putString("state"+index, state.name());
		editor.putString("page"+index, viewedPage);
	}
	
	@Override
	public void loadState(@NonNull SharedPreferences preferences)
	{
		super.loadState(preferences);
		loadPage(preferences.getString("page"+index, ""));
		state = ViewStateEnum.valueOf(preferences.getString("state"+index, ViewStateEnum.books.name()));
	}
	
}
