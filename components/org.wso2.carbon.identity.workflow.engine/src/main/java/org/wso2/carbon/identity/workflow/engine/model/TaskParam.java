package org.wso2.carbon.identity.workflow.engine.model;

/**
 * Task Parameter model, Get task parameters as list.
 */
public class TaskParam {

    private String itemName;
    private String itemValue;

    /**
     * Get the Item name of the task parameters.
     *
     * @return The Item name of the task parameters.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *  Set the Item name for the task parameters.
     *
     * @param itemName The Item name for the task parameters.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Get the Item value of the task parameters.
     *
     * @return The Item value of the task parameters.
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * Set the Item value for the task parameters.
     *
     * @param itemValue The Item value for the task parameters.
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
