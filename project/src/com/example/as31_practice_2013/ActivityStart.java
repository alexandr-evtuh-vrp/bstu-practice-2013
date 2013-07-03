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
		// TODO Auto-generated method stub
		this.speed(location.getLatitude(), location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		tvStreets.setText("Disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		tvStreets.setText("Enabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void speed(double xLat, double yLong) {
		c.moveToFirst();
		int xColIndex = c.getColumnIndex("x");
		int yColIndex = c.getColumnIndex("y");
		double lat = c.getDouble(xColIndex);
		double lon = c.getDouble(yColIndex);
		
		//
		tvSpeed1.setText("From:\n" + "Latitude " + xLat + "\nLongtitude " + yLong);
		tvSpeed2.setText("\nTo:\n" + "Latitude " + lat + "\nLongtitude " + lon);
		
		// 
		xLat *= Math.PI / 180;
		lat *= Math.PI / 180;
		yLong *= Math.PI / 180;
		lon *= Math.PI / 180;
		// 
		double cl1 = Math.cos(xLat);
		double cl2 = Math.cos(lat);
		double sl1 = Math.sin(xLat);
		double sl2 = Math.sin(lat);
		double delta = lon - yLong;
		double cDelta = Math.cos(delta);
		double sDelta = Math.sin(delta);
		
		//
		double y = Math.sqrt(Math.pow(cl2 * sDelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cDelta, 2));
		double x = sl1 * sl2 + cl1 * cl2 * cDelta;
		double ad = Math.atan2(y, x);
		double dist = ad * 6372795;
		
		Log.d("user", "greenTime() = " + greenTime());
		
		double v = dist / greenTime() * 3.6; 
		
		tvSpeed3.setText("\nSpeed " + v + "\nDistance " + dist + "\nTime for green " 
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


