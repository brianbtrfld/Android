package net.briangbutterfield.addressbookfragmentapp;


import java.util.List;
import java.util.Map;

import net.briangbutterfield.addressbookfragmentapp.ContactModel.Contact;

import org.w3c.dom.Comment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListContactFragment extends ListFragment
{

	private IContactControlListener _listener;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Insert Sample Contacts
		//AddressBookModel model = new AddressBookModel(getActivity());
		//model.insertSampleContacts();
		
		setHasOptionsMenu(true);

		refreshContactList();
	}
	
	private void refreshContactList()
	{
		
		List<Contact> contacts = ContactModel.getInstance(getActivity()).getContacts();
		
		ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(getActivity(),
		        						    					  R.layout.fragment_contact_list_item, 
		        						    					  R.id.textViewContactName,
		        						    					  contacts);
		
		setListAdapter(adapter);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflator)
	{
		// Inflate the Menu resource to be associated with
		// this activity.
		getActivity().getMenuInflater().inflate(R.menu.menu_addressbook_activity, menu);

		super.onCreateOptionsMenu(menu, menuInflator);
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

		// Create the GetContactsTask and execute to retrieve the
		// results into a Cursor and assign to adapter.
		// There is nothing to pass to the AsyncTask for a parameter.
//		new LoadContactsTask().execute((Object[]) null);
		
		refreshContactList();
	}

//	@Override
//	public void onStop()
//	{
//
//		// Get the current Cursor from the CursorAdapter and
//		// and close it.
//
//		Cursor cursor = _adapter.getCursor();
//
//		if (cursor != null)
//		{
//			cursor.close();
//		}
//
//		// Adapter no longer should have a populated cursor.
//		_adapter.changeCursor(null);
//
//		super.onStop();
//	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		_listener.contactSelect(id);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_add_contact:
			{
				_listener.contactInsert();
			}
			default:
			{
				return super.onOptionsItemSelected(item);
			}
		}
	}

//	private class LoadContactsTask extends AsyncTask<Object, Object, Cursor>
//	{
//
//		ContactModel _model;
//
//		@Override
//		protected void onPreExecute()
//		{
//			// NOTE: onPreExecute executes on the UI (Main) Thread.
//			// Instance the AddressBookModel object.
//			_model = ContactModel.getInstance(getActivity());
//		}
//
//		@Override
//		protected Cursor doInBackground(Object... params)
//		{
//
//			Cursor returnCursor;
//
//			// NOTE: doInBackground will not execute on the UI Thread.
//
//			// Calls the AddressBookModel to retrieve a Cursor populated
//			// with all of the Contacts.
//
//			// Retrieve all (-1) of the contacts from the database.
//			returnCursor = _model.getContact(-1);
//			
//			return returnCursor;
//		}
//
//		@Override
//		protected void onPostExecute(Cursor result)
//		{
//			// NOTE: onPostExecute executes on the UI (Main) Thread.
//
//			// Assign the populated Cursor from the database query
//			// in the background thread.
//			_adapter.changeCursor(result);
//
//			// Close the database connection. Never leave a
//			// database connection open longer than actually
//			// required.
//			_model.closeDBConnection();
//
//		}
//
//	}

}
