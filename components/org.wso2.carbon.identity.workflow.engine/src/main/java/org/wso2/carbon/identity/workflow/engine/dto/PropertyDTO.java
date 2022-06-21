package org.wso2.carbon.identity.workflow.engine.dto;

/**
 * DTO class for task properties, map the task properties as key & value.
 */
public class PropertyDTO {

    private String key = null;

    private String value = null;

    /**
     * The key of the task property.
     **/
    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    /**
     * The value of the key of the task property.
     **/
    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class PropertyDTO {\n");
        sb.append("  key: ").append(key).append("\n");
        sb.append("  value: ").append(value).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}