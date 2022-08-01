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

    private String createdTimeInMillis = null;

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

    /**
     * Set Unique ID to represent a approval task.
     **/
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Subject of the Approval.
     **/
    public String getSubject() {

        return subject;
    }

    /**
     * Set Subject of the Approval.
     **/
    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Description on the Approval task.
     **/

    public String getDescription() {

        return description;
    }

    /**
     * Set Description on the Approval task.
     **/
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Priority of the Approval task.
     **/
    public String getPriority() {

        return priority;
    }

    /**
     * Set Priority of the Approval task.
     **/
    public void setPriority(String priority) {

        this.priority = priority;
    }

    /**
     * The user who initiated the task.
     **/

    public String getInitiator() {

        return initiator;
    }

    /**
     * Set the user who initiated the task.
     **/
    public void setInitiator(String initiator) {

        this.initiator = initiator;
    }

    /**
     * Available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise\n
     **/
    public ApprovalStatusEnum getApprovalStatus() {

        return approvalStatus;
    }

    /**
     * Set available only for the completed Tasks, APPROVED or REJECTED if the task has been completed, PENDING otherwise\n
     **/
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

    /**
     * Set Task parameters: username, role, claims, requestId.
     **/
    public void setProperties(List<PropertyDTO> properties) {

        this.properties = properties;
    }

    /**
     * The created time of the request.
     **/
    public String getCreatedTimeInMillis() {

        return createdTimeInMillis;
    }

    /**
     * Set the created time of the request.
     **/
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
