/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package simplejcqrs.gae.client;

import java.util.List;

import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;

public class ContactDatabase {
  

  private static ContactDatabase instance;

  /**
   * Get the singleton instance of the contact database.
   *
   * @return the singleton instance
   */
  public static ContactDatabase get() {
    if (instance == null) {
      instance = new ContactDatabase();
    }
    return instance;
  }

  /**
   * The provider that holds the list of contacts in the database.
   */
  private ListDataProvider<ContactInfo> dataProvider = new ListDataProvider<ContactInfo>();


  /**
   * Construct a new contact database.
   */
  private ContactDatabase() {
    // Initialize the categories.    

    // Generate initial data.
   
  }

  /**
   * Add a new contact.
   *
   * @param contact the contact to add.
   */
  public void addContact(ContactInfo contact) {
    List<ContactInfo> contacts = dataProvider.getList();
    // Remove the contact first so we don't add a duplicate.
    contacts.remove(contact);
    contacts.add(contact);
  }

  /**
   * Add a display to the database. The current range of interest of the display
   * will be populated with data.
   *
   * @param display a {@Link HasData}.
   */
  public void addDataDisplay(HasData<ContactInfo> display) {
    dataProvider.addDataDisplay(display);
  }

  

  public ListDataProvider<ContactInfo> getDataProvider() {
    return dataProvider;
  }  
  
  /**
   * Refresh all displays.
   */
  public void refreshDisplays() {
    dataProvider.refresh();
  }

  private final QueryServiceAsync queryService = GWT.create(QueryService.class);

public void loadContacts() {
	
	queryService.GetContacts(new AsyncCallback<ContactInfo[]>() {		
		@Override
		public void onSuccess(ContactInfo[] result) {
			/*for(ContactInfo info : result)
				addContact(info);*/
			List<ContactInfo> contacts = dataProvider.getList();
			contacts.clear();
			for(ContactInfo info : result)
				contacts.add(info);			
			refreshDisplays();
		}
		
		@Override
		public void onFailure(Throwable caught) {
			//
			
		}
	});
	
}
  

}
