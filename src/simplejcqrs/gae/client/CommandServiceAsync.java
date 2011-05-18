package simplejcqrs.gae.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommandServiceAsync {
	void CreateContact(String firstName, String secondName,
			AsyncCallback<Void> callback);
}
