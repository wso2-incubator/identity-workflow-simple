package org.wso2.carbon.identity.workflow.engine.internal.dao;

import java.sql.Timestamp;
import java.util.List;

/**
 * Perform CRUD operations for workflow Event Request properties.
 */
public interface WorkflowEventRequestDAO {

    /**
     * Add who approves the relevant request.
     *
     * @param taskId       random generated unique Id.
     * @param eventId      the request ID that need to be checked.
     * @param workflowId   workflow ID.
     * @param approverType the type of the approved user EX: user or Role.
     * @param approverName the value of the approver type.
     * @param taskStatus   state of the tasks [RESERVED, READY or COMPLETED].
     * @return event ID.
     */
    String addApproversOfRequest(String taskId, String eventId, String workflowId, String approverType, String approverName, String taskStatus);

    /**
     * Get taskId from table.
     *
     * @param eventId the request ID that need to be checked.
     * @return task Id.
     */
    String getApproversOfRequest(String eventId);

    /**
     * Delete approver details using task Id.
     *
     * @param taskId random generated unique Id.
     */
    void deleteApproversOfRequest(String taskId);

    /**
     * Add what step to approve.
     *
     * @param eventId     the request ID that need to be checked.
     * @param workflowId  workflow ID.
     * @param currentStep the current step.
     */
    void createStatesOfRequest(String eventId, String workflowId, int currentStep);

    /**
     * Returns the current step given the event ID and workflow ID.
     *
     * @param eventId    the request ID that need to be checked.
     * @param workflowId workflow ID.
     * @return current step value.
     */
    int getStateOfRequest(String eventId, String workflowId);

    /**
     * Updates a state of request given the event ID, workflow ID and current step.
     *
     * @param eventId     the request ID that need to be checked.
     * @param workflowId  workflow ID.
     * @param currentStep the current step.
     */
    void updateStateOfRequest(String eventId, String workflowId, int currentStep);

    /**
     * Returns the request ID given the task ID.
     *
     * @param taskId random generated unique Id.
     * @return request Id.
     */
    String getRequestID(String taskId);

    /**
     * Returns the initiator given the request ID.
     *
     * @param requestId the request ID that need to be checked.
     * @return string initiator.
     */
    String getInitiatedUser(String requestId);

    /**
     * Returns the events list given the authenticated approver name.
     *
     * @param approverName the approver name that need to be checked.
     * @return requests list.
     */
    List<String> getRequestIdFromApprover(String approverName);

    List<Integer> getRolesID(String userName);

    List<String> getRoleNames(int roleId);

    /**
     * Returns the tasks list given the authenticated approver name.
     *
     * @param approverName the approver name that need to be checked.
     * @return tasks list.
     */
    List<String> getTaskIdList(String approverName);

    /**
     * Returns the events list according to the user.
     *
     * @param approverName admin user.
     * @return events list.
     */
    List<String> getRequestsList(String approverName);

    /**
     * Returns the event type given the request ID.
     *
     * @param requestId the request ID that need to be checked.
     * @return event type of the request.
     */
    String getEventType(String requestId);

    /**
     * Returns the task status given the task ID [RESERVED, READY or COMPLETED].
     *
     * @param taskId the task ID that need to be checked.
     * @return task Status.
     */
    String getTaskStatusOfRequest(String taskId);

    /**
     * Update the task status given the task ID.
     *
     * @param taskId the task ID that need to be checked.
     * @param taskStatus state of the tasks [RESERVED, READY or COMPLETED].
     */
    void updateStatusOfRequest(String taskId, String taskStatus);

    /**
     * Returns the created time of the request.
     *
     * @param requestId the request ID that need to be checked.
     * @return the created time.
     */
    Timestamp getCreatedAtTimeInMill(String requestId);

    /**
     * Returns the relationship ID given the request ID.
     *
     * @param eventId the event ID that need to be checked.
     * @return the relationship ID.
     */
    String getRelationshipId(String eventId);

    /**
     * Returns the approvers list given the authenticated approver name.
     *
     * @param taskId the task ID that need to be checked.
     * @return approvers list.
     */
    List<String> listApprovers(String taskId);

    /**
     * Returns the task status given the request ID [RESERVED, READY or COMPLETED].
     *
     * @param requestId the request ID that need to be checked.
     * @return task status.
     */
    String getStatusOfTask(String requestId);
    List<String> getTaskId(String eventId);
}
