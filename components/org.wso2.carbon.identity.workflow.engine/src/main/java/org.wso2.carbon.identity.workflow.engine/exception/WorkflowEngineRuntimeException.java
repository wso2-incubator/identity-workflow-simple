package org.wso2.carbon.identity.workflow.engine.exception;

import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;

public class WorkflowEngineRuntimeException extends RuntimeException {

    public WorkflowEngineRuntimeException(String message) {

        super(message);
    }
    public WorkflowEngineRuntimeException(String message, DataAccessException e) {

        super(message);
    }

}
