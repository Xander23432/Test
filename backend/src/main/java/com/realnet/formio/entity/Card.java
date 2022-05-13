package com.realnet.formio.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String title;
	private String date;
	private String image;
	private String presentColumn;
	
	// extra variables of card for making changes
	
	// variables for description
	private String description;
	private String tags;
	
	// variables for Time and Assignment
	private String storyPoints;
	private String valuePoints;
	private String issueType;
	private String assignedTo;
	private String scheduledDate;
	private String attachmentTaA;
	
	// variables for Definition of Ready
	private String story_details;
	private String scenario_given_when;
	private String data_expected_result;
	private String functional_area_passfail_details;
	private String scalibility;
	private String test_case_demo_case;
	private String estimates;
	private String dor_approval_status;
	
	// variables for Definition of Done
	private boolean gitCommited = false;
	private boolean code_quality_review_done = false;
	private boolean peer_revired = false;
	private boolean builds_without_error = false;
	private String unit_test_details;
	private boolean promoted_test_instance = false;
	private boolean configuration_changes = false;
	private String unit_test_details2;
	private boolean closed_for_clocking = false;
	private String attachmentDoD;
	
	// static value variables
	private String priority_index;
	private String related_issue;
	// Time Stats variables
	private String estimated_time;
	private String elapsed_time;
	private String start_date_time;
	private String completion_date_time;
	// Other Details
	private String project;
	private String repository;
	private String sprint_test;
	private String current_status;
	private String clock_status;
	private String project_priority;
	private String pinned_status;
	
