package org.wso2.carbon.identity.workflow.engine.model;

import java.util.Map;

/**
 * Model Class for task.
 */
public class TaskModel {

    private String id;
    private String taskSubject;
    private String taskDescription;
    private String priority;
    private String htInitiator;
    private String approvalStatus;
    private Map<String, String> assignees;

    /**
     * Unique ID to represent a approval task
     **/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Subject of the Approval
     **/
    public String getTaskSubject() {
        return taskSubject;
    }

    public void setTaskSubject(String taskSubject) {
        this.taskSubject = taskSubject;
    }

    /**
     * Description on the Approval task
     **/
    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    /**
     * Priority of the Approval task
     **/
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * The user who initiated the task
     **/
    public String getHtInitiator() {
        return htInitiator;
    }

    public void setHtInitiator(String htInitiator) {
        this.htInitiator = htInitiator;
    }

    /**
     * Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise\n
     **/
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    /**
     * To whom the task is assigned:\n  * user - username(s) if the task is reserved for specific user(s).\n  * group - role name(s) if the task is assignable for group(s).\n
     **/
    public Map<String, String> getAssignees() {
        return assignees;
    }

    public void setAssignees(Map<String, String> assignees) {
        this.assignees = assignees;
    }
}
