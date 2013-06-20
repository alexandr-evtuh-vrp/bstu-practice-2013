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
		
		// Добавление тестовых(почти одинаковых) данных в БД // TEST!!!
		// пример работы с бд
		// добавление производим 1 раз!!!		
//		Log.d(LOG_SQL, "--- Добавление строк в таблицу traffic_light: ---");
//		for (int i = 1; i < 7; i++){
//			cv.put("id", i);
//			cv.put("timeOn", "07:00:00");
//			cv.put("timeOff", "22:00:00");
//			cv.put("x", 53.17462);
//			cv.put("y", 34.94832);
//			cv.put("green", 35);
//			cv.put("yellow", 3);
//			cv.put("red", 25);
//			cv.put("nextTo", "2, 3, 4");
//			long rowID = db.insert("traffic_light", null, cv);
//			Log.d(LOG_SQL, "Добавлена строка id = " + rowID);
//		}
		
		// Проверка наличия данных в БД
		Log.d(LOG_SQL, "--- Чтение строк из таблицы traffic_light: ---");		
		Cursor c = db.query("traffic_light", null, null, null, null, null, null);
		
		if (c.moveToFirst()) {
			
			// Определяем номера столбцов по имени в выборке
			int idColIndex = c.getColumnIndex("id");
			int timeOnColIndex = c.getColumnIndex("timeOn");
			int timeOffColIndex = c.getColumnIndex("timeOff");
			int xColIndex = c.getColumnIndex("x");
			int yColIndex = c.getColumnIndex("y");
			int greenColIndex = c.getColumnIndex("green");
			int yellowColIndex = c.getColumnIndex("yellow");
			int redColIndex = c.getColumnIndex("red");
			int nextToColIndex = c.getColumnIndex("nextTo");
			
			// Получаем значения
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
			Log.d(LOG_SQL, "Прочитано 0 строк из таблицы traffic_light: ---");
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
		
		// Подключение к БД, которой не существует
		public void onCreate(SQLiteDatabase db) {
			Log.d(LOG_SQL, "Создание БД");
			// Создание таблицы с полями
			// Тип данных дата время в SQLite - строка формата ISO8601 ("YYYY-MM-DD HH:MM:SS.SSS")
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
		}
		
		// Подключение к БД более новой версии, чем существующая
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d(LOG_SQL, " --- Обновление БД --- ");
			Log.d(LOG_SQL, " c " + oldVersion
			          + " по " + newVersion + " версию ");
			// code...
			// транзакция модификации бд...
			// будет реализовано при необходимости при наличии реальных данных
		}
	}

}
