package org.wso2.carbon.identity.workflow.engine.dao.impl;

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.configuration.mgt.core.util.JdbcUtils;
import org.wso2.carbon.identity.workflow.engine.dao.WorkflowEventRequestDAO;
import org.wso2.carbon.identity.workflow.engine.exception.WorkflowEngineRuntimeException;
import org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants.APPROVER_NAME_COLUMN;
import static org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants.CURRENT_STEP_COLUMN;
import static org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants.SqlQueries.ADD_CURRENT_STEP_FOR_EVENT;
import static org.wso2.carbon.identity.workflow.engine.util.WorkflowEngineConstants.TASK_ID_COLUMN;

public class WorkflowEventRequestDAOImpl implements WorkflowEventRequestDAO {

    @Override
    public String addApproversOfRequest(String taskId, String eventId, String workflowId, String approverType,
                                        String approverName) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(WorkflowEngineConstants.SqlQueries.ADD_APPROVAL_LIST_RELATED_TO_USER,
                    preparedStatement -> {
                        preparedStatement.setString(1, taskId);
                        preparedStatement.setString(2, eventId);
                        preparedStatement.setString(3, workflowId);
                        preparedStatement.setString(4, approverType);
                        preparedStatement.setString(5, approverName);
                    });
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while adding request details" +
                    "in eventId: %s  & workflowId: %s", eventId, workflowId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskId;
    }

    public String getApproversOfRequest(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String taskIdExists;
        try {
            taskIdExists = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_TASK_ID_RELATED_TO_USER,
                    ((resultSet, i) -> (
                            resultSet.getString(TASK_ID_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
            if (taskIdExists == null) {
                return null;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving taskId from" +
                    "task Id: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return taskIdExists;
    }

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

    @Override
    public void createStatesOfRequest(String eventId, String workflowId, int currentStep) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(ADD_CURRENT_STEP_FOR_EVENT,
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

    @Override
    public int getStateOfRequest(String eventId, String workflowId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String stepExists;
        try {
            stepExists = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.GET_CURRENT_STEP,
                    ((resultSet, i) -> (
                            Integer.toString(resultSet.getInt(CURRENT_STEP_COLUMN)))),
                    preparedStatement -> {
                        preparedStatement.setString(1, eventId);
                        preparedStatement.setString(2, workflowId);
                    });
            if (stepExists == null) {
                return 0;
            }
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving currentStep from" +
                    "event Id: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return Integer.parseInt(stepExists);
    }

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
                    "eventIs:%s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
    }

    public String getApproversOfCurrentStep(String eventId) {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        String approverName;
        try {
            approverName = jdbcTemplate.fetchSingleRecord(WorkflowEngineConstants.SqlQueries.
                            GET_APPROVER_NAME_RELATED_TO_CURRENT_STEP,
                    ((resultSet, i) -> (
                            resultSet.getString(APPROVER_NAME_COLUMN))),
                    preparedStatement -> preparedStatement.setString(1, eventId));
        } catch (DataAccessException e) {
            String errorMessage = String.format("Error occurred while retrieving currentStep from" +
                    "event Id: %s", eventId);
            throw new WorkflowEngineRuntimeException(errorMessage);
        }
        return approverName;
    }

    private void setPreparedStatementForStateOfRequest(int currentStep, String eventId, String workflowId,
                                                       PreparedStatement preparedStatement) throws SQLException {

        preparedStatement.setInt(1, currentStep);
        preparedStatement.setString(2, eventId);
        preparedStatement.setString(3, workflowId);
    }
}