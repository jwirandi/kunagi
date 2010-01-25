package scrum.client.calendar;

import scrum.client.common.TooltipBuilder;

public class DeleteSimpleEventAction extends GDeleteSimpleEventAction {

	public DeleteSimpleEventAction(scrum.client.calendar.SimpleEvent simpleEvent) {
		super(simpleEvent);
	}

	@Override
	public String getLabel() {
		return "Delete";
	}

	@Override
	public String getTooltip() {
		TooltipBuilder tb = new TooltipBuilder("Delete this Impediment.");
		if (!getCurrentProject().isProductOwnerOrScrumMasterOrTeamMember(getCurrentUser()))
			tb.addRemark(TooltipBuilder.NOT_ANYTHING);
		return tb.getTooltip();
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	@Override
	public boolean isPermitted() {
		if (!getCurrentProject().isProductOwnerOrScrumMasterOrTeamMember(getCurrentUser())) return false;
		return true;
	}

	@Override
	protected void onExecute() {
		cm.getDao().deleteSimpleEvent(simpleEvent);
		addUndo(new Undo());
	}

	class Undo extends ALocalUndo {

		@Override
		public String getLabel() {
			return "Undo Delete " + simpleEvent.getLabel();
		}

		@Override
		protected void onUndo() {
			cm.getDao().createSimpleEvent(simpleEvent);
		}

	}

}