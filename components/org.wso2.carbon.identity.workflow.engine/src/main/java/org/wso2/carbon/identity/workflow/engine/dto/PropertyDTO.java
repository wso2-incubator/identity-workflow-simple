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

        StringBuilder builder = new StringBuilder();
        builder.append("class PropertyDTO {\n");
        builder.append("  key: ").append(key).append("\n");
        builder.append("  value: ").append(value).append("\n");
        builder.append("}\n");
        return builder.toString();
    }
}