package com.example.as31_practice_2013;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public String textQuerry;
	public DBHelper(Context context, String text) {
		super(context, "traffic_light", null, 1);
		textQuerry = text;
		Log.d("dbHelper", "Constructor dbHelper");
	}
	
	// Called when the database has been opened
	public void onOpen(SQLiteDatabase db) {
		Log.d("dbHelper", "Database has been opened");
	}
	
	// Called when the database is created for the first time
	public void onCreate(SQLiteDatabase db) {
		Log.d("dbHelper", "Database has been created");
		db.execSQL(textQuerry);

		// inserting test traffic light (real - Moskovskaia and Respubliki)
		ContentValues cv = new ContentValues();
		cv.put("id", 1);
		cv.put("timeOn", "07:00:00");
		cv.put("timeOff", "23:00:00");
		cv.put("x", 52.099594);
		cv.put("y", 23.764149);
		cv.put("green", 35);
		cv.put("yellow", 3);
		cv.put("red", 25);
		cv.put("nextTo", "2, 3, 4");
		db.insert("traffic_light", null, cv);
		Log.d("dbHelper", "After insert");
	}
	
	// Called when the database needs to be upgraded
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("dbHelper", "Database has been upgraded");
		// code...
	}

}
