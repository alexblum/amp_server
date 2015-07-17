package de.amp.amp_server.datasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

public class Datasource {

  private Properties props;
  private ComboPooledDataSource cpds;
  private static Datasource datasource;
  private static Logger log = Logger.getLogger(Datasource.class.getName());

  private Datasource() throws IOException, SQLException {
    // load datasource properties
    log.info("Reading datasource.properties from classpath");
    props = Utils.readProperties("datasource.properties");
    cpds = new ComboPooledDataSource();
    cpds.setJdbcUrl(props.getProperty("jdbcUrl"));
    cpds.setUser(props.getProperty("username"));
    cpds.setPassword(props.getProperty("password"));

    cpds.setInitialPoolSize(new Integer((String) props.getProperty("initialPoolSize")));
    cpds.setAcquireIncrement(new Integer((String) props.getProperty("acquireIncrement")));
    cpds.setMaxPoolSize(new Integer((String) props.getProperty("maxPoolSize")));
    cpds.setMinPoolSize(new Integer((String) props.getProperty("minPoolSize")));
    cpds.setMaxStatements(new Integer((String) props.getProperty("maxStatements")));

    Connection testConnection = null;
    Statement testStatement = null;

    // test connectivity and initialize pool
    try {
      testConnection = cpds.getConnection();
      testStatement = testConnection.createStatement();
      testStatement.executeQuery("select 1+1 from DUAL");
    } catch (SQLException e) {
      throw e;
    } finally {
      testStatement.close();
      testConnection.close();
    }

  }

  public static Datasource getInstance() throws IOException, SQLException {
    if (datasource == null) {
      datasource = new Datasource();
      return datasource;
    } else {
      return datasource;
    }
  }

  public Connection getConnection() throws SQLException {
    return this.cpds.getConnection();
  }
}
