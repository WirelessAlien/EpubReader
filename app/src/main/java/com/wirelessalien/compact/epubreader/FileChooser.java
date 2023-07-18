package com.wirelessalien.compact.epubreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FileChooser extends AppCompatActivity {

	private static final int FILE_PICKER_REQUEST_CODE = 1;
	private Button pickFileButton;
	private ProgressBar progressBar;
	private Executor executor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_chooser_layout);

		pickFileButton = findViewById(R.id.pickFileBtn);
		progressBar = findViewById(R.id.progressBar);
		executor = Executors.newSingleThreadExecutor();
		progressBar.setVisibility(View.GONE);

		pickFileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressBar.setVisibility(View.VISIBLE);
				openFilePicker();
			}
		});

		// Check if the activity was started by sharing a file
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("application/epub+zip".equals(type)) {
				Uri sharedFileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
				if (sharedFileUri != null) {
					handleSharedFile(sharedFileUri);
				}
			}
		}
	}

	private void handleSharedFile(Uri sharedFileUri) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				String filePath = createCopyAndReturnRealPath(sharedFileUri);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onCopyFileTaskCompleted(filePath);
					}
				});
			}
		});
	}


	private void openFilePicker() {
		pickFileButton.setVisibility(View.GONE); // Hide the pickFileButton
		progressBar.setVisibility(View.VISIBLE); // Show the progressBar or any other UI element
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("application/epub+zip");
		startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					executor.execute(new Runnable() {
						@Override
						public void run() {
							String filePath = createCopyAndReturnRealPath(uri);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									onCopyFileTaskCompleted(filePath);
								}
							});
						}
					});
				}
			}
		}
	}

	@Nullable
	private String createCopyAndReturnRealPath(Uri uri) {
		final ContentResolver contentResolver = getContentResolver();
		if (contentResolver == null)
			return null;

		// Create file path inside app's data dir
		String filePath = getApplicationInfo().dataDir + File.separator + "temp_file";
		File file = new File(filePath);
		try {
			InputStream inputStream = contentResolver.openInputStream(uri);
			if (inputStream == null)
				return null;
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			int totalBytesRead = 0;
			while ((len = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, len);
				totalBytesRead += len;
				publishProgress(totalBytesRead);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException ignore) {
			return null;
		}
		return file.getAbsolutePath();
	}

	private void onCopyFileTaskCompleted(String filePath) {
		progressBar.setVisibility(View.GONE); // Hide the progressBar or any other UI element
		pickFileButton.setVisibility(View.VISIBLE); // Show the pickFileButton
		if (filePath != null) {
			Intent intent = new Intent(FileChooser.this, MainActivity.class);
			intent.putExtra("epub_location", filePath);
			startActivity(intent);
			Toast.makeText(this, "Please wail till rendering completed", Toast.LENGTH_SHORT).show();
		}
	}

	private void publishProgress(int bytesRead) {
		int totalBytes = progressBar.getMax();
		progressBar.setProgress((int) (bytesRead * 100.0 / totalBytes));
	}

}



