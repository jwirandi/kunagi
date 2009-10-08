package scrum.client;

import ilarkesto.gwt.client.AComponents;

public class Components extends GComponents implements LogoutListener, ProjectClosedListener {

	Components() {
		super(new EventBus(), new Dao());
	}

	public void onLogout() {
		destroyAuth();
	}

	public void onProjectClosed() {
		destroyProjectContext();
		destroyChat();
		destroyUsersStatus();
		destroyDndManager();
	}

	@Override
	protected void initializeDao(Dao dao) {
		super.initializeDao(dao);
		dao.setUi(getUi());
	}

	@Override
	protected void initializeUsersStatus(UsersStatus usersStatus) {
		super.initializeUsersStatus(usersStatus);
		usersStatus.setUi(getUi());
	}

	@Override
	protected void initializeProjectContext(ProjectContext projectContext) {
		super.initializeProjectContext(projectContext);
		projectContext.setUi(getUi());
		projectContext.setEventBus(getEventBus());
	}

	@Override
	protected void initializeChat(Chat chat) {
		super.initializeChat(chat);
		chat.setAuth(getAuth());
		chat.setDao(getDao());
	}

	@Override
	protected void initializeAuth(Auth auth) {
		super.initializeAuth(auth);
		auth.setDao(getDao());
		auth.setEventBus(getEventBus());
		auth.setProjectContext(getProjectContext());
		auth.setUi(getUi());
	}

	public ScrumGwtApplication getApp() {
		return ScrumGwtApplication.get();
	}

	public static Components get() {
		return (Components) AComponents.get();
	}

}