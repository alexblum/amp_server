package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.control.bean.User;
import de.amp.amp_server.control.helper.HashHelper;
import de.amp.amp_server.datasource.dao.UserDAO;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authenticator {

  private static final String SALT = "amp_server_rocks_";
  private static final Duration REQUEST_DEVIATION = Duration.ofMinutes(5L);
  private static final Logger LOGGER = Logger.getLogger(Authenticator.class.getName());

  public static boolean validRequest(final Request request) {
    if (timestampTooOld(request)) {
      return false;
    }
    if (hashInvalid(request)) {
      LOGGER.log(Level.INFO, "password failed for {0}", request.getUser());
      return false;
    }

    updateLastLogin(request);

    return true;
  }

  private static void updateLastLogin(final Request request) {
    User user = findUser(request);
    user.setLastLogin(new Date());
    UserDAO userDAO = new UserDAO();
    userDAO.update(user);
  }

  private static boolean timestampTooOld(final Request request) {
    final long timestamp = request.getTimestamp();
    final Instant requestTimestamp = Instant.ofEpochMilli(timestamp);
    if (requestTimestamp.plus(REQUEST_DEVIATION).isBefore(Instant.now())) {
      return true;
    }
    return false;
  }

  private static boolean hashInvalid(Request request) {
    User user = findUser(request);
    if (user == null) {
      LOGGER.log(Level.INFO, "user not found for {0}", request.getUser());
      return true;
    }

    final StringBuilder toHash = new StringBuilder();
    toHash.append(SALT);
    toHash.append(user.getName());
    toHash.append(request.getTimestamp());
    toHash.append(user.getPassword());
    //TODO: append more request data

    try {
      System.out.println(HashHelper.hash(toHash.toString()));
      return !HashHelper.hash(toHash.toString()).equals(request.getHash());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
      LOGGER.log(Level.WARNING, null, ex);
    }
    return true;
  }

  private static User findUser(Request request) {
    UserDAO userDAO = new UserDAO();
    User userBean = userDAO.findByName(request.getUser());
    if (userBean == null) {
      return null;
    }

    return userBean;
  }
}
