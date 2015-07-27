package de.amp.amp_server.control;

import de.amp.amp_server.control.constants.AppState;
import de.amp.amp_server.datasource.dao.AppStateDAO;

public final class AppStateController {

  private static AppStateController instance;
  //
  private final AppStateDAO appStateDAO;

  private AppStateController() {
    appStateDAO = new AppStateDAO();
  }

  public static AppStateController singleton() {
    if (instance == null) {
      instance = new AppStateController();
    }
    return instance;
  }

  public String getState(AppState appState) {
    if (appState == null) {
      return null;
    }
    String name = normalizeName(appState);
    de.amp.amp_server.datasource.entity.AppState appStateFromDb = appStateDAO.findByName(name);
    if (appStateFromDb == null) {
      return null;
    }
    return appStateFromDb.getValue();
  }

  private String normalizeName(AppState appState) {
    return appState.name().toLowerCase();
  }

  /**
   * updates the specified state and returns the old value
   *
   * @param appState
   * @param value
   * @return the old value, if it existed
   */
  public String updateState(AppState appState, String value) {
    if (appState == null) {
      return null;
    }

    de.amp.amp_server.datasource.entity.AppState dbState = new de.amp.amp_server.datasource.entity.AppState();
    dbState.setName(normalizeName(appState));
    dbState.setValue(value);
    return appStateDAO.update(dbState);
  }

}
