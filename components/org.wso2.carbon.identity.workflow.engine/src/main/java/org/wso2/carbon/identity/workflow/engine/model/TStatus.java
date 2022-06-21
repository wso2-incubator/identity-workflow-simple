package org.wso2.carbon.identity.workflow.engine.model;

/**
 * Model class for task status [RESERVED, READY or COMPLETED].
 */
public class TStatus {

    protected String localTStatus;

    public TStatus() {

    }

    /**
     * The status of the task [RESERVED, READY or COMPLETED].
     */
    public String getTStatus() {

        return this.localTStatus;
    }

    public void setTStatus(String param) {

        this.localTStatus = param;
    }

}
