package scrum.client.project;

import ilarkesto.gwt.client.ARichtextViewEditWidget;
import ilarkesto.gwt.client.ATextViewEditWidget;
import ilarkesto.gwt.client.ToolbarWidget;
import scrum.client.ScrumGwtApplication;
import scrum.client.common.ABlockWidget;
import scrum.client.common.ItemFieldsWidget;
import scrum.client.common.editable.AEditableListBoxWidget;
import scrum.client.dnd.BlockListDropController;
import scrum.client.img.Img;
import scrum.client.sprint.Sprint;

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class RequirementWidget extends ABlockWidget {

	private Requirement requirement;

	private ATextViewEditWidget label;
	private ARichtextViewEditWidget description;
	private ARichtextViewEditWidget test;
	private Label summary;
	private ItemFieldsWidget fieldsWidget;
	private ToolbarWidget toolbar;

	public RequirementWidget(Requirement item) {
		this.requirement = item;
	}

	@Override
	protected void onBlockInitialization() {
		summary = new Label();

		fieldsWidget = new ItemFieldsWidget();
		label = fieldsWidget.addField("Label", new ATextViewEditWidget() {

			@Override
			protected void onViewerUpdate() {
				setViewerText(requirement.getLabel());
			}

			@Override
			protected void onEditorUpdate() {
				setEditorText(requirement.getLabel());
			}

			@Override
			protected void onEditorSubmit() {
				requirement.setLabel(getEditorText());
				update();
			}

		});

		description = fieldsWidget.addField("Description", new ARichtextViewEditWidget() {

			@Override
			protected void onViewerUpdate() {
				setViewerText(requirement.getDescription());
			}

			@Override
			protected void onEditorUpdate() {
				setEditorText(requirement.getDescription());
			}

			@Override
			protected void onEditorSubmit() {
				requirement.setDescription(getEditorText());
			}

		});

		test = fieldsWidget.addField("Test", new ARichtextViewEditWidget() {

			@Override
			protected void onViewerUpdate() {
				setViewerText(requirement.getTestDescription());
			}

			@Override
			protected void onEditorUpdate() {
				setEditorText(requirement.getTestDescription());
			}

			@Override
			protected void onEditorSubmit() {
				requirement.setTestDescription(getEditorText());
			}

		});

		fieldsWidget.addField("Estimated Work", new AEditableListBoxWidget() {

			@Override
			protected String getText() {
				Integer effort = requirement.getEstimatedWork();
				return effort == null ? "No estimation." : effort.toString() + " "
						+ requirement.getProject().getEffortUnit();
			}

			@Override
			protected String[] getSelectableValues() {
				return new String[] { "", "1", "2", "3", "5", "8", "13", "21" };
			}

			@Override
			protected String getSelectedValue() {
				Integer effort = requirement.getEstimatedWork();
				return effort == null ? "" : effort.toString();
			}

			@Override
			protected void setValue(String value) {
				requirement.setEstimatedWork(value.length() == 0 ? null : Integer.parseInt(value));
				rebuild();
			}

		});
	}

	@Override
	protected void onBlockUpdate() {
		setBlockTitle(requirement.getLabel());
		setIcon(requirement.isClosed() ? Img.bundle.storyDoneIcon32() : Img.bundle.storyIcon32());
		if (!isSelected()) {
			summary.setText(requirement.getProductBacklogSummary());
			setContent(summary);
			setToolbar(null);
			return;
		}

		label.update();
		description.update();
		test.update();

		setContent(fieldsWidget);
		setToolbar(getToolbar());
	}

	protected Widget getToolbar() {
		if (toolbar == null) {
			toolbar = new ToolbarWidget();

			toolbar.addButton(Img.bundle.delete16().createImage(), "Delete").addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					ScrumGwtApplication.get().getProject().deleteRequirement(requirement);
					ProductBacklogWidget.get().list.removeSelectedRow();
				}
			});

			final Sprint currentSprint = ScrumGwtApplication.get().getProject().getCurrentSprint();
			if (currentSprint != null) {
				if (requirement.isSprint(currentSprint)) {
					toolbar.addButton("Remove from Sprint").addClickListener(new ClickListener() {

						public void onClick(Widget sender) {
							requirement.setSprint(null);
							update();
						}
					});
				} else {
					toolbar.addButton(Img.bundle.sprintIcon16().createImage(), "Add to Sprint").addClickListener(
						new ClickListener() {

							public void onClick(Widget sender) {
								requirement.setSprint(currentSprint);
								update();
							}
						});
				}
			}

			if (!requirement.isClosed() && requirement.isDone()) {
				toolbar.addButton(Img.bundle.done16().createImage(), "Close").addClickListener(new ClickListener() {

					public void onClick(Widget sender) {
						// item.setDone(false);
						update();
					}
				});
			}
		}
		return toolbar;
	}

	@Override
	protected AbstractImagePrototype getIcon16() {
		if (requirement.isClosed()) return Img.bundle.storyDoneIcon16();
		return Img.bundle.storyIcon16();
	}

	@Override
	public void delete() {
		ScrumGwtApplication.get().getProject().deleteRequirement(requirement);
		ProductBacklogWidget.get().list.remove(this);
	}

	@Override
	protected DropController createDropController() {
		return new BlockListDropController(this, ProductBacklogWidget.get().list);
	}

	public Requirement getRequirement() {
		return requirement;
	}
}
