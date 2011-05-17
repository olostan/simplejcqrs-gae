package simplejcqrs.gae.client;

import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("query")
public interface QueryService extends RemoteService {
	ContactInfo[] GetContacts();
}
