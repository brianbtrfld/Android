package com.innovsys.managemyvmail;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TestMessageGenerator extends SQLiteOpenHelper
{
	private static final String TAG = "TestMessageGenerator";

	private static final String DB_NAME = "testmessages.db";
	private static final int DB_VERSION = 1;

	public static final String TABLE_MESSAGES = "messages";
	public static final String KEY_ID = BaseColumns._ID; // Primary Key on table.
	public static final String KEY_PHONE = "phone";
	public static final String KEY_MESSAGE_DATE = "message_date";
	public static final String KEY_MESSAGE_TEXT = "message_text";
	

	// Create Table Statement.
	private static final String DB_CREATE = "create table " + TABLE_MESSAGES 
			                                                + " ( " 
	                                                        + KEY_ID + " integer primary key autoincrement, " 
			                                                + KEY_PHONE + " text, " 
			                                                + KEY_MESSAGE_DATE + " integer, "
			                                                + KEY_MESSAGE_TEXT + " text " 
	                                                        + ");";

	public TestMessageGenerator(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}

	public void InsertTestMessages()
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		//helper.onUpgrade(db, 1, 2);
		
		ContentValues values = new ContentValues();
		
		values.put(TestMessageGenerator.KEY_PHONE, "605-555-1212");
		values.put(TestMessageGenerator.KEY_MESSAGE_DATE, System.currentTimeMillis());
		values.put(TestMessageGenerator.KEY_MESSAGE_TEXT, "this is a test 1");
		
		db.insert(TestMessageGenerator.TABLE_MESSAGES, null, values);
		values.clear();
		
		values.put(TestMessageGenerator.KEY_PHONE, "605-555-1213");
		values.put(TestMessageGenerator.KEY_MESSAGE_DATE, System.currentTimeMillis());
		values.put(TestMessageGenerator.KEY_MESSAGE_TEXT, "this is a test 2");
		
		db.insert(TestMessageGenerator.TABLE_MESSAGES, null, values);
		values.clear();
		
		values.put(TestMessageGenerator.KEY_PHONE, "605-555-1215");
		values.put(TestMessageGenerator.KEY_MESSAGE_DATE, System.currentTimeMillis());
		values.put(TestMessageGenerator.KEY_MESSAGE_TEXT, "this is a test 3");
		
		db.insert(TestMessageGenerator.TABLE_MESSAGES, null, values);
		values.clear();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "Creating database");

		// Execute the Create Table statement.
		db.execSQL(DB_CREATE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(TAG, "Upgrading database from version " + oldVersion + " to version  " + newVersion);
		db.execSQL("drop table if exists " + TABLE_MESSAGES);
		onCreate(db);
	}

}
