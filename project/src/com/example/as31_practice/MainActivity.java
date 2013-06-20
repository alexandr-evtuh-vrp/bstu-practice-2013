package com.example.as31_practice;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
		
		// Show rows
		Log.d(LOG_SQL, "--- Rows in traffic_light: ---");		
		Cursor c = db.query("traffic_light", null, null, null, null, null, null);
		
		if (c.moveToFirst()) {
			
			int idColIndex = c.getColumnIndex("id");
			int timeOnColIndex = c.getColumnIndex("timeOn");
			int timeOffColIndex = c.getColumnIndex("timeOff");
			int xColIndex = c.getColumnIndex("x");
			int yColIndex = c.getColumnIndex("y");
			int greenColIndex = c.getColumnIndex("green");
			int yellowColIndex = c.getColumnIndex("yellow");
			int redColIndex = c.getColumnIndex("red");
			int nextToColIndex = c.getColumnIndex("nextTo");
			
			do {
				Log.d(LOG_SQL, 
					"id = " + c.getInt(idColIndex) +
					", timeOn = " + c.getString(timeOnColIndex) +
					", timeOff = " + c.getString(timeOffColIndex) +
					", x = " + c.getDouble(xColIndex) +
					", y = " + c.getDouble(yColIndex) +
					", green = " + c.getInt(greenColIndex) +
					", yellow = " + c.getInt(yellowColIndex) +
					", red = " + c.getInt(redColIndex) +
					", nextTo = " + c.getString(nextToColIndex));				
			} while (c.moveToNext());
		} else {
			Log.d(LOG_SQL, "0 rows in table traffic_light: ---");
			c.close();
		}
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
	
	class DBHelper extends SQLiteOpenHelper {
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
			
			// Inserting test data into DB
			Log.d(LOG_SQL, "--- Inserting rows into table traffic_light: ---");
			int n = 0;
			for (int i = 0; i < 13; i += 6) {
				for (int j = 0; j < 13; j += 6) {
					cv.put("id", ++n);
					cv.put("timeOn", "07:00:00");
					cv.put("timeOff", "22:00:00");
					cv.put("x", (double) j);
					cv.put("y", (double) i);
					cv.put("green", 35);
					cv.put("yellow", 3);
					cv.put("red", 25);
					cv.put("nextTo", getNextTo(n));
					long rowID = db.insert("traffic_light", null, cv);
					Log.d(LOG_SQL, "Insert row id = " + rowID);
				}
			}
		}
		
		public String getNextTo(int temp) {
			switch (temp) {
			case 1:
				return "2, 4";
			case 2:
				return "1, 3, 5";
			case 3:
				return "2, 6";
			case 4:
				return "1, 5, 7";
			case 5:
				return "2, 4, 6, 8";
			case 6:
				return "3, 5, 9";
			case 7:
				return "4, 8";
			case 8:
				return "5, 7, 9";
			case 9:
				return "6, 8";
			default:
				return "0";
			}
		}
		
		// 
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(LOG_SQL, " --- Updating DB --- ");
			Log.d(LOG_SQL, " from " + oldVersion
			          + " to " + newVersion + " version ");
			// code...
		}
	}

}
