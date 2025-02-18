package org.wso2.carbon.identity.workflow.engine.exception;

/**
 * Exception class to handle client exceptions.
 */
public class WorkflowEngineClientException extends WorkflowEngineException {

    public WorkflowEngineClientException(String message, String errorCode) {

        super(message, errorCode);
    }
}
