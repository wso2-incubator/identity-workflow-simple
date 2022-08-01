package org.wso2.carbon.identity.workflow.engine.model;

/**
 * The model class for set up the page pagination.
 */
public class PagePagination {

    private int localPageSize;
    private boolean localPageSizeTracker = false;
    private int localPageNumber;
    private boolean localPageNumberTracker = false;

    /**
     * Maximum number of results expected.
     */
    public int getPageSize() {

        return this.localPageSize;
    }

    /**
     * Set maximum number of results expected.
     */
    public void setPageSize(int param) {

        this.localPageSizeTracker = param != Integer.MIN_VALUE;
        this.localPageSize = param;
    }

    /**
     * Start index of the search.
     */
    public int getPageNumber() {

        return this.localPageNumber;
    }

    /**
     * Set start index of the search.
     */
    public void setPageNumber(int param) {

        this.localPageNumberTracker = param != Integer.MIN_VALUE;
        this.localPageNumber = param;
    }
}
