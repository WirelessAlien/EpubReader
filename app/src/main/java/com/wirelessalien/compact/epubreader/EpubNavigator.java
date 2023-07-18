package com.wirelessalien.compact.epubreader;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EpubNavigator {
	private EpubManipulator book;
	private ViewPanel view;
	private final MainActivity activity;
	private static Context context;

	public EpubNavigator(@NonNull MainActivity a) {
		activity = a;
		context = a.getBaseContext();
	}

	public boolean openBook(String path) {
		try {
			if (book != null)
				book.destroy();

			book = new EpubManipulator( path, "", context );
			changePanel( new BookView() );
			setBookPage( book.getSpineElementPath( 0 ) );

			return true;
		} catch (Exception e) {
			return false;
		}
	}


	public void setBookPage(String page) {
		if (book != null) {
			book.goToPage( page );
		}
		loadPageIntoView( page );
	}

	public void setNote(String page) {
		loadPageIntoView( page );
	}

	public void loadPageIntoView(String pathOfPage) {
		ViewStateEnum state = ViewStateEnum.notes;

		if (book != null && (pathOfPage.equals( book.getCurrentPageURL() ) || book.getPageIndex( pathOfPage ) >= 0))
			state = ViewStateEnum.books;

		if (view == null || !(view instanceof BookView))
			changePanel( new BookView() );

		((BookView) view).state = state;
		((BookView) view).loadPage( pathOfPage );
	}

	public void goToNextChapter() throws Exception {
		setBookPage( book.goToNextChapter() );
	}

	public void goToPrevChapter() throws Exception {
		setBookPage( book.goToPreviousChapter() );
	}

	public void closeView() {
		if (book != null && (!(view instanceof BookView) || ((BookView) view).state != ViewStateEnum.books)) {
			BookView v = new BookView();
			changePanel( v );
			v.loadPage( book.getCurrentPageURL() );
		} else {
			if (book != null) {
				try {
					book.destroy();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			activity.removePanel( view );
			book = null;
			view = null;
		}
	}

	public boolean displayMetadata() {
		boolean res = true;

		if (book != null) {
			DataView dv = new DataView();
			dv.loadData( book.metadata() );
			changePanel( dv );
		} else {
			res = false;
		}

		return res;
	}

	public boolean displayTOC() {
		boolean res = true;

		if (book != null) {
			setBookPage( book.tableOfContents() );
		} else {
			res = false;
		}

		return res;
	}

	public void changeCSS(String[] settings) {
		book.addCSS( settings );
		loadPageIntoView( book.getCurrentPageURL() );
	}

    public void changePanel(ViewPanel p) {
		if (view != null) {
			activity.removePanelWithoutClosing( view );
			p.changeWeight( view.getWeight() );
		}

		if (p.isAdded())
			activity.removePanelWithoutClosing( p );

		view = p;
		activity.addPanel( p );
		p.setKey( 0 );
	}

	// ...

	public void saveState(Editor editor) {
		if (book != null) {
			editor.putInt( getS( R.string.CurrentPageBook ) + 0, book.getCurrentSpineElementIndex() );
			editor.putString( getS( R.string.nameEpub ) + 0, book.getDecompressedFolder() );
			editor.putString( getS( R.string.pathBook ) + 0, book.getFileName() );
			try {
				book.closeStream();
			} catch (IOException e) {
				Log.e( getS( R.string.error_CannotCloseStream ), getS( R.string.Book_Stream ) + 1 );
				e.printStackTrace();
			}
		} else {
			editor.putInt( getS( R.string.CurrentPageBook ) + 0, 0 );
			editor.putString( getS( R.string.nameEpub ) + 0, null );
			editor.putString( getS( R.string.pathBook ) + 0, null );
		}

		if (view != null) {
			editor.putString( getS( R.string.ViewType ) + 0, view.getClass().getName() );
			view.saveState( editor );
			activity.removePanelWithoutClosing( view );
		} else {
			editor.putString( getS( R.string.ViewType ) + 0, "" );
		}
	}

	public boolean loadState(@NonNull SharedPreferences preferences) {
		boolean ok = true;
		int current, lang;
		String name, path;

		current = preferences.getInt( getS( R.string.CurrentPageBook ) + 0, 0 );
		lang = preferences.getInt( getS( R.string.LanguageBook ) + 0, 0 );
		name = preferences.getString( getS( R.string.nameEpub ) + 0, null );
		path = preferences.getString( getS( R.string.pathBook ) + 0, null );

		if (path != null) {
			try {
				book = new EpubManipulator( path, name, current, lang, context );
				book.goToPage( current );
			} catch (Exception | Error e1) {
				try {
					book = new EpubManipulator( path, "0", context );
					book.goToPage( current );
				} catch (Exception | Error e2) {
					ok = false;
				}
			}
		} else {
			book = null;
		}

		return ok;
	}

	public void loadViews(@NonNull SharedPreferences preferences) {
		view = newPanelByClassName( preferences.getString( getS( R.string.ViewType ) + 0, "" ) );
		if (view != null) {
			activity.addPanel( view );
			view.setKey( 0 );
			view.loadState( preferences );
		}
	}

	@Nullable
	private ViewPanel newPanelByClassName(@NonNull String className) {
		if (className.equals(BookView.class.getName()))
			return new BookView();
		if (className.equals(DataView.class.getName()))
			return new DataView();
		return null;
	}


	public String getS(int id) {
		return context.getResources().getString(id);
	}
}
