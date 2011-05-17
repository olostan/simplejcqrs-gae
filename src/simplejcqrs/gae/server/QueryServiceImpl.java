package simplejcqrs.gae.server;

import simplejcqrs.gae.client.QueryService;
import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class QueryServiceImpl extends RemoteServiceServlet implements
		QueryService {

	@Override
	public ContactInfo[] GetContacts() {
		ContactInfo i1 = new ContactInfo();
		i1.setFirstName("first");
		i1.setLastName("firstovich");
		
		ContactInfo i2 = new ContactInfo();
		i2.setFirstName("second");
		i2.setLastName("secondovich");
		
		ContactInfo[] infos = new ContactInfo[] {
			i1,i2
		};
		return infos;
	}

}
