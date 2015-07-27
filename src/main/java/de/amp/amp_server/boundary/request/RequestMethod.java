package de.amp.amp_server.boundary.request;

import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.datasource.entity.User;

public abstract class RequestMethod<T> {

  private final Class<T> entityClass;

  public RequestMethod(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public Class<T> getEntityClass() {
    return entityClass;
  }

  public String getMethodName() {
    return this.getClass().getSimpleName();
  }

  public abstract Response process(User user, T requestEntity);

}
