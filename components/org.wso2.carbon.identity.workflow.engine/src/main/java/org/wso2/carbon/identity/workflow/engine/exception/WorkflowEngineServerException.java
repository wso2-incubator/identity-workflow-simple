package org.wso2.carbon.identity.workflow.engine.exception;

/**
 * This class is used to define the server side errors which need to be handled.
 */
public class WorkflowEngineServerException extends WorkflowEngineException {

    public WorkflowEngineServerException(String message, String errorCode) {

        super(message, errorCode);
    }

    public WorkflowEngineServerException(String message, Throwable cause) {

        super(message, cause);
    }
}
