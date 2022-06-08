package org.wso2.carbon.identity.workflow.engine.dao;

public interface WorkflowEventRequestDAO {

    /**
     * Add who approves the relevant request.
     *
     * @param taskId       random generated unique Id.
     * @param eventId      the request ID that need to be checked.
     * @param workflowId   workflow ID.
     * @param approverType the type of the approved user EX: user or Role.
     * @param approverName the value of the approver type.
     * @return event ID.
     */
    String addApproversOfRequest(String taskId, String eventId, String workflowId, String approverType, String approverName);

    /**
     * Get taskId from table.
     *
     * @param eventId the request ID that need to be checked.
     * @return task Id.
     */
    String getApproversOfRequest(String eventId);

    /**
     * Get approverName related to currentStep.
     *
     * @param eventId the request ID that need to be checked.
     * @return the value of the approver type.
     */
    String getApproversOfCurrentStep(String eventId);

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
}
