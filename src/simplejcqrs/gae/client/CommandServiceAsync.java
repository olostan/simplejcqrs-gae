package simplejcqrs.gae.client;

import simplejcqrs.commands.Command;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CommandServiceAsync {
	//void CreateContact(String firstName, String secondName,	AsyncCallback<Void> callback);

	void Execute(Command command, AsyncCallback<Void> callback);
}
