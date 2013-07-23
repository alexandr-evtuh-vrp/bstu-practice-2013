package com.example.as31_practice_2013;

import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ActivityStart extends Activity implements LocationListener {
	
	LocationManager myManager;
	String best;
	ContentValues cv;
	SQLiteDatabase db;
	DBHelper dbHelper;
	Cursor c;

	final String TAG = "lstart";
	
	TextView tvStreets, tvSpeed1, tvSpeed2, tvSpeed3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lstart);
		
		myManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		myManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		best = myManager.getBestProvider(crit, false);
		
		tvStreets = (TextView) findViewById(R.id.tvStreets);
		tvSpeed1 = (TextView) findViewById(R.id.tvSpeed1);
		tvSpeed2 = (TextView) findViewById(R.id.tvSpeed2);
		tvSpeed3 = (TextView) findViewById(R.id.tvSpeed3);
		
		dbHelper = new DBHelper(this, "");
		cv = new ContentValues();
		db = dbHelper.getReadableDatabase();
		
		c = db.query("traffic_light", null, null, null, null, null, null);
		
		greenTime();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d("lstart", "changed");
		this.getSpeed(location.getLatitude(), location.getLongitude());
		Log.d("lstart", "" + location.getSpeed());
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		tvStreets.setText("Disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		tvStreets.setText("Enabled");
		Log.d("lstart", "enabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void getSpeed(double startLatitude, double startLongitude) {
		c.moveToFirst();
		int xColIndex = c.getColumnIndex("x");
		int yColIndex = c.getColumnIndex("y");
		double endLatitude = c.getDouble(xColIndex);
		double endLongitude = c.getDouble(yColIndex);
		
		//
		tvSpeed1.setText("From:\n" + "Latitude " + startLatitude + "\nLongtitude " + startLongitude);
		tvSpeed2.setText("\nTo:\n" + "Latitude " + endLatitude + "\nLongtitude " + endLongitude);
	
		Log.d("user", "greenTime() = " + greenTime());
		
		// Approximate distance in meters between two locations
		float[] distance = {0};
		Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, distance);
		
		double speed = distance[0] / greenTime() * 3.6;
		
		tvSpeed3.setText("\nSpeed " + speed + "\nDistanceFunc " + distance[0] + "\nTime for green " 
				+ greenTime());
		
	}
	
	@SuppressWarnings("deprecation")
	public int greenTime() {
		c.moveToFirst();
		int timeOnColIndex = c.getColumnIndex("timeOn");
		int timeOffColIndex = c.getColumnIndex("timeOff");
		int greenColIndex = c.getColumnIndex("green");
		int yellowColIndex = c.getColumnIndex("yellow");
		int redColIndex = c.getColumnIndex("red");
		
		String[] timeOn = c.getString(timeOnColIndex).split(":");
		String[] timeOff = c.getString(timeOffColIndex).split(":");
		int green = c.getInt(greenColIndex);
		int yellow = c.getInt(yellowColIndex);
		int red = c.getInt(redColIndex);
		
		int round = green + yellow + red;
		int point = green / 2;
		
		Date date = new Date();
		Date constDate = new Date();
		Date finishDate = new Date();
		constDate.setHours(Integer.parseInt(timeOn[0]));		
		constDate.setMinutes(Integer.parseInt(timeOn[1]));
		constDate.setSeconds(Integer.parseInt(timeOn[2]));
		finishDate.setHours(Integer.parseInt(timeOff[0]));
		finishDate.setMinutes(Integer.parseInt(timeOff[1]));
		finishDate.setSeconds(Integer.parseInt(timeOff[2]));

		// time from start traffic light to current time
		long dTime = (date.getTime() - constDate.getTime()) / 1000;
		// time to red color
		int timeLeft = (int) (round - (dTime % round));
		// return time from current to half of green light
		return ((timeLeft > point) ? (timeLeft - point) : (round + timeLeft - point));
	}
}


