package home.app.direct.vaadin.components.view.window;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;

public abstract class SearchWindow extends Window {

	private Button closeVendorDialog;
	private Button openVendor;
	private TextField vendorIdTextField;

	private VerticalLayout layout;
	protected Panel mainPanel;

	public void start(Panel mainPanel) {
		center();
		this.mainPanel = mainPanel;
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setSizeUndefined();

		vendorIdField();
		layout.addComponent(vendorIdTextField);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSizeFull();
		//buttonLayout.setSpacing(true);
		layout.addComponent(buttonLayout);

		openVendor = openButton();
		openVendor.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		buttonLayout.addComponent(openVendor);
		buttonLayout.setComponentAlignment(openVendor, Alignment.BOTTOM_CENTER);

		closeVendorDialog = closeButton();
		buttonLayout.addComponent(closeVendorDialog);
		buttonLayout.setComponentAlignment(closeVendorDialog, Alignment.BOTTOM_CENTER);
		setContent(layout);
	}

	private void vendorIdField() {
		vendorIdTextField = new TextField();
		vendorIdTextField.setCaption("Vendor ID: ");
		vendorIdTextField.setInputPrompt("Vendor ID");
		vendorIdTextField.focus();
		vendorIdTextField.setRequired(true);
	}

	private Button openButton() {
		return new Button("Open", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				if(event.getSource()==openVendor){
					String searchedText = vendorIdTextField.getValue().trim();
					try {
						onEnter(searchedText);
					}finally {
						closeAndRemove();
					}
				}
			}
		});
	}

	private Button closeButton() {
		return new Button("Close", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				if(event.getSource()==closeVendorDialog){
					closeAndRemove();
				}
			}
		});
	}

	private void closeAndRemove() {
		close();
	}

	protected abstract void onEnter(String vendorId);
}