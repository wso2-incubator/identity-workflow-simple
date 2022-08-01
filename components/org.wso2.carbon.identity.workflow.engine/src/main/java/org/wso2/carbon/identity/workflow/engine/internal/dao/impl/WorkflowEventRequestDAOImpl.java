package org.wso2.carbon.identity.workflow.engine.internal.dao.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.configuration.mgt.core.util.JdbcUtils;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineServerException;
import org.wso2.carbon.identity.workflow.engine.internal.dao.WorkflowEventRequestDAO;
import org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Workflow Event Request DAO implementation.
 */
public class WorkflowEventRequestDAOImpl implements WorkflowEventRequestDAO {

    private static final Log log = LogFactory.getLog(WorkflowEventRequestDAOImpl.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public void addApproverDetailsOfEvent(String taskId, String eventId, String workflowId, String approverType,
                                          String approverName, String taskStatus) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.ADD_APPROVAL_LIST_RELATED_TO_REQUEST,
                    preparedStatement -> {
                        preparedStatement.setString(1, taskId);
                        preparedStatement.setString(2, eventId);
                        preparedStatement.setString(3, workflowId);
                        preparedStatement.setString(4, approverType);
                        preparedStatement.setString(5, approverName);
                        preparedStatement.setString(6, taskStatus);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while adding request details" +
                    "in eventId: %s  & workflowId: %s", eventId, workflowId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage,e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getTaskIDOfEvent(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskIdExists;
        try {
            taskIdExists = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_ID_RELATED_TO_EVENT,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.TASK_ID_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
            if (taskIdExists == null) {
                return null;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving taskId from" +
                    "requestId: %s", eventId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskIdExists;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void deleteApproversOfTask(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.DELETE_APPROVAL_LIST_RELATED_TO_TASK,
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error while deleting the approver details from taskId:%s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void createCurrentStepOfEvent(String eventId, String workflowId, int currentStep) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.ADD_CURRENT_STEP_FOR_EVENT,
                    preparedStatement -> {
                        preparedStatement.setString(1, eventId);
                        preparedStatement.setString(2, workflowId);
                        preparedStatement.setInt(3, currentStep);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while adding request approval steps" +
                    "in event Id: %s & workflowId: %s", eventId, workflowId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentStepOfEvent(String eventId, String workflowId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String stepExists;
        try {
            stepExists = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.GET_CURRENT_STEP,
                    ((resultSet, i) -> (
                            Integer.toString(resultSet.getInt(WorkflowEngineConstants.CURRENT_STEP_COLUMN)))),
                    preparedStatement -> {
                        preparedStatement.setString(1, eventId);
                        preparedStatement.setString(2, workflowId);
                    });
            if (stepExists == null) {
                return 0;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving currentStep from" +
                    "event Id: %s & workflowId: %s", eventId, workflowId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return Integer.parseInt(stepExists);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void updateCurrentStepOfEvent(String eventId, String workflowId, int currentStep) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.UPDATE_CURRENT_STEP_OF_EVENT,
                    (preparedStatement -> {
                        setPreparedStatementForStateOfRequest(currentStep, eventId, workflowId, preparedStatement);
                        preparedStatement.setInt(1, currentStep);
                        preparedStatement.setString(2, eventId);
                        preparedStatement.setString(3, workflowId);
                    }));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while updating state from" +
                    "eventId: %s", eventId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
    }

    private void setPreparedStatementForStateOfRequest(int currentStep, String eventId, String workflowId,
                                                       PreparedStatement preparedStatement) throws SQLException {

        preparedStatement.setInt(1, currentStep);
        preparedStatement.setString(2, eventId);
        preparedStatement.setString(3, workflowId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listApprovers(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> requestsList;
        try {
            requestsList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_APPROVER_NAME_RELATED_TO_CURRENT_TASK_ID, (resultSet, rowNumber) ->
                            resultSet.getString(WorkflowEngineConstants.APPROVER_NAME_COLUMN),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = "Error occurred while retrieving approvers";
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return requestsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getRolesIDOfUser(String userName) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<Integer> roleIdList;
        try {
            roleIdList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_ROLE_ID, (resultSet, rowNumber) ->
                            resultSet.getInt(WorkflowEngineConstants.ROLE_ID_COLUMN),
                    preparedStatement -> preparedStatement.setString(1, userName));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving Role ID from" +
                    "user name: %s", userName);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return roleIdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRoleNamesOfRoleID(int roleId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> roleNameList;
        try {
            roleNameList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_ROLE_NAME, (resultSet, rowNumber) ->
                            resultSet.getString(WorkflowEngineConstants.ROLE_NAME),
                    preparedStatement -> preparedStatement.setInt(1, roleId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving Role name from" +
                    "role ID: %d", roleId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return roleNameList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestIDFromTask(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String requestId;
        try {
            requestId = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_REQUEST_ID,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.EVENT_ID))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving request ID from" +
                    "taskID: %s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return requestId;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getInitiatedUserOfRequest(String requestId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String createdBy;
        try {
            createdBy = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_CREATED_USER,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.CREATED_USER_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, requestId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving initiator from" +
                    "requestId: %s", requestId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getEventsListOfApprover(String approverName) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> requestIdList;
        try {
            requestIdList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_REQUEST_ID_FROM_APPROVER, (resultSet, rowNumber) ->
                            resultSet.getString(WorkflowEngineConstants.EVENT_ID),
                    preparedStatement -> preparedStatement.setString(1, approverName));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving request id from" +
                    "approver name: %s", approverName);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return requestIdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEventTypeOfEvent(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String eventType;
        try {
            eventType = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_EVENT_TYPE,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.EVENT_TYPE_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving event type from" +
                    "request : %s", eventId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return eventType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskStatusOfTask(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_STATUS,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.TASK_STATUS_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving task status from" +
                    "task Id: %s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatusOfRequest(String requestId) {

       JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
            String taskStatus;
            try {
                taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                                GET_STATUS_OF_EVENT,
                        ((resultSet, i) -> (
                                resultSet.getString(WorkflowEngineConstants.TASK_STATUS_COLUMN))),
                        preparedStatement -> preparedStatement.setString(1, requestId));
            } catch (DataAccessException e) {
                String errorMessage = String.format("Error occurred while retrieving task status from" +
                        "request Id: %s", requestId);
                if (log.isDebugEnabled()) {
                    log.debug(errorMessage, e);
                }
                throw new WorkflowEngineServerException(errorMessage, e);
            }
            return taskStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatusOfTask(String taskId, String taskStatus) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.UPDATE_TASK_STATUS,
                    (preparedStatement -> {
                        setPreparedStatementForStatusOfRequest(taskStatus, taskId, preparedStatement);
                        preparedStatement.setString(1, taskStatus);
                        preparedStatement.setString(2, taskId);
                    }));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while updating status from" +
                    "taskID: %s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelationshipId(String requestId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_REQUEST_ID_OF_RELATIONSHIP,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.RELATIONSHIP_ID_IN_REQUEST_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, requestId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving relationship ID from" +
                    "event Id: %s", requestId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskStatus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getTaskId(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> taskIdList;
        try {
            taskIdList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_ID_FROM_EVENT, (resultSet, rowNumber) ->
                            resultSet.getString(WorkflowEngineConstants.TASK_ID_COLUMN),
                    preparedStatement -> preparedStatement.setString(1, eventId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving tasks from" +
                    "request id : %s", eventId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskIdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp getCreatedAtTimeInMill(String requestId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        java.sql.Timestamp createdTime;
        try {
            createdTime = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_CREATED_TIME_IN_MILL,
                    ((resultSet, i) -> (
                            resultSet.getTimestamp(WorkflowEngineConstants.CREATED_AT_IN_MILL_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, requestId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving createdAt time from" +
                    "request Id: %s", requestId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return createdTime;
    }

    private void setPreparedStatementForStatusOfRequest(String taskStatus, String taskId,
                                                        PreparedStatement preparedStatement) throws SQLException {

        preparedStatement.setString(1, taskStatus);
        preparedStatement.setString(2, taskId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCurrentStepOfRequest(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.DELETE_CURRENT_STEP_OF_REQUEST,
                    preparedStatement -> preparedStatement.setString(1, eventId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error while deleting the current step from eventID:%s", eventId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getWorkflowID(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_WORKFLOW_ID,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.WORKFLOW_ID))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving workflow from" +
                    "task Id: %s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskStatus;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getWorkflowName(String workflowID) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_WORKFLOW_NAME,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.WORKFLOW_NAME))),
                    preparedStatement -> preparedStatement.setString(1, workflowID));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving workflow name from" +
                    "workflow Id: %s", workflowID);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskStatus;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getEntityNameOfRequest(String requestID) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_ENTITY_NAME,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.ENTITY_NAME))),
                    preparedStatement -> preparedStatement.setString(1, requestID));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving workflow name from" +
                    "workflow Id: %s", requestID);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskStatus;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getTask(String taskId){

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskExist;
        try {
            taskExist = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.GET_TASK_DATA,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.TASK_STATUS_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
            if (taskExist == null) {
                return null;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving taskDataDTO from" +
                    "task Id: %s", taskId);
            if (log.isDebugEnabled()) {
                log.debug(errorMessage, e);
            }
            throw new WorkflowEngineServerException(errorMessage, e);
        }
        return taskExist;
    }
}
