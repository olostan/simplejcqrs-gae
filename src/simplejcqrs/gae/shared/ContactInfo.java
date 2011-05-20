package simplejcqrs.gae.shared;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

@PersistenceCapable
public class ContactInfo implements Comparable<ContactInfo>, IsSerializable {
    
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<ContactInfo> KEY_PROVIDER = new ProvidesKey<ContactInfo>() {
      public Object getKey(ContactInfo item) {
        return item == null ? null : item.getId();
      }
    };
    
  
    @Persistent
    private String address;
    @Persistent
    private String firstName;
    
    @Persistent
    private int originalVersion=-1;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private long id;
    
    @Persistent
    private String lastName;

    public ContactInfo() {           
    }
    public ContactInfo(long id) {
    	this.id = id;
    }
    
    public int compareTo(ContactInfo o) {
      return (o == null || o.firstName == null) ? -1
          : -o.firstName.compareTo(firstName);
    }
    
    @Override
    public boolean equals(Object o) {
      if (o instanceof ContactInfo) {
        return id == ((ContactInfo) o).id;
      }
      return false;
    }
    
    /**
     * @return the contact's address
     */
    public String getAddress() {
      return address;
    }
           
    /**
     * @return the contact's firstName
     */
    public String getFirstName() {
      return firstName;
    }
    
    /**
     * @return the contact's full name
     */
    public final String getFullName() {
      return firstName + " " + lastName;
    }
    
    /**
     * @return the unique ID of the contact
     */
    public long getId() {
      return this.id;
    }
    
    /**
     * @return the contact's lastName
     */
    public String getLastName() {
      return lastName;
    }
    
    @Override
    public int hashCode() {
      //return id.hashCode();
    	return (int)id;
    }
    public void setAddress(String address) {
      this.address = address;
    }   
    
   
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }
    
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
    
	public int getOriginalVersion() {
		return originalVersion;
	}
	public void setOriginalVersion(int originalVersion) {
		this.originalVersion = originalVersion;
	}

}
