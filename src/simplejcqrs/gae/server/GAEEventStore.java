package simplejcqrs.gae.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import simplejcqrs.domain.AggregateRoot;
import simplejcqrs.events.Event;
import simplejcqrs.events.EventStore;
import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

public class GAEEventStore implements EventStore 
{
	private static final class EventIterableAdapter implements Iterable<Event>, Iterator<Event> {
		private Iterator<Entity> itr;
		
		public EventIterableAdapter(Iterator<Entity> itr) {
			super();
			this.itr = itr;
		}

		@Override
		public Iterator<Event> iterator() {
			return this;
		}

		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public Event next() {
			Entity e = itr.next();
			ByteArrayInputStream bis = new ByteArrayInputStream(((Blob)e.getProperty("data")).getBytes());
			ObjectInput inp;
			try {
				inp = new ObjectInputStream(bis);
				Event ev = (Event) inp.readObject();
				inp.close();
				return ev;
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			return null;

		}

		@Override
		public void remove() {
			itr.remove();
		}

	}

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	private Entity serializeEvent(Event ev, Entity parent) {
		Entity e = new Entity("e"+ev.getAggregateVersion(),parent.getKey());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out;
		try {
			out = new ObjectOutputStream(bos);		
			out.writeObject(ev);
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		e.setUnindexedProperty("data",new Blob(bos.toByteArray()));
		return e;
	}
	
	@Override
	public void saveEvents(Class<? extends AggregateRoot> rootClass,
			long aggregateId, Iterable<Event> events, int expectedVersion) {
		Transaction t = datastore.beginTransaction();
		Entity parent = null;
		try {
			parent = datastore.get(KeyFactory.createKey(rootClass.getSimpleName(), aggregateId));
		} catch (EntityNotFoundException exp) {
			parent = new Entity(rootClass.getSimpleName(), aggregateId);
			datastore.put(parent);
		}
		for(Event e : events) {
			datastore.put(serializeEvent(e, parent));
		}
		t.commitAsync();		
	}
	
	

	@Override
	public Iterable<Event> getEventsForAggregate(
			Class<? extends AggregateRoot> rootClass, long id) 
	{
		Entity parent = getParent(rootClass, id);
		if (parent==null) throw new RuntimeException("No aggregate found with id "+id);
		Query q = new Query(parent.getKey());
		PreparedQuery pq = datastore.prepare(q);		
		return new EventIterableAdapter(pq.asIterable().iterator());
	}

	private Entity getParent(Class<? extends AggregateRoot> rootClass, long id) {
		Entity parent = null;
		try{
			parent = datastore.get(KeyFactory.createKey(rootClass.getSimpleName(), id));
		} catch (EntityNotFoundException exp) {
			//return null;
		}
		return parent;
	}

	@Override
	public boolean hasEventsForAggregate(
			Class<? extends AggregateRoot> rootClass, long id) {
		Entity parent = getParent(rootClass, id);
		if (parent==null) return false;
		Query q = new Query(parent.getKey());
		PreparedQuery pq = datastore.prepare(q);		
		return pq.countEntities(null)>0;
	}

	@Override
	public boolean checkVersion(Class<? extends AggregateRoot> rootClass,
			long id, int version) {
		Entity parent = getParent(rootClass, id);
		if (parent==null) return false;
		Query q = new Query(parent.getKey());
		q.addSort("av",Query.SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		Iterator<Entity> itr = pq.asQueryResultIterator(withLimit(1));		
		return itr.hasNext();
	}

}