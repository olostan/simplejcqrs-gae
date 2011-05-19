package simplejcqrs.gae.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import com.google.inject.Singleton;

import simplejcqrs.commandhandlers.CommandHandler;
import simplejcqrs.commands.HumanCommands;
import simplejcqrs.events.HumanEvents;
import simplejcqrs.gae.shared.ContactInfo;
import simplejcqrs.structural.EventHandler;

@Singleton
public class QueryHandlers {
		
	public List<ContactInfo> contacts = new ArrayList<ContactInfo>();
	
	@EventHandler
	public void handleHumanRegistred(HumanEvents.HumanRegistred ev,long id) {
		ContactInfo info = new ContactInfo(id);
		info.setFirstName(ev.getFirstName());
		info.setLastName(ev.getLastName());		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//pm.makePersistent(info);
			contacts.add(info);
		}finally { pm.close(); }
	}
}
