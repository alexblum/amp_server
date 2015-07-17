package de.amp.amp_server.datasource.dao;

import de.amp.amp_server.control.bean.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends AbstractDAO {

  public User findByName(String name) {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet resultSet = null;

    try {
      connection = getConnection();
      ps = connection.prepareStatement("SELECT * FROM user WHERE active = TRUE AND name = ? LIMIT 1");
      ps.setString(1, name);

      resultSet = ps.executeQuery();
      if (resultSet.next()) {
        return map(resultSet);
      }

    } catch (Exception ex) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
    } finally {
      try {
        resultSet.close();
        ps.close();
        connection.close();
      } catch (Exception ex) {
      }
    }

    return null;
  }

  private User map(ResultSet resultSet) throws SQLException {
    User u = new User();
    u.setId(resultSet.getInt("id"));
    u.setActive(resultSet.getBoolean("active"));
    u.setCreateDate(resultSet.getDate("createDate"));
    u.setEmail(resultSet.getString("email"));
    u.setLastLogin(resultSet.getDate("lastLogin"));
    u.setName(resultSet.getString("name"));
    u.setPassword(resultSet.getString("password"));
    return u;
  }
}
