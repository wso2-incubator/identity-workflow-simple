package org.wso2.carbon.identity.workflow.engine.internal.dao.impl;

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.configuration.mgt.core.util.JdbcUtils;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineRuntimeException;
import org.wso2.carbon.identity.workflow.engine.internal.dao.WorkflowEventRequestDAO;
import org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
