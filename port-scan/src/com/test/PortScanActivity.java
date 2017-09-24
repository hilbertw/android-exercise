package com.test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PortScanActivity extends Activity {

	private static TextView tv2;

	//private Context Ctx;
	private ListView lv;
	public static final String TAG = "PortScan";

	ArrayList<String> listItems = new ArrayList<String>();
	static ArrayAdapter<String> adapter;
	private ProgressBar pb;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Ctx = getApplicationContext();

		// setContentView(text);
		setContentView(R.layout.main);

		tv2 = (TextView) findViewById(R.id.textView2);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		lv = (ListView) findViewById(R.id.listView1);
		Button ok = (Button) findViewById(R.id.button1);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scan();
			}
		});
		adapter = new ArrayAdapter<String>(this, R.layout.simple_row, listItems);
		lv.setAdapter(adapter);

		pb.setMax(65535);
	}

	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				adapter.notifyDataSetChanged();
				break;
			case 1:
				tv2.setText("Done");
				break;
			}
		}
	};

	public void addPort(int port) {
		StringBuilder builder = new StringBuilder();

		builder.append(String.format("%d", port));
		listItems.add(builder.toString());
		handler.sendEmptyMessage(0);
	}

	public void scan() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int i;
				for (i = 1024; i < 65535; i++) {
					StringBuilder builder = new StringBuilder();

					Socket socket = new Socket();

					SocketAddress remoteAddr = new InetSocketAddress(
							"localhost", i);
					pb.setProgress(i);
					try {
						socket.connect(remoteAddr);
						addPort(i);
					} catch (ConnectException ex) {
					} catch (java.net.SocketException ex) {
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						Log.e(TAG, ex.fillInStackTrace().toString());
					}
				}

				handler.sendEmptyMessage(1);

			}
		}).start();
	}
}