package net.briangbutterfield.addressbookfragmentapp;

import net.briangbutterfield.addressbookfragmentapp.ContactModel.Contact;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity implements IContactControlListener
{

	private final static String FRAGMENT_CONTACT_LIST_TAG = "ContactListTag";
	private final static String FRAGMENT_CONTACT_VIEW_TAG = "ContactViewTag";
	
	private FragmentManager _fragmentManager;
	private Fragment _fragmentContactList;
	private Fragment _fragmentViewContact;
	
	private ContactModel _model;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Associate the layout with host activity.
		setContentView(R.layout.activity_main);
		
		// Get a 
		_fragmentManager = getFragmentManager();
		
		_fragmentContactList = _fragmentManager.findFragmentByTag(FRAGMENT_CONTACT_LIST_TAG);
		if (_fragmentContactList == null)
		{
			_fragmentContactList = new ListContactFragment();
		}
		
		_fragmentViewContact = _fragmentManager.findFragmentByTag(FRAGMENT_CONTACT_VIEW_TAG);
		if (_fragmentViewContact == null)
		{
			_fragmentViewContact = new ViewContactFragment();
		}
		
		if (savedInstanceState == null)
		{
			_fragmentManager.beginTransaction()
			                .replace(R.id.fragmentContainerFrame, _fragmentContactList, FRAGMENT_CONTACT_LIST_TAG)
			                .commit();
		}
		
		_model = ContactModel.getInstance(this);
	}
	
	@Override
	public void contactSelect(long contactID)
	{
		Bundle bundle = new Bundle();
		bundle.putLong(ContactModel.KEY_ID, contactID);
		
		_fragmentViewContact.setArguments(bundle);
		
		showContactViewFragment();
		
	}

	@Override
	public void contactInsert()
	{
		showContactViewFragment();
	}

	@Override
	public void contactInsert(Contact contact)
	{
		_model.insertContact(contact);
		_fragmentManager.popBackStackImmediate();
	}

	@Override
	public void contactUpdate(Contact contact)
	{
		_model.updateContact(contact);
		_fragmentManager.popBackStackImmediate();
	}

	@Override
	public void contactDelete(long contactID)
	{
		_model.deleteContact(contactID);
		_fragmentManager.popBackStackImmediate();
	}

	private void showContactViewFragment()
	{
		_fragmentManager.beginTransaction()
				        .replace(R.id.fragmentContainerFrame, _fragmentViewContact, FRAGMENT_CONTACT_VIEW_TAG)
				        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
				        .addToBackStack(null)
				        .commit();
		
	}
}
