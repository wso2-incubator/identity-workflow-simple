package org.wso2.carbon.identity.workflow.engine.util;

/**
 * This class holds the constants used in the module, identity-workflow-simple.
 */
public class WorkflowEngineConstants {

    public static final String CURRENT_STEP_COLUMN = "CURRENT_STEP";
    public static final String TASK_ID_COLUMN = "TASK_ID";
    public static final String APPROVER_NAME_COLUMN = "APPROVER_NAME";
    public static final String WORKFLOW_ID_COLUMN="WORKFLOW_ID";
    public static final String REQUEST_ID_COLUMN="REQUEST_ID";
    public static final String CREATED_BY_COLUMN="CREATED_BY";
    public static final String STATUS_COLUMN="STATUS";
    public static final String ENTITY_NAME_COLUMN="ENTITY_NAME";

    public static class SqlQueries {

        public static final String ADD_APPROVAL_LIST_RELATED_TO_USER = "INSERT INTO WF_WORKFLOW_APPROVAL_RELATION (TASK_ID,EVENT_ID,WORKFLOW_ID,APPROVER_TYPE,APPROVER_NAME) VALUES (?,?,?,?,?)";
        public static final String GET_TASK_ID_RELATED_TO_USER = "SELECT TASK_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID = ?";
        public static final String GET_APPROVER_NAME_RELATED_TO_CURRENT_STEP = "SELECT APPROVER_TYPE, APPROVER_NAME FROM WF_WORKFLOW_APPROVAL_RELATION WHERE EVENT_ID = ?";
        public static final String DELETE_APPROVAL_LIST_RELATED_TO_USER = "DELETE FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID=?";
        public static final String ADD_CURRENT_STEP_FOR_EVENT = "INSERT INTO WF_WORKFLOW_APPROVAL_STATE (EVENT_ID,WORKFLOW_ID, CURRENT_STEP) VALUES (?,?,?)";
        public static final String GET_CURRENT_STEP = "SELECT CURRENT_STEP FROM WF_WORKFLOW_APPROVAL_STATE WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";
        public static final String UPDATE_STATE_OF_REQUEST = "UPDATE WF_WORKFLOW_APPROVAL_STATE SET CURRENT_STEP=? WHERE EVENT_ID = ? AND WORKFLOW_ID = ?";
        public static final String GET_WORKFLOW_ID = "SELECT WORKFLOW_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID = ?";
        public static final String GET_REQUEST_ID = "SELECT EVENT_ID FROM WF_WORKFLOW_APPROVAL_RELATION WHERE TASK_ID = ?";
        public static final String GET_CREATED_USER = "SELECT CREATED_BY FROM WF_REQUEST WHERE UUID= ?";
        public static final String GET_STATES_OF_REQUEST= "SELECT STATUS FROM WF_WORKFLOW_REQUEST_RELATION WHERE UUID= ?";
        public static final String GET_USER_DETAILS="SELECT ENTITY_NAME FROM WF_REQUEST_ENTITY_RELATIONSHIP WHERE REQUEST_ID";
    }

    public static class ParameterName {
        public static final String USER_AND_ROLE_STEP = "UserAndRole" ;
        public static final String REQUEST_ID = "REQUEST ID" ;
        public static final String TASK_SUBJECT = "HTSubject";
        public static final String TASK_DESCRIPTION ="HTDescription";
        public static final String PRIORITY= "0";
    }
}
