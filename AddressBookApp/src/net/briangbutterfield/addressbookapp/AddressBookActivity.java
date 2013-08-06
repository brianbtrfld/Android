package net.briangbutterfield.addressbookapp;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AddressBookActivity extends ListActivity
{

	private AddressBookModel _model;
	private ListAdapter _adapter;
	private Cursor _cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		_model = new AddressBookModel(this);
		
		//_model.insertContact("Brian Butterfield", "996", "brian@brian.com", "123 Main Street", "RC");
		//_model.deleteContact(3);

		_model.openDBConnection();
		
		_cursor = _model.getContact(-1);

		_adapter = new SimpleCursorAdapter(this,
				R.layout.contact_list_item,
				_cursor,
				new String[] { AddressBookModel.KEY_NAME },
				new int[] { R.id.textViewContactName }, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		setListAdapter(_adapter);
		
		_model.closeDBConnection();

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
	
	private class GetContactsTask extends AsyncTask<Object, Object, Cursor>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Cursor doInBackground(Object... params)
		{
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Cursor result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		
	}

}
