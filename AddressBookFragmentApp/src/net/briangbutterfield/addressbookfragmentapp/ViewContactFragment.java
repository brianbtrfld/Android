package net.briangbutterfield.addressbookfragmentapp;

import net.briangbutterfield.addressbookfragmentapp.ContactModel.Contact;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ViewContactFragment extends Fragment
{

	private IContactControlListener _listener;
	
	private EditText _editTextName;
	private EditText _editTextPhone;
	private EditText _editTextEmail;
	private EditText _editTextStreet;
	private EditText _editTextCity;
	private Button _buttonSaveContact;

	private long _contactID = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		// Inflate the UI.
		View rootView = inflater.inflate(R.layout.fragment_view_contact, container, false);

		// Assign instances of Views from the Layout Resource.
		_editTextName = (EditText) rootView.findViewById(R.id.editTextName);
		_editTextPhone = (EditText) rootView.findViewById(R.id.editTextPhone);
		_editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
		_editTextStreet = (EditText) rootView.findViewById(R.id.editTextStreet);
		_editTextCity = (EditText) rootView.findViewById(R.id.editTextCityStateZip);
		
		_buttonSaveContact = (Button) rootView.findViewById(R.id.buttonSaveContact);

		_buttonSaveContact.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (_editTextName.getText().toString().isEmpty() == false)
				{
					Contact contact = new ContactModel.Contact();
					contact.ContactID = _contactID;
					contact.Name = _editTextName.getText().toString();
					contact.Phone = _editTextPhone.getText().toString();
					contact.Email = _editTextEmail.getText().toString();
					contact.Street = _editTextStreet.getText().toString();
					contact.City = _editTextCity.getText().toString();
					
					if (_contactID > 0)
					{
						// Update the contact in the database.
						_listener.contactUpdate(contact);
					}
					else
					{
						// Insert the contact into the database.
						_listener.contactInsert(contact);
					}
					
				}
				else
				{
					// Alert the user of missing Name field.

					AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
					alertBuilder.setTitle(R.string.alert_title_missinginfo);
					alertBuilder.setMessage(R.string.alert_message_missing_name);
					alertBuilder.setPositiveButton(R.string.alert_button_OK, null);
					alertBuilder.show();
				}
				
			}
		});

		return rootView;
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// Retrieve the bundle "args" of data that was added to the intent
		// and passed to the fragment.
		Bundle args = getArguments();
		
		if (args != null)
		{
			// If there are extras, it is known to be the ContactID.
			// In this case, retrieve the ContactID and disable
			// all of the views because this is only a "view"
			// of the contact.
			_contactID = args.getLong(ContactModel.KEY_ID);

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
	public void onAttach(Activity activity)
	{
		try
		{
			// Assign listener reference from hosting activity.
			_listener = (IContactControlListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString());
		}
		
		super.onAttach(activity);
	}

	@Override
	public void onResume()
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
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.menu_view_contact_activity, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{

		switch (item.getItemId())
		{
			case R.id.action_update_contact:
			{
				// Allow for editing on the fields.
				_editTextName.setEnabled(true);
				_editTextPhone.setEnabled(true);
				_editTextEmail.setEnabled(true);
				_editTextStreet.setEnabled(true);
				_editTextCity.setEnabled(true);

				// Display the Save Contact button.
				_buttonSaveContact.setVisibility(View.VISIBLE);

				return true;
			}
			case R.id.action_delete_contact:
			{
				// Do not allow editing.
				_editTextName.setEnabled(false);
				_editTextPhone.setEnabled(false);
				_editTextEmail.setEnabled(false);
				_editTextStreet.setEnabled(false);
				_editTextCity.setEnabled(false);

				_buttonSaveContact.setVisibility(View.INVISIBLE);

				// Delete Contact from the database.
				_listener.contactDelete(_contactID);

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

		ContactModel _model;

		@Override
		protected void onPreExecute()
		{
			// NOTE: onPreExecute executes on the UI (Main) Thread.
			// Instance the AddressBookModel object.
			_model = ContactModel.getInstance(getActivity());
		}

		@Override
		protected Cursor doInBackground(Long... params)
		{
			Cursor returnCursor = null;

			// NOTE: doInBackground will not execute on the UI Thread.

			// Calls the AddressBookModel to retrieve a Cursor populated
			// with the specific Contact details.

			// Retrieve the contact from the database.
			//returnCursor = _model.getContact(params[0]);

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
				_editTextName.setText(result.getString(result.getColumnIndex(ContactModel.KEY_NAME)));
				_editTextPhone.setText(result.getString(result.getColumnIndex(ContactModel.KEY_PHONE)));
				_editTextEmail.setText(result.getString(result.getColumnIndex(ContactModel.KEY_EMAIL)));
				_editTextStreet.setText(result.getString(result.getColumnIndex(ContactModel.KEY_STREET)));
				_editTextCity.setText(result.getString(result.getColumnIndex(ContactModel.KEY_CITY)));
			}

			// Close the Cursor since the data fields have been extracted.
			result.close();

			// Close the database connection.
			//_model.closeDBConnection();

		}

	}

}
