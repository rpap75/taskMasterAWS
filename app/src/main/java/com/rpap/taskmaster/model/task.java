package com.rpap.taskmaster.model;

public class task {
    public Long id;
    private String title;
    private String body;
    private taskStatusEnum status;

    public task(String title, String body, taskStatusEnum status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public taskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(taskStatusEnum status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public enum taskStatusEnum {
        NEW("new"),
        ASSIGNED("assigned"),
        IN_PROGRESS("in progress"),
        COMPLETE("complete");

        private final String taskStatus;

        taskStatusEnum(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public static taskStatusEnum fromString(String possibleTaskStatus){
            for (taskStatusEnum status: taskStatusEnum.values()) {
                if (status.taskStatus.equals(possibleTaskStatus)){
                    return status;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return taskStatus;
        }
    }
}
