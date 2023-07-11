package com.wirelessalien.compact.epubreader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileChooser extends AppCompatActivity {

	private static final int FILE_PICKER_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_chooser_layout);

		Button pickFileButton = findViewById(R.id.pickFileBtn );
		pickFileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openFilePicker();
			}
		});
	}

	private void openFilePicker() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (uri != null) {
					String filePath = createCopyAndReturnRealPath(this, uri);
					if (filePath != null) {
						Intent intent = new Intent(this, MainActivity.class);
						intent.putExtra("epub_location", filePath);
						startActivity(intent);
					}
				}
			}
		}
	}

	public static String createCopyAndReturnRealPath(
			@NonNull Activity activity, @NonNull Uri uri) {
		final ContentResolver contentResolver = activity.getContentResolver();
		if (contentResolver == null)
			return null;

		// Create file path inside app's data dir
		String filePath = activity.getApplicationInfo().dataDir + File.separator + "temp_file";
		File file = new File(filePath);
		try {
			InputStream inputStream = contentResolver.openInputStream(uri);
			if (inputStream == null)
				return null;
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				outputStream.write(buf, 0, len);
			outputStream.close();
			inputStream.close();
		} catch (IOException ignore) {
			return null;
		}
		return file.getAbsolutePath();
	}
}


