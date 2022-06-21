package org.wso2.carbon.identity.workflow.engine.util;

/**
 * This class holds the constants used in the module, identity-workflow-simple.
 */
public class WorkflowEngineConstants {

    public static final String CURRENT_STEP_COLUMN = "CURRENT_STEP";
    public static final String TASK_ID_COLUMN = "TASK_ID";

    public static class SqlQueries {

        public static final String ADD_APPROVAL_LIST_RELATED_TO_USER = "INSERT INTO WF_WORKFLOW_APPROVAL_RELATION (TASK_ID,EVENT_ID,WORKFLOW_ID,APPROVER_TYPE,APPROVER_NAME, TASK_STATUS) VALUES (?,?,?,?,?,?)";
        public static final String GET_TASK_ID_RELATED_TO_USER = "SELECT DISTINCT TASK_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID = ?";
        public static final String DELETE_APPROVAL_LIST_RELATED_TO_USER = "DELETE FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID=?";
        public static final String ADD_CURRENT_STEP_FOR_EVENT = "INSERT INTO WF_WORKFLOW_APPROVAL_STATE (EVENT_ID,WORKFLOW_ID, CURRENT_STEP) VALUES (?,?,?)";
        public static final String GET_CURRENT_STEP = "SELECT CURRENT_STEP FROM WF_WORKFLOW_APPROVAL_STATE WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";
        public static final String UPDATE_STATE_OF_REQUEST = "UPDATE WF_WORKFLOW_APPROVAL_STATE SET CURRENT_STEP=? WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";

    }

    public static class ParameterName {

        public static final String USER_AND_ROLE_STEP = "UserAndRole";
        public static final String TASK_STATUS_DEFAULT = "RESERVED";
        public static final String REQUEST_ID = "REQUEST ID";
        public static final String TASK_SUBJECT = "HTSubject";
        public static final String TASK_DESCRIPTION = "HTDescription";
        public static final String PRIORITY = "0";
        public static final String TENANT_NAME = "TenantNameFromContext";
        public static final String APPROVAL_TASK = "Approve task";
        public static final String ADMIN = "admin";
        public static final String INTERNAL_ADMIN = "Internal/";
        public static final String ASSIGNEE_TYPE = "Type";
        public static final String STATUS_ERROR = "Status is notAcceptable";
        public static final String NOT_ACCEPTABLE_INPUT_FOR_NEXT_STATE = "Unacceptable input provided\",\n" +
                "                \"Only [CLAIM, RELEASE, APPROVED, REJECTED] are acceptable";
    }

    public enum TaskStatus {
        READY,
        RESERVED,
        COMPLETED;

        private TaskStatus() {

        }
    }
}
