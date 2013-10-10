package net.briangbutterfield.addressbookfragmentapp;

import net.briangbutterfield.addressbookfragmentapp.ContactModel.Contact;

public interface IContactControlListener
{
	public void contactSelect(long id);
	public void contactInsert();
	public void contactInsert(Contact contact);
	public void contactUpdate(Contact contact);
	public void contactDelete(long contactID);

}
