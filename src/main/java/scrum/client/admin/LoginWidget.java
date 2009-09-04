package scrum.client.admin;

import ilarkesto.gwt.client.AWidget;
import ilarkesto.gwt.client.GwtLogger;
import ilarkesto.gwt.client.ToolbarWidget;
import scrum.client.ScrumGwtApplication;
import scrum.client.common.FieldsWidget;
import scrum.client.common.GroupWidget;
import scrum.client.project.Project;
import scrum.client.test.WidgetsTesterWidget;
import scrum.client.workspace.Ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginWidget extends AWidget {

	private TextBox username;
	private PasswordTextBox password;

	@Override
	protected Widget onInitialization() {
		username = new TextBox();
		password = new PasswordTextBox();
		password.addKeyboardListener(new KeyboardListenerAdapter() {

			@Override
			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				super.onKeyPress(sender, keyCode, modifiers);
				if (keyCode == KeyboardListener.KEY_ENTER) login();
			}

		});

		if (ScrumGwtApplication.get().isDevelopmentMode()) {
			username.setText("duke");
			password.setText("geheim");
		}

		ToolbarWidget toolbar = new ToolbarWidget(true);
		toolbar.addButton("Login").addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				login();
			}
		});

		FieldsWidget fieldsWidget = new FieldsWidget();
		fieldsWidget.addWidget("Username", username);
		fieldsWidget.addWidget("Password", password);
		fieldsWidget.addWidget(null, toolbar.update());

		SimplePanel wrapper = new SimplePanel();
		wrapper.setStyleName("LoginWidget");
		wrapper.setWidget(new GroupWidget("Login", fieldsWidget));

		if (GWT.isScript()) { return wrapper; }

		FlowPanel test = new FlowPanel();
		test.add(wrapper);
		test.add(new WidgetsTesterWidget().update());
		return test;

	}

	@Override
	protected void onUpdate() {
		username.setFocus(true);
	}

	private void login() {
		ScrumGwtApplication.get().getUi().lock("Checking login data...");
		ScrumGwtApplication.get().callLogin(username.getText(), password.getText(), new Runnable() {

			public void run() {
				GwtLogger.DEBUG("Login response received");
				User user = ScrumGwtApplication.get().getUser();
				if (user == null) {
					GwtLogger.DEBUG("LOGIN FAILED!");
					ScrumGwtApplication.get().getUi().unlock();
					Ui.get().showError("Login failed.");
				} else {
					GwtLogger.DEBUG("Login succeded:", ScrumGwtApplication.get().getUi());
					Project project = user.getCurrentProject();
					if (project == null || user.isAdmin()) {
						Ui.get().showStartPage();
					} else {
						ScrumGwtApplication.get().openProject(project);
					}
				}
			}
		});
	}

}