//	@OneToOne(cascade = CascadeType.ALL)
//	private CardImage cardimage;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Column column;
	
	
	
	public Card() {}
	
	
	
	public Card(Long id, String title, String date, String image, String presentColumn, String description, String tags,
			String storyPoints, String valuePoints, String issueType, String assignedTo, String scheduledDate,
			String attachmentTaA, String story_details, String scenario_given_when, String data_expected_result,
			String functional_area_passfail_details, String scalibility, String test_case_demo_case, String estimates,
			String dor_approval_status, boolean gitCommited, boolean code_quality_review_done, boolean peer_revired,
			boolean builds_without_error, String unit_test_details, boolean promoted_test_instance,
			boolean configuration_changes, String unit_test_details2, boolean closed_for_clocking, String attachmentDoD,
			String priority_index, String related_issue, String estimated_time, String elapsed_time,
			String start_date_time, String completion_date_time, String project, String repository, String sprint_test,
			String current_status, String clock_status, String project_priority, String pinned_status, Column column) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.image = image;
		this.presentColumn = presentColumn;
		this.description = description;
		this.tags = tags;
		this.storyPoints = storyPoints;
		this.valuePoints = valuePoints;
		this.issueType = issueType;
		this.assignedTo = assignedTo;
		this.scheduledDate = scheduledDate;
		this.attachmentTaA = attachmentTaA;
		this.story_details = story_details;
		this.scenario_given_when = scenario_given_when;
		this.data_expected_result = data_expected_result;
		this.functional_area_passfail_details = functional_area_passfail_details;
		this.scalibility = scalibility;
		this.test_case_demo_case = test_case_demo_case;
		this.estimates = estimates;
		this.dor_approval_status = dor_approval_status;
		this.gitCommited = gitCommited;
		this.code_quality_review_done = code_quality_review_done;
		this.peer_revired = peer_revired;
		this.builds_without_error = builds_without_error;
		this.unit_test_details = unit_test_details;
		this.promoted_test_instance = promoted_test_instance;
		this.configuration_changes = configuration_changes;
		this.unit_test_details2 = unit_test_details2;
		this.closed_for_clocking = closed_for_clocking;
		this.attachmentDoD = attachmentDoD;
		this.priority_index = priority_index;
		this.related_issue = related_issue;
		this.estimated_time = estimated_time;
		this.elapsed_time = elapsed_time;
		this.start_date_time = start_date_time;
		this.completion_date_time = completion_date_time;
		this.project = project;
		this.repository = repository;
		this.sprint_test = sprint_test;
		this.current_status = current_status;
		this.clock_status = clock_status;
		this.project_priority = project_priority;
		this.pinned_status = pinned_status;
		this.column = column;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}



	public String getPresentColumn() {
		return presentColumn;
	}



	public void setPresentColumn(String presentColumn) {
		this.presentColumn = presentColumn;
	}





	public Column getColumn() {
		return column;
	}





	public void setColumn(Column column) {
		this.column = column;
	}





	public String getDescription() {
		return description;
	}





	public void setDescription(String description) {
		this.description = description;
	}





	public String getTags() {
		return tags;
	}





	public void setTags(String tags) {
		this.tags = tags;
	}





	public String getStoryPoints() {
		return storyPoints;
	}





	public void setStoryPoints(String storyPoints) {
		this.storyPoints = storyPoints;
	}





	public String getValuePoints() {
		return valuePoints;
	}





	public void setValuePoints(String valuePoints) {
		this.valuePoints = valuePoints;
	}





	public String getIssueType() {
		return issueType;
	}





	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}





	public String getAssignedTo() {
		return assignedTo;
	}





	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}





	public String getScheduledDate() {
		return scheduledDate;
	}





	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}





	public String getAttachmentTaA() {
		return attachmentTaA;
	}





	public void setAttachmentTaA(String attachmentTaA) {
		this.attachmentTaA = attachmentTaA;
	}





	public String getStory_details() {
		return story_details;
	}





	public void setStory_details(String story_details) {
		this.story_details = story_details;
	}





	public String getScenario_given_when() {
		return scenario_given_when;
	}





	public void setScenario_given_when(String scenario_given_when) {
		this.scenario_given_when = scenario_given_when;
	}





	public String getData_expected_result() {
		return data_expected_result;
	}





	public void setData_expected_result(String data_expected_result) {
		this.data_expected_result = data_expected_result;
	}





	public String getFunctional_area_passfail_details() {
		return functional_area_passfail_details;
	}





	public void setFunctional_area_passfail_details(String functional_area_passfail_details) {
		this.functional_area_passfail_details = functional_area_passfail_details;
	}





	public String getScalibility() {
		return scalibility;
	}





	public void setScalibility(String scalibility) {
		this.scalibility = scalibility;
	}





	public String getTest_case_demo_case() {
		return test_case_demo_case;
	}





	public void setTest_case_demo_case(String test_case_demo_case) {
		this.test_case_demo_case = test_case_demo_case;
	}





	public String getEstimates() {
		return estimates;
	}





	public void setEstimates(String estimates) {
		this.estimates = estimates;
	}





	public String getDor_approval_status() {
		return dor_approval_status;
	}





	public void setDor_approval_status(String dor_approval_status) {
		this.dor_approval_status = dor_approval_status;
	}





	public boolean isGitCommited() {
		return gitCommited;
	}





	public void setGitCommited(boolean gitCommited) {
		this.gitCommited = gitCommited;
	}





	public boolean isCode_quality_review_done() {
		return code_quality_review_done;
	}





	public void setCode_quality_review_done(boolean code_quality_review_done) {
		this.code_quality_review_done = code_quality_review_done;
	}





	public boolean isPeer_revired() {
		return peer_revired;
	}





	public void setPeer_revired(boolean peer_revired) {
		this.peer_revired = peer_revired;
	}





	public boolean isBuilds_without_error() {
		return builds_without_error;
	}





	public void setBuilds_without_error(boolean builds_without_error) {
		this.builds_without_error = builds_without_error;
	}





	public String getUnit_test_details() {
		return unit_test_details;
	}





	public void setUnit_test_details(String unit_test_details) {
		this.unit_test_details = unit_test_details;
	}





	public boolean isPromoted_test_instance() {
		return promoted_test_instance;
	}





	public void setPromoted_test_instance(boolean promoted_test_instance) {
		this.promoted_test_instance = promoted_test_instance;
	}





	public boolean isConfiguration_changes() {
		return configuration_changes;
	}





	public void setConfiguration_changes(boolean configuration_changes) {
		this.configuration_changes = configuration_changes;
	}





	public String getUnit_test_details2() {
		return unit_test_details2;
	}





	public void setUnit_test_details2(String unit_test_details2) {
		this.unit_test_details2 = unit_test_details2;
	}





	public boolean isClosed_for_clocking() {
		return closed_for_clocking;
	}





	public void setClosed_for_clocking(boolean closed_for_clocking) {
		this.closed_for_clocking = closed_for_clocking;
	}





	public String getAttachmentDoD() {
		return attachmentDoD;
	}





	public void setAttachmentDoD(String attachmentDoD) {
		this.attachmentDoD = attachmentDoD;
	}



	public String getPriority_index() {
		return priority_index;
	}



	public void setPriority_index(String priority_index) {
		this.priority_index = priority_index;
	}



	public String getRelated_issue() {
		return related_issue;
	}



	public void setRelated_issue(String related_issue) {
		this.related_issue = related_issue;
	}



	public String getEstimated_time() {
		return estimated_time;
	}



	public void setEstimated_time(String estimated_time) {
		this.estimated_time = estimated_time;
	}



	public String getElapsed_time() {
		return elapsed_time;
	}



	public void setElapsed_time(String elapsed_time) {
		this.elapsed_time = elapsed_time;
	}



	public String getStart_date_time() {
		return start_date_time;
	}



	public void setStart_date_time(String start_date_time) {
		this.start_date_time = start_date_time;
	}



	public String getCompletion_date_time() {
		return completion_date_time;
	}



	public void setCompletion_date_time(String completion_date_time) {
		this.completion_date_time = completion_date_time;
	}



	public String getProject() {
		return project;
	}



	public void setProject(String project) {
		this.project = project;
	}



	public String getRepository() {
		return repository;
	}



	public void setRepository(String repository) {
		this.repository = repository;
	}



	public String getSprint_test() {
		return sprint_test;
	}



	public void setSprint_test(String sprint_test) {
		this.sprint_test = sprint_test;
	}



	public String getCurrent_status() {
		return current_status;
	}



	public void setCurrent_status(String current_status) {
		this.current_status = current_status;
	}



	public String getClock_status() {
		return clock_status;
	}



	public void setClock_status(String clock_status) {
		this.clock_status = clock_status;
	}



	public String getProject_priority() {
		return project_priority;
	}



	public void setProject_priority(String project_priority) {
		this.project_priority = project_priority;
	}



	public String getPinned_status() {
		return pinned_status;
	}



	public void setPinned_status(String pinned_status) {
		this.pinned_status = pinned_status;
	}
	
	
	
}
