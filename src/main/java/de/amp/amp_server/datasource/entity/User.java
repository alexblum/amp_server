package de.amp.amp_server.datasource.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class User {

  private int id;
  private String name;
  private String password;
  private String email;
  private Date createDate;
  private Date lastLogin;
  private boolean active;
  private int userGroup;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPasswordMD5() {
    if (password == null) {
      return null;
    }
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(password.getBytes());
      byte[] hash = digest.digest();

      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < hash.length; i++) {
        hexString.append(Integer.toHexString(0xFF & hash[i]));
      }

      return hexString.toString();
    } catch (NoSuchAlgorithmException ex) {
    }
    return password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public int getUserGroup() {
    return userGroup;
  }

  public void setUserGroup(int userGroup) {
    this.userGroup = userGroup;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + this.id;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

}
