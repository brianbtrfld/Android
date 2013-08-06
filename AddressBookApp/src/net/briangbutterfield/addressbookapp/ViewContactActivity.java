package net.briangbutterfield.addressbookapp;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ViewContactActivity extends Activity
{

	private EditText _editTextName;
	private EditText _editTextPhone;
	private EditText _editTextEmail;
	private EditText _editTextStreet;
	private EditText _editTextCity;
	private Button _buttonSaveContact;

	private long _contactID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Inflate the UI.
		setContentView(R.layout.activity_view_contact);

		// Assign instances of Views from the Layout Resource.
		_editTextName = (EditText) findViewById(R.id.editTextName);
		_editTextPhone = (EditText) findViewById(R.id.editTextPhone);
		_editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		_editTextStreet = (EditText) findViewById(R.id.editTextStreet);
		_editTextCity = (EditText) findViewById(R.id.editTextCityStateZip);
		_buttonSaveContact = (Button) findViewById(R.id.buttonSaveContact);

		// Retrieve the "bundle" of data that was added to the intent
		// and passed to the activity.
		Bundle extras = getIntent().getExtras();

		if (extras != null)
		{
			// If there are extras, it is known to be the ContactID.
			// In this case, retrieve the ContactID and disable
			// all of the views because this is only a "view"
			// of the contact.
			_contactID = extras.getLong(AddressBookModel.KEY_ID);

			// Do not allow editing.
			_editTextName.setEnabled(false);
			_editTextPhone.setEnabled(false);
			_editTextEmail.setEnabled(false);
			_editTextStreet.setEnabled(false);
			_editTextCity.setEnabled(false);

			// Do not display the Save Contact button initially,
			// since the user is only viewing the contact.
			_buttonSaveContact.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (_contactID > 0)
		{
			// If there is a ContactID, execute the AsyncTask
			// to retrieve all of the Contact details to be
			// displayed.
			new LoadContactTask().execute(_contactID);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_view_contact_activity, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		
		switch (item.getItemId())
		{
			case R.id.action_update_contact:
			{
				
				return true;
			}
			case R.id.action_delete_contact:
			{
			
				return true;
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
		
		
	}

	private class LoadContactTask extends AsyncTask<Long, Object, Cursor>
	{

		AddressBookModel _model;

		@Override
		protected void onPreExecute()
		{
			// NOTE: onPreExecute executes on the UI (Main) Thread.
			// Instance the AddressBookModel object.
			_model = new AddressBookModel(ViewContactActivity.this);
		}

		@Override
		protected Cursor doInBackground(Long... params)
		{
			Cursor returnCursor;

			// NOTE: doInBackground will not execute on the UI Thread.

			// Calls the AddressBookModel to retrieve a Cursor populated
			// with the specific Contact details.

			// Open connection to the database.
			_model.openDBConnection();

			// Retrieve the contact from the database.
			returnCursor = _model.getContact(params[0]);

			// Returning the populated cursor to the
			// onPostExecute() method which runs on the UI
			// thread, so the UI views can be populated with
			// the data.
			return returnCursor;
		}

		@Override
		protected void onPostExecute(Cursor result)
		{
			// NOTE: onPostExecute executes on the UI (Main) Thread.

			if (result.moveToFirst() == true)
			{
				_editTextName.setText(result.getString(result.getColumnIndex(AddressBookModel.KEY_NAME)));
				_editTextPhone.setText(result.getString(result.getColumnIndex(AddressBookModel.KEY_PHONE)));
				_editTextEmail.setText(result.getString(result.getColumnIndex(AddressBookModel.KEY_EMAIL)));
				_editTextStreet.setText(result.getString(result.getColumnIndex(AddressBookModel.KEY_STREET)));
				_editTextCity.setText(result.getString(result.getColumnIndex(AddressBookModel.KEY_CITY)));
			}

			// Close the Cursor since the data fields have been extracted.
			result.close();

			// Close the database connection.
			_model.closeDBConnection();

		}

	}

}
