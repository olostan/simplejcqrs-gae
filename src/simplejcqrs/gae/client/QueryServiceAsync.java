package simplejcqrs.gae.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import simplejcqrs.gae.shared.ContactInfo;

public interface QueryServiceAsync {
	void GetContacts(AsyncCallback<ContactInfo[]> callback);	
}
