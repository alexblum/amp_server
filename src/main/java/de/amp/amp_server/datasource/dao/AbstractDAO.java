package de.amp.amp_server.datasource.dao;

import de.amp.amp_server.datasource.Datasource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractDAO {

  public Connection getConnection() throws IOException, SQLException {
    Datasource instance = Datasource.getInstance();
    return instance.getConnection();
  }

}
