package com.wirelessalien.compact.epubreader;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ChangeCSSMenu extends DialogFragment {

	protected Builder builder;
	protected Spinner spinColor;
	protected Spinner spinBack;
	protected Spinner spinFontStyle;
	protected Spinner spinAlignText;
	protected Spinner spinFontSize;
	protected Spinner spinLineH;
	protected Button defaultButton;
	protected Spinner spinLeft;
	protected Spinner spinRight;
	protected int colInt, backInt, fontInt, alignInt, sizeInt, heightInt,
			marginLInt, marginRInt;
	protected MainActivity a;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		builder = new Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		a = (MainActivity)getActivity();
		View view = inflater.inflate(R.layout.change_css, null);

		final SharedPreferences preferences = a.getPreferences(Context.MODE_PRIVATE);

		spinColor = view.findViewById(R.id.spinnerColor);
		colInt = preferences.getInt("spinColorValue", 0);
		spinColor.setSelection(colInt);

		spinBack = view.findViewById(R.id.spinnerBackgroundColor );
		backInt = preferences.getInt("spinBackValue", 0);
		spinBack.setSelection(backInt);

		spinFontStyle = view.findViewById(R.id.spinnerFontFamily);
		fontInt = preferences.getInt("spinFontStyleValue", 0);
		spinFontStyle.setSelection(fontInt);

		spinAlignText = view.findViewById(R.id.spinnerAlign);
		alignInt = preferences.getInt("spinAlignTextValue", 0);
		spinAlignText.setSelection(alignInt);

		spinFontSize = view.findViewById(R.id.spinnerFS);
		sizeInt = preferences.getInt("spinFontSizeValue", 0);
		spinFontSize.setSelection(sizeInt);

		spinLineH = view.findViewById(R.id.spinnerLH);
		heightInt = preferences.getInt("spinLineHValue", 0);
		spinLineH.setSelection(heightInt);

		spinLeft = view.findViewById(R.id.spinnerLeft);
		marginLInt = preferences.getInt("spinLeftValue", 0);
		spinLeft.setSelection(marginLInt);

		spinRight = view.findViewById(R.id.spinnerRight);
		marginRInt = preferences.getInt("spinRightValue", 0);
		spinRight.setSelection(marginRInt);

		defaultButton = view.findViewById(R.id.buttonDefault);
		// editTextTop = (EditText) view.findViewById(R.id.editText1);
		// editTextBottom = (EditText) view.findViewById(R.id.editText2);
		// editTextLeft = (EditText) view.findViewById(R.id.editText3);
		// editTextRight = (EditText) view.findViewById(R.id.editText4);

		builder.setTitle("Style");
		builder.setView(view);

		spinColor
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						colInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setColor(getString(R.string.black_rgb));
							break;
						case 1:
							a.setColor(getString(R.string.red_rgb));
							break;
						case 2:
							a.setColor(getString(R.string.green_rgb));
							break;
						case 3:
							a.setColor(getString(R.string.blue_rgb));
							break;
						case 4:
							a.setColor(getString(R.string.white_rgb));
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinBack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				backInt = (int) id;
				switch ((int) id) {
				case 0:
					a.setBackColor(getString(R.string.white_rgb));
					break;
				case 1:
					a.setBackColor(getString(R.string.red_rgb));
					break;
				case 2:
					a.setBackColor(getString(R.string.green_rgb));
					break;
				case 3:
					a.setBackColor(getString(R.string.blue_rgb));
					break;
				case 4:
					a.setBackColor(getString(R.string.black_rgb));
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinFontStyle
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						fontInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setFontType(getString(R.string.Arial));
							break;
						case 1:
							a.setFontType(getString(R.string.Serif));
							break;
						case 2:
							a.setFontType(getString(R.string.Monospace));
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinAlignText
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						alignInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setAlign(getString(R.string.Left_Align));
							break;
						case 1:
							a.setAlign(getString(R.string.Center_Align));
							break;
						case 2:
							a.setAlign(getString(R.string.Right_Align));
							break;
						case 3:
							a.setAlign(getString(R.string.Justify));
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinFontSize
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						sizeInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setFontSize("100");
							break;
						case 1:
							a.setFontSize("125");
							break;
						case 2:
							a.setFontSize("150");
							break;
						case 3:
							a.setFontSize("175");
							break;
						case 4:
							a.setFontSize("200");
							break;
						case 5:
							a.setFontSize("90");
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinLineH
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						heightInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setLineHeight("1");
							break;
						case 1:
							a.setLineHeight("1.25");
							break;
						case 2:
							a.setLineHeight("1.5");
							break;
						case 3:
							a.setLineHeight("1.75");
							break;
						case 4:
							a.setLineHeight("2");
							break;
						case 5:
							a.setLineHeight("0.9");
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				marginLInt = (int) id;
				switch ((int) id) {
				case 0:
					a.setMarginLeft("0");
					break;
				case 1:
					a.setMarginLeft("5");
					break;
				case 2:
					a.setMarginLeft("10");
					break;
				case 3:
					a.setMarginLeft("15");
					break;
				case 4:
					a.setMarginLeft("20");
					break;
				case 5:
					a.setMarginLeft("25");
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinRight
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						marginRInt = (int) id;
						switch ((int) id) {
						case 0:
							a.setMarginRight("0");
							break;
						case 1:
							a.setMarginRight("5");
							break;
						case 2:
							a.setMarginRight("10");
							break;
						case 3:
							a.setMarginRight("15");
							break;
						case 4:
							a.setMarginRight("20");
							break;
						case 5:
							a.setMarginRight("25");
							break;
						default:
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		defaultButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				a.setColor("");
				a.setBackColor("");
				a.setFontType("");
				a.setFontSize("");
				a.setLineHeight("");
				a.setAlign("");
				a.setMarginLeft("");
				a.setMarginRight("");
				a.setCSS();
				SharedPreferences.Editor editor = preferences.edit();
				editor.putInt("spinColorValue", 0);
				editor.putInt("spinBackValue", 0);
				editor.putInt("spinFontStyleValue", 0);
				editor.putInt("spinAlignTextValue", 0);
				editor.putInt("spinFontSizeValue", 0);
				editor.putInt("spinLineHValue", 0);
				editor.putInt("spinLeftValue", 0);
				editor.putInt("spinRightValue", 0);
				editor.commit();

				dismiss();
			}
		});

		builder.setPositiveButton(getString(R.string.OK),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						// a.setLineHeight(editTextLineH.getText()
						// .toString());
						// a.setMargin(editTextTop.getText()
						// .toString(), editTextBottom.getText()
						// .toString(), editTextLeft.getText().toString(),
						// editTextRight.getText().toString());
						if (a != null) {
							a.setCSS();
						} else {
							Log.d("a", "null");
						}

						SharedPreferences.Editor editor = preferences.edit();
						editor.putInt("spinColorValue", colInt);
						editor.putInt("spinBackValue", backInt);
						editor.putInt("spinFontStyleValue", fontInt);
						editor.putInt("spinAlignTextValue", alignInt);
						editor.putInt("spinFontSizeValue", sizeInt);
						editor.putInt("spinLineHValue", heightInt);
						editor.putInt("spinLeftValue", marginLInt);
						editor.putInt("spinRightValue", marginRInt);
						editor.apply();
					}
				});
		builder.setNegativeButton(getString(R.string.Cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		return builder.create();
	}
}
