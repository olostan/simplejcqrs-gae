package simplejcqrs.gae.server;

import java.util.Set;

import simplejcqrs.commandhandlers.CommandHandler;
import simplejcqrs.commandhandlers.HumanCommandHandlers;
import simplejcqrs.events.EventStore;
import simplejcqrs.structural.EventBus;
import sun.security.jca.GetInstance;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

public final class SimpleJCQRS 
{
	private static void  ensureCreated() {
		if (injector==null) injector = Guice.createInjector(new GAEModule());
		if (bus==null) {			
			bus = createBus();
			GAEEventStore store = injector.getInstance(GAEEventStore.class);
			store.publishAllEvents(bus);
		}
	}
	private static EventBus bus;
	public static EventBus getBus() {
		ensureCreated();
		return bus;
	}
	
	private static final class GAEModule extends AbstractModule {
		@Override
		public void configure() {
			Multibinder<Object> m = Multibinder.newSetBinder(binder(), Object.class, CommandHandler.class);
			m.addBinding().to(HumanCommandHandlers.class);
			bind(EventStore.class)
			 .annotatedWith(Names.named("ActualStore"))
			//.to(InMemoryEventStore.class);
			 .to(GAEEventStore.class);
		}		
	}	
	private static Injector injector = null;
	public static Injector getInjector() {
		ensureCreated();
		return injector;
	}
	
	private static EventBus createBus() {
		Injector inj = injector;
		EventBus bus  = inj.getInstance(EventBus.class);		  
		TypeLiteral<Set<Object>> t = new TypeLiteral<Set<Object>>() {};
		
		Set<Object> handlers = injector.getInstance(Key.get(t,CommandHandler.class));
		for (Object handler : handlers) {
			bus.registerHandler(handler);
		}		
		bus.registerHandler(inj.getInstance(QueryHandlers.class));		
		return bus;
	}
	
}
