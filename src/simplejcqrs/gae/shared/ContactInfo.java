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
    private Date birthday;
    @Persistent
    private String firstName;

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
     * @return the contact's birthday
     */
    public Date getBirthday() {
      return birthday;
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
    /**
     * Set the contact's address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
      this.address = address;
    }
    
    /**
     * Set the contact's birthday.
     *
     * @param birthday the birthday
     */
    public void setBirthday(Date birthday) {
      this.birthday = birthday;
    }
    
   
    /**
     * Set the contact's first name.
     *
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }
    
    /**
     * Set the contact's last name.
     *
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

}
