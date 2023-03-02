package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "tasks", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byTeams", fields = {"taskTeamId","title"})
public final class task implements Model {
  public static final QueryField ID = field("task", "id");
  public static final QueryField TITLE = field("task", "title");
  public static final QueryField BODY = field("task", "body");
  public static final QueryField STATUS = field("task", "status");
  public static final QueryField TASK_TEAM = field("task", "taskTeamId");
  public static final QueryField S3_IMAGE_KEY = field("task", "s3ImageKey");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="taskStatusEnum") TaskStatusEnum status;
  private final @ModelField(targetType="taskTeam") @BelongsTo(targetName = "taskTeamId", targetNames = {"taskTeamId"}, type = taskTeam.class) taskTeam taskTeam;
  private final @ModelField(targetType="String") String s3ImageKey;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getBody() {
      return body;
  }
  
  public TaskStatusEnum getStatus() {
      return status;
  }
  
  public taskTeam getTaskTeam() {
      return taskTeam;
  }
  
  public String getS3ImageKey() {
      return s3ImageKey;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private task(String id, String title, String body, TaskStatusEnum status, taskTeam taskTeam, String s3ImageKey) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.status = status;
    this.taskTeam = taskTeam;
    this.s3ImageKey = s3ImageKey;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      task task = (task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getBody(), task.getBody()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getTaskTeam(), task.getTaskTeam()) &&
              ObjectsCompat.equals(getS3ImageKey(), task.getS3ImageKey()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getBody())
      .append(getStatus())
      .append(getTaskTeam())
      .append(getS3ImageKey())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("taskTeam=" + String.valueOf(getTaskTeam()) + ", ")
      .append("s3ImageKey=" + String.valueOf(getS3ImageKey()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static task justId(String id) {
    return new task(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      body,
      status,
      taskTeam,
      s3ImageKey);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    task build();
    BuildStep id(String id);
    BuildStep body(String body);
    BuildStep status(TaskStatusEnum status);
    BuildStep taskTeam(taskTeam taskTeam);
    BuildStep s3ImageKey(String s3ImageKey);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String body;
    private TaskStatusEnum status;
    private taskTeam taskTeam;
    private String s3ImageKey;
    @Override
     public task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new task(
          id,
          title,
          body,
          status,
          taskTeam,
          s3ImageKey);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep status(TaskStatusEnum status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep taskTeam(taskTeam taskTeam) {
        this.taskTeam = taskTeam;
        return this;
    }
    
    @Override
     public BuildStep s3ImageKey(String s3ImageKey) {
        this.s3ImageKey = s3ImageKey;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String body, TaskStatusEnum status, taskTeam taskTeam, String s3ImageKey) {
      super.id(id);
      super.title(title)
        .body(body)
        .status(status)
        .taskTeam(taskTeam)
        .s3ImageKey(s3ImageKey);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder status(TaskStatusEnum status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder taskTeam(taskTeam taskTeam) {
      return (CopyOfBuilder) super.taskTeam(taskTeam);
    }
    
    @Override
     public CopyOfBuilder s3ImageKey(String s3ImageKey) {
      return (CopyOfBuilder) super.s3ImageKey(s3ImageKey);
    }
  }
  
}
