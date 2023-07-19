package com.wirelessalien.compact.epubreader;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Abstract fragment that represents a general panel containing only the closing button
public abstract class ViewPanel extends Fragment {

	private RelativeLayout generalLayout;
	protected int index;
	protected RelativeLayout layout;
	protected Button closeButton;
	protected EpubNavigator navigator;
	protected int screenWidth;
	protected int screenHeight;
	protected float weight = 0.5f; // weight of the generalLayout
	protected boolean created; // tells whether the fragment has been created

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_view_panel, container, false);
		created = false;
		return v;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		created = true;
		generalLayout = view.findViewById(R.id.GeneralLayout);
		layout = view.findViewById(R.id.Content);
		closeButton = view.findViewById(R.id.CloseButton);

		// ----- get activity screen size
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// -----

		changeWeight(weight);

		// ----- VIEW CLOSING
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeView();
			}
		});

		// Fade in animation
		Animation fadeInAnimation = new AlphaAnimation(0, 1);
		fadeInAnimation.setDuration(500); // Adjust the duration as needed
		generalLayout.startAnimation(fadeInAnimation);

		// Null check for getActivity()
		if (getActivity() instanceof MainActivity) {
			navigator = ((MainActivity) getActivity()).navigator;
		}
	}

	protected void closeView() {
		// Fade out animation
		Animation fadeOutAnimation = new AlphaAnimation(1, 0);
		fadeOutAnimation.setDuration(500); // Adjust the duration as needed
		fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				navigator.closeView();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		generalLayout.startAnimation(fadeOutAnimation);
	}

	// change the weight of the general layout
	public void changeWeight(float value) {
		weight = value;
		if (created) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, value);
			generalLayout.setLayoutParams(params);
		}
	}

	public float getWeight() {
		return weight;
	}

	public void setKey(int value) {
		index = value;
	}

	public void errorMessage(String message) {
		((MainActivity) getActivity()).errorMessage(message);
	}

	public void saveState(@NonNull Editor editor) {
		editor.putFloat("weight" + index, weight);
	}

	public void loadState(@NonNull SharedPreferences preferences) {
		changeWeight(preferences.getFloat("weight" + index, 0.5f));
	}


}
