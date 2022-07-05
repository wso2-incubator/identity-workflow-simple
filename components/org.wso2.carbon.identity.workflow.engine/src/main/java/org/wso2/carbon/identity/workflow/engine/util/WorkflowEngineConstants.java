package org.wso2.carbon.identity.workflow.engine.util;

/**
 * This class holds the constants used in the module, identity-workflow-simple.
 */
public class WorkflowEngineConstants {

    public static final String CURRENT_STEP_COLUMN = "CURRENT_STEP";
    public static final String TASK_ID_COLUMN = "TASK_ID";
    public static final String APPROVER_NAME_COLUMN = "APPROVER_NAME";
    public static final String EVENT_ID = "EVENT_ID";
    public static final String CREATED_USER_COLUMN = "CREATED_BY";
    public static final String EVENT_TYPE_COLUMN = "OPERATION_TYPE";
    public static final String TASK_STATUS_COLUMN = "TASK_STATUS";
    public static final String CREATED_AT_IN_MILL_COLUMN = "CREATED_AT";
    public static final String RELATIONSHIP_ID_IN_REQUEST_COLUMN = "RELATIONSHIP_ID";
    public static final String ROLE_ID_COLUMN = "UM_ROLE_ID";
    public static final String ROLE_NAME = "UM_ROLE_NAME";

    public static class SqlQueries {

        public static final String ADD_APPROVAL_LIST_RELATED_TO_USER = "INSERT INTO WF_WORKFLOW_APPROVAL_RELATION (TASK_ID,EVENT_ID,WORKFLOW_ID,APPROVER_TYPE,APPROVER_NAME, TASK_STATUS) VALUES (?,?,?,?,?,?)";
        public static final String GET_TASK_ID_RELATED_TO_USER = "SELECT DISTINCT TASK_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID = ?";
        public static final String DELETE_APPROVAL_LIST_RELATED_TO_USER = "DELETE FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID=?";
        public static final String ADD_CURRENT_STEP_FOR_EVENT = "INSERT INTO WF_WORKFLOW_APPROVAL_STATE (EVENT_ID,WORKFLOW_ID, CURRENT_STEP) VALUES (?,?,?)";
        public static final String GET_CURRENT_STEP = "SELECT CURRENT_STEP FROM WF_WORKFLOW_APPROVAL_STATE WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";
        public static final String UPDATE_STATE_OF_REQUEST = "UPDATE WF_WORKFLOW_APPROVAL_STATE SET CURRENT_STEP=? WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";
        public static final String DELETE_CURRENT_STEP_OF_REQUEST = "DELETE FROM WF_WORKFLOW_APPROVAL_STATE WHERE EVENT_ID=?";
        public static final String GET_APPROVER_NAME_RELATED_TO_CURRENT_TASK_ID = "SELECT DISTINCT APPROVER_NAME FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID = ?";
        public static final String UPDATE_TASK_STATUS = "UPDATE WF_WORKFLOW_APPROVAL_RELATION SET TASK_STATUS=? WHERE TASK_ID=?";
        public static final String GET_REQUEST_ID = "SELECT DISTINCT EVENT_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID = ?";
        public static final String GET_TASK_ID_FROM_APPROVER = "SELECT TASK_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE APPROVER_NAME= ?";
        public static final String GET_TASK_ID_FROM_REQUEST = "SELECT TASK_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID= ?";
        public static final String GET_REQUEST_ID_FROM_APPROVER = "SELECT EVENT_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE APPROVER_NAME= ?";
        public static final String GET_TASK_STATUS = "SELECT DISTINCT TASK_STATUS FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID = ?";
        public static final String GET_STATUS = "SELECT DISTINCT TASK_STATUS FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID = ?";
        public static final String GET_CREATED_USER = "SELECT CREATED_BY FROM WF_REQUEST WHERE UUID= ?";
        public static final String GET_EVENT_TYPE = "SELECT OPERATION_TYPE FROM WF_REQUEST WHERE UUID= ?";
        public static final String GET_CREATED_TIME_IN_MILL = "SELECT CREATED_AT FROM WF_REQUEST WHERE UUID= ?";
        public static final String GET_REQUEST_ID_OF_RELATIONSHIP = "SELECT RELATIONSHIP_ID FROM " +
                "WF_WORKFLOW_REQUEST_RELATION WHERE REQUEST_ID = ?";
        public static final String GET_ROLE_ID = "SELECT UM_ROLE_ID FROM UM_HYBRID_USER_ROLE WHERE UM_USER_NAME=?";
        public static final String GET_ROLE_NAME = "SELECT UM_ROLE_NAME FROM UM_HYBRID_ROLE WHERE UM_ID=?";
    }

    public static class ParameterName {

        public static final String USER_AND_ROLE_STEP = "UserAndRole";
        public static final String TASK_STATUS_DEFAULT = "RESERVED";
        public static final String REQUEST_ID = "REQUEST ID";
        public static final String TASK_SUBJECT = "HTSubject";
        public static final String TASK_DESCRIPTION = "HTDescription";
        public static final String PRIORITY = "0";
        public static final String APPROVAL_TASK = "Approval task";
        public static final String INTERNAL_USER = "Internal/";
        public static final String APPLICATION_USER = "Application";
        public static final String SYSTEM_USER = "system";
        public static final String SYSTEM_PRIMARY_USER = "system_primary_";
        public static final String ASSIGNEE_TYPE = "Type";
        public static final String STATUS_ERROR = "Status is notAcceptable";
        public static final String CREDENTIAL = "Credential";
        public static final String HT_SUBJECT = "HTSubject";
    }

    public enum TaskStatus {
        READY,
        RESERVED,
        COMPLETED;

        TaskStatus() {

        }
    }

    public static class ParameterHolder {
        public static final String WORKFLOW_IMPL = "WorkflowImpl";
    }

    public static class ParameterValue {
        public static final String WORKFLOW_NAME = "WorkflowName";
    }
}
