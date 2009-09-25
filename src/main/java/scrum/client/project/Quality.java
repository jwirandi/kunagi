package scrum.client.project;

import java.util.Map;

import scrum.client.issues.Issue;

public class Quality extends GQuality {

	public static final String INITIAL_LABEL = "New Quality";

	public Quality(Project project) {
		setProject(project);
		setLabel(INITIAL_LABEL);
	}

	public Quality(Issue issue) {
		setProject(issue.getProject());
		setLabel(issue.getLabel());
		setDescription(issue.getDescription());
	}

	public Quality(Map data) {
		super(data);
	}

	@Override
	public String toString() {
		return getLabel();
	}
}
