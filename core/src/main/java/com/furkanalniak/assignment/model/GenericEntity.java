package com.furkanalniak.assignment.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface GenericEntity extends Serializable {

  String getId();

  void setId(String id);

  Boolean getIsSimpleEntity();

  void setIsSimpleEntity(Boolean isSimpleEntity);

  LocalDateTime getCreateDate();

  void setCreateDate(LocalDateTime createDate);

  LocalDateTime getUpdateDate();

  void setUpdateDate(LocalDateTime updateDate);
}
