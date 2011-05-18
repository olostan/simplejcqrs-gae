package simplejcqrs.gae.client;

import com.google.gwt.core.client.GWT;

public class CommandSender {	
	private final CommandServiceAsync commandService = GWT.create(CommandService.class);
	private static CommandSender instance;
	public static CommandServiceAsync get() {
	    if (instance == null) {
	      instance = new CommandSender();
	    }
	    return instance.commandService;
	}
	
}
