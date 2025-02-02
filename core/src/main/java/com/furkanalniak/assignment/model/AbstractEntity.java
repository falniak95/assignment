package com.furkanalniak.assignment.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class AbstractEntity implements Persistable<String>, GenericEntity, Cloneable {

  @Transient private static final long serialVersionUID = 1L;

  @Id private String id;

  private LocalDateTime createDate = LocalDateTime.now();

  private LocalDateTime updateDate = LocalDateTime.now();

  private Boolean isActive = Boolean.TRUE;

  @Transient private Boolean isSimpleEntity = Boolean.FALSE;

  @Override
  public boolean isNew() {
    return (getId() == null);
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public Boolean getIsSimpleEntity() {
    return isSimpleEntity;
  }

  @Override
  public void setIsSimpleEntity(Boolean isSimpleEntity) {
    this.isSimpleEntity = isSimpleEntity;
  }

  @Override
  public LocalDateTime getCreateDate() {
    return createDate;
  }

  @Override
  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  @Override
  public LocalDateTime getUpdateDate() {
    return updateDate;
  }

  @Override
  public void setUpdateDate(LocalDateTime updateDate) {
    this.updateDate = updateDate;
  }

  public Boolean isActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  @Override
  public int hashCode() {
    final int prime = 13;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    AbstractEntity other = (AbstractEntity) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
