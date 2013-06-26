package com.example.as31_practice_2013;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
		Location loc = myManager.getLastKnownLocation(best);
		
		tvStreets = (TextView) findViewById(R.id.tvStreets);
		tvSpeed1 = (TextView) findViewById(R.id.tvSpeed1);
		tvSpeed2 = (TextView) findViewById(R.id.tvSpeed2);
		tvSpeed3 = (TextView) findViewById(R.id.tvSpeed3);
		
		dbHelper = new DBHelper(this);
		cv = new ContentValues();
		db = dbHelper.getReadableDatabase();
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
		Cursor c = db.query("traffic_light", null, null, null, null, null, null);
		c.moveToFirst();
		int xColIndex = c.getColumnIndex("x");
		int yColIndex = c.getColumnIndex("y");
		double lat = c.getDouble(xColIndex);
		double lon = c.getDouble(yColIndex);
		
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
		
		tvSpeed1.setText("From:\n" + "Latitude " + xLat + "\nLongtitude " + yLong);
		tvSpeed2.setText("\nTo:\n" + "Latitude " + lat + "\nLongtitude " + lon);
		tvSpeed3.setText("\nDistance " + dist + "\n");
	}
	public class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, "traffic_light", null, 1);
		}
		
		public void onCreate(SQLiteDatabase db) {
			Log.d(TAG, "Creating DB");
			// 
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(TAG, " --- Updating DB --- ");
			Log.d(TAG, " from " + oldVersion
				+ " to " + newVersion + " version ");
			// code...
		}
	}
}


