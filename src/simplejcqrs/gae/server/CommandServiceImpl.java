package simplejcqrs.gae.server;

import java.util.Random;

import javax.jdo.PersistenceManager;

import simplejcqrs.gae.client.CommandService;
import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CommandServiceImpl extends RemoteServiceServlet implements
		CommandService {

	@Override
	public void CreateContact(String firstName, String secondName) {
		ContactInfo info = new ContactInfo(Integer.toString((new Random()).nextInt()));
		info.setFirstName(firstName);
		info.setLastName(secondName);		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(info);
		}finally { pm.close(); }
	}

}
