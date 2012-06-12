package com.innovsys.managemyvmail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AppLocalStorage extends SQLiteOpenHelper
{
	private static final String TAG = "LocalStorage";

	private static final String DB_NAME = "app_storage.db";
	private static final int DB_VERSION = 1;

	public static final String TABLE_LOGIN = "login";
	public static final String KEY_STATUS = "status";
	

	// Create Table Statement.
	private static final String DB_CREATE = "create table " + TABLE_LOGIN 
			                                                + " ( " 
			                                                + KEY_STATUS + " text" 
	                                                        + " );";

	public AppLocalStorage(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "Creating database");
	
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(TAG, "Upgrading database from version " + oldVersion + " to version  " + newVersion);
		
		db.execSQL("drop table if exists " + TABLE_LOGIN);
		
		onCreate(db);
	}

	public String setLoginStatus()
	{
		
		String loginStatus = "false";
		
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery("select status from login;", null);
		
		if (cursor.moveToFirst() == true)
		{
			loginStatus = cursor.getString(0);
			cursor.close();
			cursor = null;
		}
		else
		{
			insertLoginStatus(loginStatus);
		}
		
		db.close();
		
		return loginStatus;
	}

	public void updateLoginStatus(String status)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(AppLocalStorage.KEY_STATUS, status);
		
		db.update(AppLocalStorage.TABLE_LOGIN, values, null, null);
		
		values.clear();
		
		db.close();
	}
	
	public void insertLoginStatus(String status)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(AppLocalStorage.KEY_STATUS, status);
		
		db.insert(AppLocalStorage.TABLE_LOGIN, null, values);		
		
		values.clear();
		
		db.close();
	}
}
