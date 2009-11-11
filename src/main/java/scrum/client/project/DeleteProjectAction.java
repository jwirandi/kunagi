package scrum.client.project;

import ilarkesto.gwt.client.Gwt;
import scrum.client.common.TooltipBuilder;

public class DeleteProjectAction extends GDeleteProjectAction {

	public DeleteProjectAction(Project project) {
		super(project);
	}

	@Override
	public String getLabel() {
		return "Delete Project";
	}

	@Override
	public String getTooltip() {
		TooltipBuilder tb = new TooltipBuilder("Delete this project and destroy all its data.");

		if (!getCurrentUser().isAdmin()) tb.addRemark(TooltipBuilder.NOT_AN_ADMIN);

		return tb.getTooltip();
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	public boolean isPermitted() {
		if (!getCurrentUser().isAdmin()) return false;
		return true;
	}

	@Override
	protected void onExecute() {
		if (Gwt.confirm("Delete project " + project.getLabel() + "?")) {
			cm.getDao().deleteProject(project);
		}
		addUndo(new Undo());
	}

	class Undo extends ALocalUndo {

		@Override
		public String getLabel() {
			return "Undo Delete " + project.getLabel();
		}

		@Override
		protected void onUndo() {
			cm.getDao().createProject(project);
		}

	}

}