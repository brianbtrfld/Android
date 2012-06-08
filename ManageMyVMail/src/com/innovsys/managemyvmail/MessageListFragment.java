package com.innovsys.managemyvmail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MessageListFragment extends ListFragment implements ViewBinder
{

	private static final String[] columnNames = { TestMessageGenerator.KEY_PHONE, TestMessageGenerator.KEY_MESSAGE_DATE, TestMessageGenerator.KEY_MESSAGE_TEXT };
	private static final int[] valueMap = { R.id.textViewPhone, R.id.textViewDate, R.id.textViewMessageText };
	private Cursor m_cursor;
	private SimpleCursorAdapter m_adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		
		//Forces the state retention of the Fragment when an Activity is destroyed and
        //re-created, this tells the Activity Manager to not call onCreate or onDestroy
        //methods of the Fragment, hence retaining the object in memory while the associated
        //Activity goes through its life cycle.
        setRetainInstance(true);
        
		setHasOptionsMenu(true);
		
		SQLiteDatabase db = new TestMessageGenerator(getActivity().getApplicationContext()).getWritableDatabase();
		
		m_cursor = db.query(TestMessageGenerator.TABLE_MESSAGES, 
				            null, null, null, null, null, 
				            TestMessageGenerator.KEY_ID + " desc");
		
		m_adapter = new SimpleCursorAdapter(getActivity().getApplicationContext(),
											R.layout.messagelist_row, 
											m_cursor, 
											columnNames, valueMap, 0);

		m_adapter.setViewBinder(this);

		setListAdapter(m_adapter);
	
	}

	@Override
	public void onResume()
	{
		super.onResume();
		m_cursor.requery();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		m_cursor.deactivate();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		m_cursor.close();
	}

	public boolean setViewValue(View view, Cursor cursor, int columnIndex)
	{
		int id = view.getId();

		switch (id)
		{
			case R.id.textViewDate:
				
				// customize appearance of the date
				long timestamp = cursor.getLong(columnIndex);
				// provides string ex. 1 hour ago, 3 mins ago, etc.
				CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp);
				((TextView) view).setText(relativeTime);
				
				return true;
	
			default:
				// Let the SimpleCursorAdapter perform the default binding of data.
				return false;
		}
	}

	@Override
	public void onListItemClick(ListView l, View view, int position, long id)
	{
		Intent intent = new Intent(view.getContext(), MessageDetailActivity.class);
		intent.putExtra("position", String.valueOf(position));
		intent.putExtra("id", String.valueOf(id));
		startActivity(intent);
	}
}
