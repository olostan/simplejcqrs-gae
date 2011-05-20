package simplejcqrs.gae.client;

import simplejcqrs.commands.Command;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("command")
public interface CommandService extends RemoteService {
	void Execute(Command command);
}
