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

import java.util.Date;

import simplejcqrs.gae.shared.ContactInfo;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * Example file.
 */
public class CwCellList extends Composite {

  /**
   * The UiBinder interface used by this example.
   */
  interface Binder extends UiBinder<Widget, CwCellList> {
  }

  
  /**
   * The images used for this example.
   */
  static interface Images extends ClientBundle {
    ImageResource contact();
  }

  /**
   * The Cell used to render a {@link ContactInfo}.
   */
  static class ContactCell extends AbstractCell<ContactInfo> {

    /**
     * The html of the image used for contacts.
     */
    private final String imageHtml;

    public ContactCell(ImageResource image) {
      this.imageHtml = AbstractImagePrototype.create(image).getHTML();
    }

    @Override
    public void render(Context context, ContactInfo value, SafeHtmlBuilder sb) {
      // Value can be null, so do a null check..
      if (value == null) {
        return;
      }

      sb.appendHtmlConstant("<table>");

      // Add the contact image.
      sb.appendHtmlConstant("<tr><td rowspan='3'>");
      sb.appendHtmlConstant(imageHtml);
      sb.appendHtmlConstant("</td>");

      // Add the name and address.
      sb.appendHtmlConstant("<td style='font-size:95%;'>");
      sb.appendEscaped(value.getFullName());
      sb.appendHtmlConstant("</td></tr><tr><td>");
      String addr = value.getAddress();
      sb.appendEscaped(addr==null?"<unknown>":addr);
      sb.appendHtmlConstant("</td></tr></table>");
    }
  }

  /**
   * The contact form used to update contacts.
   
  @UiField
  ContactInfoForm contactForm;
  */
  
  @UiField
  Button reloadButton;
  
  @UiField
  Button createButton;

  /**
   * The pager used to change the range of data.
   */
  @UiField
  ShowMorePagerPanel pagerPanel;

  /**
   * The pager used to display the current range.
   */
  @UiField
  RangeLabelPager rangeLabelPager;

  /**
   * The CellList.
   */
  private CellList<ContactInfo> cellList;

  /**
   * Constructor.
   *
   * @param constants the constants
   */
  public CwCellList() {
	  initWidget(onInitialize());
  }

  /**
   * Initialize this example.
   */
  public Widget onInitialize() {
    Images images = GWT.create(Images.class);

    // Create a CellList.
    ContactCell contactCell = new ContactCell(images.contact());

    // Set a key provider that provides a unique key for each contact. If key is
    // used to identify contacts when fields (such as the name and address)
    // change.
    cellList = new CellList<ContactInfo>(contactCell,
        ContactInfo.KEY_PROVIDER);
    cellList.setPageSize(30);
    cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

    // Add a selection model so we can select cells.
    final SingleSelectionModel<ContactInfo> selectionModel = new SingleSelectionModel<ContactInfo>(
        ContactInfo.KEY_PROVIDER);
    cellList.setSelectionModel(selectionModel);
    
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      public void onSelectionChange(SelectionChangeEvent event) {
        //contactForm.setContact(selectionModel.getSelectedObject());
      }
    });

    // Create the UiBinder.
    Binder uiBinder = GWT.create(Binder.class);
    Widget widget = uiBinder.createAndBindUi(this);

    // Add the CellList to the data provider in the database.
    ContactDatabase.get().addDataDisplay(cellList);

    pagerPanel.setDisplay(cellList);
    rangeLabelPager.setDisplay(cellList);
    
    reloadButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
    	  ContactDatabase.get().loadContacts();
      }
    });   
    
    final CreateDialog createDialog = new CreateDialog();
    createButton.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          createDialog.showNew();
    
        }
    }); 
    return widget;
  }
  
  static class CreateDialog extends DialogBox {
	
	private final TextBox tbName = new TextBox();
	private final Label errorLabel = new Label();
	
	public void showNew() {
		tbName.setText("");
		errorLabel.setText("");
		center();
		tbName.setFocus(true);
	}
	
	public CreateDialog() {
		super();
		setText("Create new Contact");
		setAnimationEnabled(true);
		  
		VerticalPanel dialogVPanel = new VerticalPanel();
		  
		 
		  dialogVPanel.add(tbName);
		  
		  
		  errorLabel.setStyleName("error");
		  dialogVPanel.add(errorLabel);	  	  
		  
		  HorizontalPanel buttons = new HorizontalPanel();	  	  
		  final Button addButton = new Button("Add");
		  buttons.add(addButton);
		  final Button closeButton = new Button("Cancel");
		  buttons.add(closeButton);
		  dialogVPanel.add(buttons);
		  	 	  
	      closeButton.addClickHandler(new ClickHandler() {
	              public void onClick(ClickEvent event) {            	  
	                      hide();         
	              }
	      });
	      
	      addButton.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	              // errorLabel.setText("Too short!");
	        	  
	        	  
	        	  ContactInfo info = new ContactInfo( Integer.toString( Random.nextInt(10000) ));	        	    
	        		CommandSender.get().CreateContact(tbName.getText(), "second", new AsyncCallback<Void>() {						
						@Override
						public void onSuccess(Void result) {
			        		hide();							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							errorLabel.setText(caught.getMessage());							
						}
					});

	          }
	      });
		  setWidget(dialogVPanel);

	}
	  
  }
  
  
}
