package com.example.as31_practice_2013;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	Button btnStart, btnSettings;
	final String LOG_SQL = "SQLite_logs";
	
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
		
		
		dbHelper = new DBHelper(this);	
		cv = new ContentValues();
		db = dbHelper.getWritableDatabase();
		
//		Cursor c = db.query("traffic_light", null, null, null, null, null, null);
		
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
		// TODO Auto-generated method stub
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
	
	public class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, "traffic_light", null, 1);
		}
		
		public void onCreate(SQLiteDatabase db) {
			Log.d(LOG_SQL, "Creating DB");
			// time data ("YYYY-MM-DD HH:MM:SS.SSS")
			db.execSQL("create table traffic_light ("
				+ "id integer primary key, "
				+ "timeOn text,"
				+ "timeOff text,"
				+ "x real,"
				+ "y real,"
				+ "green integer,"
				+ "yellow integer,"
				+ "red integer," 
				+ "nextTo text" + ");");
			
		// inserting test traffic light (real - Moskovskaia and Respubliki)
				cv.put("id", 1);
				cv.put("timeOn", "07:00:00");
				cv.put("timeOff", "23:00:00");
				cv.put("x", 52.099594);
				cv.put("y", 23.764149);
				cv.put("green", 35);
				cv.put("yellow", 3);
				cv.put("red", 25);
				cv.put("nextTo", "2, 3, 4");
				long rowID = db.insert("traffic_light", null, cv);
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(LOG_SQL, " --- Updating DB --- ");
			Log.d(LOG_SQL, " from " + oldVersion
			          + " to " + newVersion + " version ");
			// code...
		}
	}

}
