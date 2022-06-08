package org.wso2.carbon.identity.workflow.engine;

import org.wso2.carbon.identity.workflow.mgt.bean.Parameter;
import org.wso2.carbon.identity.workflow.mgt.bean.WorkflowAssociation;
import org.wso2.carbon.identity.workflow.mgt.dto.WorkflowRequest;

import java.util.List;

public interface DefaultWorkflowEventRequest {

    /**
     * Add who approves the relevant request.
     *
     * @param request       workflow request object.
     * @param parameterList parameterList.
     */
    void addApproversOfRequests(WorkflowRequest request, List<Parameter> parameterList);

    /**
     * Get taskId from WF_REQUEST_APPROVAL_RELATION table.
     *
     * @param eventId   the request ID that need to be checked.
     * @return task Id.
     */
    String getApprovalOfRequest(String eventId);

    /**
     * Delete approver details using task Id.
     *
     * @param taskId random generated unique Id.
     */
     void deleteApprovalOfRequest(String taskId);

    /**
     * Add current step.
     *
     * @param eventId the request ID that need to be checked.
     * @param workflowId workflow id.
     * @param currentStep current step of the flow.
     */
    void createStatesOfRequest(String eventId, String workflowId, int currentStep);

    /**
     * Get current step from the table.
     *
     * @param eventId    the request ID that need to be checked.
     * @param workflowId workflow Id.
     * @return currentStep.
     */
     int getStateOfRequest(String eventId, String workflowId);

    /**
     *Update current step according to the eventId and workflowId.
     *
     * @param eventId the request ID that need to be checked.
     * @param workflowId workflow Id.
     */
    void updateStateOfRequest(String eventId, String workflowId);

    /**
     * Get related associations.
     *
     * @param workflowRequest request object.
     * @return association list.
     */
    List<WorkflowAssociation> getAssociations(WorkflowRequest workflowRequest);

    /**
     * Get relevant workflow id to request.
     *
     * @param request request object.
     * @return workflow Id.
     */
    String getWorkflowId(WorkflowRequest request);
}
