package de.amp.amp_server.boundary.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class RequestMethodRegistry {

  private RequestMethodRegistry() {
  }

  private static final List<RequestMethod> REQUEST_METHODS = new ArrayList<>();
  private static final Class[] ENTITY_CLASSES;

  static {
    REQUEST_METHODS.add(new GetDatabaseStats());
    REQUEST_METHODS.add(new ChangeState());

    ENTITY_CLASSES = REQUEST_METHODS.stream()
            .filter(requestMethod -> !requestMethod.getEntityClass().equals(String.class))
            .map(RequestMethod::getEntityClass)
            .toArray(Class[]::new);
  }

  public static Class[] getEntityClasses() {
    return ENTITY_CLASSES;
  }

  public static Optional<RequestMethod> getByName(String name) {
    return REQUEST_METHODS.stream()
            .filter(requestMethod -> requestMethod.getMethodName().equals(name))
            .findFirst();
  }
}
