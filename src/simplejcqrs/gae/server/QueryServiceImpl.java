package simplejcqrs.gae.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import simplejcqrs.domain.Repository;
import simplejcqrs.gae.client.QueryService;
import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class QueryServiceImpl extends RemoteServiceServlet implements
		QueryService {

	private final static ContactInfo[] emptyContacts = new ContactInfo[0];
	@Override
	public ContactInfo[] GetContacts() {
		
		ContactInfo[] infos = null;
		/*
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(ContactInfo.class);
			try {	
				List<ContactInfo> results = (List<ContactInfo>) query.execute();
				if (!results.isEmpty()) {
					infos = results.toArray(new ContactInfo[results.size()]);
				}
			} finally {
				query.closeAll();
			}
		} finally {
			pm.close();
		}
		return infos!=null?infos:emptyContacts;*/
		QueryHandlers handlers = SimpleJCQRS.getInjector().getInstance(QueryHandlers.class);		
		List<ContactInfo> results = handlers.contacts;
		infos = results.toArray(new ContactInfo[results.size()]);
		return infos!=null?infos:emptyContacts;
	}

}
