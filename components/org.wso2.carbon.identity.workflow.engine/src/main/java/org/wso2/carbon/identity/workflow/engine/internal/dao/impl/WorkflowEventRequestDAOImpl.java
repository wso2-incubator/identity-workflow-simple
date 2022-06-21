package org.wso2.carbon.identity.workflow.engine.internal.dao.impl;

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.configuration.mgt.core.util.JdbcUtils;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineRuntimeException;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String addApproversOfRequest(String taskId, String eventId, String workflowId, String approverType,
                                        String approverName, String taskStatus) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.ADD_APPROVAL_LIST_RELATED_TO_USER,
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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskId;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getApproversOfRequest(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskIdExists;
        try {
            taskIdExists = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_ID_RELATED_TO_USER,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.TASK_ID_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
            if (taskIdExists == null) {
                return null;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving taskId from" +
                    "requestId: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskIdExists;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void deleteApproversOfRequest(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.DELETE_APPROVAL_LIST_RELATED_TO_USER,
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error while deleting the approver details from taskId:%s", taskId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void createStatesOfRequest(String eventId, String workflowId, int currentStep) {

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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStateOfRequest(String eventId, String workflowId) {

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
                    "event Id: %s & workflowId: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return Integer.parseInt(stepExists);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void updateStateOfRequest(String eventId, String workflowId, int currentStep) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.UPDATE_STATE_OF_REQUEST,
                    (preparedStatement -> {
                        setPreparedStatementForStateOfRequest(currentStep, eventId, workflowId, preparedStatement);
                        preparedStatement.setInt(1, currentStep);
                        preparedStatement.setString(2, eventId);
                        preparedStatement.setString(3, workflowId);
                    }));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while updating state from" +
                    "eventId: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
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
    public String getApproversOfCurrentStep(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String approverName;
        try {
            approverName = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_APPROVER_NAME_RELATED_TO_CURRENT_TASK_ID,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.APPROVER_NAME_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving approverName from" +
                    "task Id: %s", taskId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return approverName;
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
                            new String(resultSet.getString(WorkflowEngineConstants.APPROVER_NAME_COLUMN)),
                    preparedStatement -> {
                        preparedStatement.setString(1, taskId);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving approvers from" +
                    "task Id: %s", taskId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return requestsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRequestID(String taskId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String requestId;
        try {
            requestId = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_REQUEST_ID,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.Event_ID_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, taskId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving request ID from" +
                    "task Id: %s", taskId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return requestId;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public String getInitiatedUser(String requestId) {

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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return createdBy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRequestIdFromApprover(String approverName) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> requestIdList;
        try {
            requestIdList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_EVENT_ID_FROM_APPROVER, (resultSet, rowNumber) ->
                            new String(resultSet.getString(WorkflowEngineConstants.EVENT_ID_FROM_APPROVER_COLUMN)),
                    preparedStatement -> {
                        preparedStatement.setString(1, approverName);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving requestID from" +
                    "approver name: %s", approverName);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return requestIdList;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getTaskIdList(String approverName) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> requestIdList;
        try {
            requestIdList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_ID_FROM_APPROVER, (resultSet, rowNumber) ->
                            new String(resultSet.getString(WorkflowEngineConstants.TASK_ID_COLUMN)),
                    preparedStatement -> {
                        preparedStatement.setString(1, approverName);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving task id from" +
                    "approver name: %s", approverName);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return requestIdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRequestsFromAdmin(String approverName, String name) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<String> requestsList;
        try {
            requestsList = jdbcTemplate.executeQuery(WorkflowEngineConstants.SqlQueries.
                            GET_EVENT_ID_FROM_ADMIN, (resultSet, rowNumber) ->
                            new String(resultSet.getString(WorkflowEngineConstants.EVENT_ID_FROM_APPROVER_COLUMN)),
                    preparedStatement -> {
                        preparedStatement.setString(1, approverName);
                        preparedStatement.setString(2, name);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving requestID from" +
                    "admin: %s", approverName);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return requestsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEventType(String requestId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String eventType;
        try {
            eventType = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_EVENT_TYPE,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.EVENT_TYPE_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, requestId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving event type from" +
                    "request Id: %s", requestId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return eventType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskStatusOfRequest(String taskId) {

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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskStatus;
    }

    @Override
    public void updateStatusOfRequest(String taskId, String taskStatus) {

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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRelationshipId(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskStatus;
        try {
            taskStatus = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_REQUEST_ID_OF_RELATIONSHIP,
                    ((resultSet, i) -> (
                            resultSet.getString(WorkflowEngineConstants.RELATIONSHIP_ID_IN_REQUEST_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving relationship ID from" +
                    "event Id: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskStatus;
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
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return createdTime;
    }

    private void setPreparedStatementForStatusOfRequest(String taskStatus, String taskId,
                                                        PreparedStatement preparedStatement) throws SQLException {

        preparedStatement.setString(1, taskStatus);
        preparedStatement.setString(2, taskId);
    }
}
