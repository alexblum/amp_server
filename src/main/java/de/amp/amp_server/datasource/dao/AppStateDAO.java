package de.amp.amp_server.datasource.dao;

import de.amp.amp_server.datasource.entity.AppState;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppStateDAO extends AbstractDAO {

  public AppState findByName(String name) {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet resultSet = null;

    try {
      connection = getConnection();
      ps = connection.prepareStatement("SELECT * FROM app_state WHERE name = ? LIMIT 1");
      ps.setString(1, name);

      resultSet = ps.executeQuery();
      if (resultSet.next()) {
        AppState appState = new AppState();
        appState.setName(resultSet.getString("name"));
        appState.setValue(resultSet.getString("value"));
        return appState;
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

  /**
   *
   * @param appState
   * @return the old value, if it existed
   */
  public String update(AppState appState) {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet resultSet = null;
    PreparedStatement insertOrUpdateStatement = null;

    try {
      connection = getConnection();
      ps = connection.prepareStatement("SELECT * FROM app_state WHERE name = ?");
      ps.setString(1, appState.getName());

      String oldValue = null;
      boolean create = true;
      resultSet = ps.executeQuery();
      if (resultSet.next()) {
        create = false;
        oldValue = resultSet.getString("value");
      }

      if (create) {
        insertOrUpdateStatement = connection.prepareStatement("INSERT INTO app_state VALUES (?,?)");
        insertOrUpdateStatement.setString(1, appState.getName());
        insertOrUpdateStatement.setString(2, appState.getValue());
      } else {
        insertOrUpdateStatement = connection.prepareStatement("UPDATE app_state SET value = ? WHERE name = ?");
        insertOrUpdateStatement.setString(2, appState.getName());
        insertOrUpdateStatement.setString(1, appState.getValue());
      }

      insertOrUpdateStatement.executeUpdate();

      return oldValue;

    } catch (Exception ex) {
      Logger.getLogger(getClass().getName()).log(Level.WARNING, null, ex);
    } finally {
      try {
        resultSet.close();
        insertOrUpdateStatement.close();
        ps.close();
        connection.close();
      } catch (Exception ex) {
      }
    }

    return null;
  }
}
