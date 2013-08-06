package net.briangbutterfield.addressbookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressBookModel extends SQLiteOpenHelper
{
	
	public static final String KEY_ID = "ContactID";
	public static final String KEY_NAME = "Name";
	public static final String KEY_PHONE = "Phone";
	public static final String KEY_EMAIL = "Email";
	public static final String KEY_STREET = "Street";
	public static final String KEY_CITY = "City";
	
	private static final String TABLE_MYCONTACTS = "MyContacts";
	
	private static final String DATABASE_NAME = "MyAddressBook";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_CREATE_MYCONTACTS =
			"CREATE TABLE " +
					TABLE_MYCONTACTS +
					"(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_NAME + " TEXT, " +
					KEY_PHONE + " TEXT, " +
					KEY_EMAIL + " TEXT, " +
					KEY_STREET + " TEXT, " +
					KEY_CITY + " TEXT);";

	private SQLiteDatabase _db;

	public AddressBookModel(Context context)
	{
		// Call the parent class and pass the actual name and version of the
		// database to be created. The version will be used in the future for
		// determine whether onUpgrade() is called from the SQLiteOpenHelper
		// extension.
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// Execute the CREATE TABLE statement defined as a const.
		db.execSQL(TABLE_CREATE_MYCONTACTS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// If there is ever a need to upgrade the database and/or table.
		// Compare old and new versions to determine if modifications
		// to the database are necessary. Typically, this will be done with
		// ALTER TABLE or CREATE TABLE SQL statements depending on the
		// change required.

		if (newVersion == 2)
		{
			// No version 2 upgrade process yet.
		}
	}

	public void insertContact(String name, String phone, String email, String address, String city)
	{
		// Take parameters and pass to method to populate the
		// ContentValues data structure.
		ContentValues values = populateContentValues(name, phone, email, address, city);

		// Open the database connect, keep it close to the actual operation.
		openDBConnection();

		// Execute query to update the specified contact.
		long rowsAffected = _db.insert(TABLE_MYCONTACTS, null, values);

		// Close the database connection as soon as possible.
		closeDBConnection();

	}

	public void updateContact(int contactID, String name, String phone, String email, String address, String city)
	{
		// Take parameters and pass to method to populate the
		// ContentValues data structure.
		ContentValues values = populateContentValues(name, phone, email, address, city);

		// Open the database connect, keep it close to the actual operation.
		openDBConnection();

		// Execute query to update the specified contact.
		int rowsAffected = _db.update(TABLE_MYCONTACTS,
				values,
				KEY_ID + " = ?",
				new String[] { String.valueOf(contactID) });

		// Close the database connection as soon as possible.
		closeDBConnection();

		if (rowsAffected == 0)
		{
			// The contact row was not updated, what should be done?
		}
	}

	public void deleteContact(int contactID)
	{
		// Open the database connect, keep it close to the actual operation.
		openDBConnection();

		// Execute query to delete the specified contact.
		int rowsAffected = _db.delete(TABLE_MYCONTACTS,
				KEY_ID + " = ?",
				new String[] { String.valueOf(contactID) });

		// Close the database connection as soon as possible.
		closeDBConnection();

		if (rowsAffected == 0)
		{
			// The contact row was not deleted, what should be done?
		}
	}

	public Cursor getContact(int contactID)
	{

		if (contactID == -1)
		{
			// -1 not a legal ID in table, assume all Contacts should be
			// returned.  _id is required by SimpleCursorAdaptor.
			return _db.query(TABLE_MYCONTACTS,
					new String[] { KEY_ID + " as _id", KEY_NAME },
					null,
					null,
					null,
					null,
					KEY_NAME);
		}
		else
		{
			// Return the specific contact row based on ID passed.
			// _id is required by SimpleCursorAdaptor.
			return _db.query(TABLE_MYCONTACTS,
					new String[] { KEY_ID + " as _id", KEY_NAME },
					KEY_ID + "=" + contactID,
					null,
					null,
					null,
					KEY_NAME);
		}

	}

	public void openDBConnection()
	{
		// Opens connection to the database for writing specifically.
		_db = getWritableDatabase();
	}

	public void closeDBConnection()
	{
		if (_db != null)
		{
			// Close connection to database if open.
			_db.close();
		}
	}

	// Common function used to populate the ContentValues to be used in SQL
	// insert
	// or update methods.
	private ContentValues populateContentValues(String name, String phone, String email, String address, String city)
	{
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);
		values.put(KEY_PHONE, phone);
		values.put(KEY_EMAIL, email);
		values.put(KEY_STREET, address);
		values.put(KEY_CITY, city);

		return values;
	}

}
