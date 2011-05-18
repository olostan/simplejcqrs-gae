package simplejcqrs.gae.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("command")
public interface CommandService extends RemoteService {
	void CreateContact(String firstName, String secondName);
}
