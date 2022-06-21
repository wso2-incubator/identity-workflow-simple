package org.wso2.carbon.identity.workflow.engine.model;

/**
 * model class for task subject & task description.
 */
public class TaskDetails {
    String taskDescription;
    String taskSubject;

    /**
     * The subject of the task.
     */
    public String getTaskDescription() {

        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {

        this.taskDescription = taskDescription;
    }

    /**
     * The description of the task.
     */
    public String getTaskSubject() {

        return taskSubject;
    }

    public void setTaskSubject(String taskSubject) {

        this.taskSubject = taskSubject;
    }
}
