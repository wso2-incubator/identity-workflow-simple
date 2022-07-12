package org.wso2.carbon.identity.workflow.engine.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO class for task details.
 */
public class TaskDataDTO {

    private String id = null;

    private String subject = null;

    private String description = null;

    private String priority = null;

    private String initiator = null;

    public enum ApprovalStatusEnum {
        PENDING, APPROVED, REJECTED,
    }

    private ApprovalStatusEnum approvalStatus = null;

    private List<PropertyDTO> assignees = new ArrayList<PropertyDTO>();

    private List<PropertyDTO> properties = new ArrayList<PropertyDTO>();

    /**
     * Unique ID to represent a approval task.
     **/
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    /**
     * Subject of the Approval.
     **/
    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Description on the Approval task.
     **/

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Priority of the Approval task.
     **/
    public String getPriority() {

        return priority;
    }

    public void setPriority(String priority) {

        this.priority = priority;
    }

    /**
     * The user who initiated the task.
     **/

    public String getInitiator() {

        return initiator;
    }

    public void setInitiator(String initiator) {

        this.initiator = initiator;
    }

    /**
     * Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise\n
     **/
    public ApprovalStatusEnum getApprovalStatus() {

        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatusEnum approvalStatus) {

        this.approvalStatus = approvalStatus;
    }

    /**
     * To whom the task is assigned:\n  * user - username(s) if the task is reserved for specific user(s).\n  * group - role name(s) if the task is assignable for group(s).\n
     **/
    public List<PropertyDTO> getAssignees() {
        return assignees;
    }
    public void setAssignees(List<PropertyDTO> assignees) {
        this.assignees = assignees;
    }


    /**
     * Task parameters: username, role, claims, requestId.
     **/
    public List<PropertyDTO> getProperties() {
        return properties;
    }
    public void setProperties(List<PropertyDTO> properties) {
        this.properties = properties;
    }
    private String createdTimeInMillis = null;
    public String getCreatedTimeInMillis() {
        return createdTimeInMillis;
    }
    public void setCreatedTimeInMillis(String createdTimeInMillis) {
        this.createdTimeInMillis = createdTimeInMillis;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class TaskDataDTO {\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  subject: ").append(subject).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("  priority: ").append(priority).append("\n");
        sb.append("  initiator: ").append(initiator).append("\n");
        sb.append("  approvalStatus: ").append(approvalStatus).append("\n");
        sb.append("  assignees: ").append(assignees).append("\n");
        sb.append("  properties: ").append(properties).append("\n");
        sb.append("  createdTimeInMillis: ").append(createdTimeInMillis).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
