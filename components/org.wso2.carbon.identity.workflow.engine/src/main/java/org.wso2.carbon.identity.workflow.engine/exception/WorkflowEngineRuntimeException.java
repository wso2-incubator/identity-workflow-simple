package org.wso2.carbon.identity.workflow.engine.exception;

import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.workflow.mgt.exception.InternalWorkflowException;

/**
 * Exception class to handle runtime exceptions.
 */
public class WorkflowEngineRuntimeException extends RuntimeException {

    public WorkflowEngineRuntimeException(String message) {

        super(message);
    }
    public WorkflowEngineRuntimeException(String message, DataAccessException e) {

        super(message);
    }
}
