package simplejcqrs.gae.server;

import java.security.InvalidParameterException;

import simplejcqrs.commands.Command;
import simplejcqrs.events.ConcurrancyException;
import simplejcqrs.gae.client.CommandService;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CommandServiceImpl extends RemoteServiceServlet implements
		CommandService {
	@Override
	public void Execute(Command command) {
			SimpleJCQRS.getBus().send(command);	
	}

}
