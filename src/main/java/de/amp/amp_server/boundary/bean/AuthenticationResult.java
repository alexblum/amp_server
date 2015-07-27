package de.amp.amp_server.boundary.bean;

import de.amp.amp_server.datasource.entity.User;

public class AuthenticationResult {

  private boolean authenticated;
  private User user;

  public AuthenticationResult(boolean authenticated, User user) {
    this.authenticated = authenticated;
    this.user = user;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public User getUser() {
    return user;
  }
}
