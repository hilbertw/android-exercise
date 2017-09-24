package com.droidterm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ShellActivity extends Activity {
	private TextView tv1;
	private EditText tv2;
	private TextView tv3;

	private boolean bin;
	protected String output;

	private static final String TAG = "SceenCap";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// text.setText("Hello World, Android - mkyong.com");
		// setContentView(text);
		setContentView(R.layout.main);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (EditText) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv3.setMovementMethod(new ScrollingMovementMethod());
		final CheckBox cb = (CheckBox) findViewById(R.id.checkbox1);
		cb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bin = cb.isChecked();
			}
		});
		Button ok = (Button) findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cmd = tv2.getText().toString();

				if (!bin) {
					execCommandLine(cmd);

				} else {

					execCommandLineBin(cmd);
				}

			}

		});
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:

				break;
			case 1:
				tv3.setText(output);
				break;
			}
		}
	};

	public static String bytesToHex(char[] buffer, int n) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(String.format("%02x", (byte) buffer[i]));
		}
		return builder.toString();
	}

	public void execCommandLine(final String cmd) {
		// ***********************
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuilder builder = new StringBuilder();
				try {
					Runtime rt = Runtime.getRuntime();

					Process proc = rt.exec(cmd);
					InputStream is = proc.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);
					String line;
					
					proc.waitFor();
					Log.i(TAG, "exit:" + proc.exitValue());
					
					while ((line = br.readLine()) != null) {
						Log.i(TAG, line);
						builder.append(line + "\n");
					}
				} catch (IOException ex) {
					Log.e(TAG, ex.fillInStackTrace().toString());
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					Log.e(TAG, ex.fillInStackTrace().toString());
				}
				Log.i(TAG, "exec " + cmd);
				output = builder.toString();
				handler.sendEmptyMessage(1);
			}
		}).start();
	}

	public void execCommandLineBin(final String cmd) {
		// ***********************
		new Thread(new Runnable() {
			@Override
			public void run() {
				StringBuilder builder = new StringBuilder();
				try {
					Runtime rt = Runtime.getRuntime();

					Process proc = rt.exec(cmd);
					InputStream is = proc.getInputStream();
					InputStreamReader isr = new InputStreamReader(is);
					BufferedReader br = new BufferedReader(isr);

					char[] buf = new char[16];

					proc.waitFor();
					Log.i(TAG, "exit:" + proc.exitValue());

					int n;
					while ((n = br.read(buf)) > 0) {
						String line = bytesToHex(buf, n);
						Log.i(TAG, line);
						builder.append(line + "\n");
					}
				} catch (IOException ex) {
					Log.e(TAG, ex.fillInStackTrace().toString());
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					Log.e(TAG, ex.fillInStackTrace().toString());
				}

				// TODO Auto-generated method stub

				Log.i(TAG, "exec " + cmd);
				output = builder.toString();
				handler.sendEmptyMessage(1);
			}

		}).start();
	}
}
