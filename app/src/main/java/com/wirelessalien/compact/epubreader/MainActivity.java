/*
The MIT License (MIT)

Copyright (c) 2013, V. Giacometti, M. Giuriato, B. Petrantuono

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package com.wirelessalien.compact.epubreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

	protected EpubNavigator navigator;
	protected int panelCount;
	protected String[] settings;
	protected String epub_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navigator = new EpubNavigator(this);

		panelCount = 0;
		settings = new String[8];

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		loadState(preferences);
		navigator.loadViews(preferences);

		if (panelCount == 0) {
			epub_location = getIntent().getStringExtra("epub_location");
			if (epub_location == null || epub_location.isEmpty()) {
				openFileChooser();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (panelCount == 0) {
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			navigator.loadViews(preferences);
		}

		if (epub_location != null && !epub_location.isEmpty()) {
			navigator.openBook(epub_location);
			epub_location = null;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		Editor editor = preferences.edit();
		saveState(editor);
		editor.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (panelCount == 0) {
			SharedPreferences preferences = getPreferences(MODE_PRIVATE);
			navigator.loadViews(preferences);
		}

	}

	private void openFileChooser() {

		//open file chooser class
		Intent goToChooser = new Intent(this, FileChooser.class);
		goToChooser.putExtra(getString(R.string.second), getString(R.string.time));
		startActivityForResult(goToChooser, 0);


	}

	// ---- Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.FirstEPUB:
			Intent goToChooser1 = new Intent(this, FileChooser.class);
			goToChooser1.putExtra(getString(R.string.second),
					getString(R.string.time));
			startActivityForResult(goToChooser1, 0);
			return true;

			case R.id.Metadata:
			if (!navigator.displayMetadata())
				errorMessage(getString(R.string.error_metadataNotFound));
			return true;

			case R.id.tableOfContents:
				if (!navigator.displayTOC())
					errorMessage(getString(R.string.error_tocNotFound));
				return true;

		case R.id.Style:
			try {
				{
					DialogFragment newFragment = new ChangeCSSMenu();
					newFragment.show(getSupportFragmentManager(), "");
				}
			} catch (Exception e) {
				errorMessage(getString(R.string.error_CannotChangeStyle));
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// ----

	// ---- Panels Manager
	public void addPanel(ViewPanel p) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.add(R.id.MainLayout, p, p.getTag());
		fragmentTransaction.commit();

		panelCount++;
	}


	public void removePanelWithoutClosing(ViewPanel p) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.remove(p);
		fragmentTransaction.commit();

		panelCount--;
	}


	public void removePanel(ViewPanel p) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		fragmentTransaction.remove(p);
		fragmentTransaction.commit();

		panelCount--;
		if (panelCount <= 0)
			finish();
	}


	//back pressed close the book


	// ---- Change Style
	public void setCSS() {
		navigator.changeCSS(settings);
	}

	public void setBackColor(String my_backColor) {
		settings[1] = my_backColor;
	}

	public void setColor(String my_color) {
		settings[0] = my_color;
	}

	public void setFontType(String my_fontFamily) {
		settings[2] = my_fontFamily;
	}

	public void setFontSize(String my_fontSize) {
		settings[3] = my_fontSize;
	}

	public void setLineHeight(String my_lineHeight) {
		if (my_lineHeight != null)
			settings[4] = my_lineHeight;
	}

	public void setAlign(String my_Align) {
		settings[5] = my_Align;
	}

	public void setMarginLeft(String mLeft) {
		settings[6] = mLeft;
	}

	public void setMarginRight(String mRight) {
		settings[7] = mRight;
	}


	// Save/Load State
	protected void saveState(Editor editor) {
		navigator.saveState(editor);
	}

	protected void loadState(SharedPreferences preferences) {
		if (!navigator.loadState(preferences))
			errorMessage(getString(R.string.error_cannotLoadState));
	}

	public void errorMessage(String message) {
		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}
}
