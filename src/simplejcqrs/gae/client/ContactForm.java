package simplejcqrs.gae.client;

import simplejcqrs.commands.HumanCommands;
import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContactForm extends Composite  {
	private long humanId;
	private int version;
	private static ContactFormUiBinder uiBinder = GWT
			.create(ContactFormUiBinder.class);
	@UiField Button btnRename;
	@UiField TextBox tbName;
	@UiField Label lblData;

	interface ContactFormUiBinder extends UiBinder<Widget, ContactForm> {
	}

	public ContactForm() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setContact(ContactInfo selectedObject) {
		tbName.setText(selectedObject.getFirstName());
		humanId = selectedObject.getId();
		version = selectedObject.getOriginalVersion();
		lblData.setText("(id="+humanId+" vers="+version);
	}
	
	@UiHandler("btnRename")
	void onBtnRenameClick(ClickEvent event) {
		HumanCommands.RenameHuman cmd = new HumanCommands.RenameHuman(humanId, tbName.getText(),"last",version);
		setEnabled(false);
		CommandSender.get().Execute(cmd, new AsyncCallback<Void>() {						
			@Override
			public void onSuccess(Void result) {
        		setEnabled(true);							
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if (caught.getClass() == InvocationException.class) {
					Window.alert("Somebody else changed");
				} else {
					caught.printStackTrace();
					Window.alert(caught.getMessage());
				//errorLabel.setText(caught.getMessage());
				}
				setEnabled(true);
			}
		});
		
	}

	protected void setEnabled(boolean enabled) {
		tbName.setEnabled(enabled);
		btnRename.setEnabled(enabled);		
	}
}
