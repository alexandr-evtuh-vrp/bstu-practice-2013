package com.example.as31_practice_2013;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	Button btnStart, btnSettings;
	final String LOG_SQL = "SQLite_logs";
	final String querry = "create table traffic_light ("
			+ "id integer primary key, "
			+ "timeOn text,"
			+ "timeOff text,"
			+ "x real,"
			+ "y real,"
			+ "green integer,"
			+ "yellow integer,"
			+ "red integer," 
			+ "nextTo text" + ");";
	
	ContentValues cv;
	SQLiteDatabase db;
	DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		btnStart = (Button) findViewById(R.id.btnStart);
		btnSettings = (Button) findViewById(R.id.btnSettings);
		
		btnStart.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		
		
		dbHelper = new DBHelper(this, querry);	
		cv = new ContentValues();
		db = dbHelper.getWritableDatabase();
		
		db.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			Intent intent = new Intent(this, ActivityStart.class);
			startActivity(intent);
			break;			
		case R.id.btnSettings:
			// code...
			break;
		default:
			break;
		}
	}
}
