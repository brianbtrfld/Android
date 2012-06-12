package com.innovsys.managemyvmail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MessageDetailFragment extends Fragment
{

	private String m_stringID = "Not Set";
	private String m_stringPosition = "Not Set";
	
	private EditText m_editTextPosition;
	private EditText m_editTextId;
	
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
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{	
		//Inflate the fragment user interface which creates the View hierarchy.
		View top = inflater.inflate(R.layout.messagedetail_fragment, container, false);
				
		m_editTextPosition = (EditText)top.findViewById(R.id.editText1);
		m_editTextId = (EditText)top.findViewById(R.id.editText2);
		
		return top;
	}
	
	public void setMessageDetails(String id, String position)
	{
		m_stringID = id;
		m_stringPosition = position;
		
		m_editTextPosition.setText("Position: " + m_stringPosition);
		m_editTextId.setText("ID: " + m_stringID);
	}

}
