package org.wso2.carbon.identity.workflow.engine.internal;

import org.wso2.carbon.identity.workflow.mgt.WorkflowExecutorManagerService;
import org.wso2.carbon.identity.workflow.mgt.WorkflowManagementService;
import org.wso2.carbon.identity.workflow.mgt.workflow.AbstractWorkflow;

import java.util.HashMap;
import java.util.Map;

public class WorkflowEngineServiceDataHolder {

    private static WorkflowEngineServiceDataHolder instance = new WorkflowEngineServiceDataHolder();

    private WorkflowManagementService workflowManagementService;

    private WorkflowExecutorManagerService workflowExecutorManagerService;

    private WorkflowEngineServiceDataHolder() {

    }

    public static WorkflowEngineServiceDataHolder getInstance() {

        return instance;
    }

    public WorkflowManagementService getWorkflowManagementService() {
        return workflowManagementService;
    }

    public void setWorkflowManagementService(
            WorkflowManagementService workflowManagementService) {
        this.workflowManagementService = workflowManagementService;
    }

    public WorkflowExecutorManagerService getWorkflowExecutorManagerService() {

        return workflowExecutorManagerService;
    }

    public void setWorkflowExecutorManagerService(
            WorkflowExecutorManagerService workflowExecutorManagerService) {
        this.workflowExecutorManagerService = workflowExecutorManagerService;
    }
    private Map<String, Map<String,AbstractWorkflow>> workflowImpls = new HashMap<String, Map<String,AbstractWorkflow>>();
    public Map<String, Map<String, AbstractWorkflow>> getWorkflowImpls() {
        return workflowImpls;
    }
}
