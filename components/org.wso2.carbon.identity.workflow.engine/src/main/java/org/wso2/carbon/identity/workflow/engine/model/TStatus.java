package org.wso2.carbon.identity.workflow.engine.model;

/**
 * Model class for task status [RESERVED, READY or COMPLETED].
 */
public class TStatus {

    protected String localTStatus;

    public TStatus() {

    }

    /**
     * Get the status of the task [RESERVED, READY or COMPLETED].
     */
    public String getTStatus() {

        return this.localTStatus;
    }

    /**
     * Set the status of the task [RESERVED, READY or COMPLETED].
     */
    public void setTStatus(String param) {

        this.localTStatus = param;
    }
}
