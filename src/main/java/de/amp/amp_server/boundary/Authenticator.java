package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.AuthenticationResult;
import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.datasource.entity.User;
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

  public static AuthenticationResult validRequest(final Request request) {

    if (timestampTooOld(request)) {
      return fail();
    }

    User user = findUser(request);
    if (user == null) {
      LOGGER.log(Level.INFO, "user not found for {0}", request.getUser());
      return fail();
    }

    if (hashInvalid(user, request)) {
      LOGGER.log(Level.INFO, "password failed for {0}", request.getUser());
      return fail();
    }

    updateLastLogin(user, request);

    return new AuthenticationResult(true, user);
  }

  private static AuthenticationResult fail() {
    return new AuthenticationResult(false, null);
  }

  private static void updateLastLogin(final User user, final Request request) {
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

  private static boolean hashInvalid(User user, Request request) {

    final StringBuilder toHash = new StringBuilder();
    toHash.append(SALT);
    toHash.append(user.getName());
    toHash.append(request.getTimestamp());
    toHash.append(request.getMethod());
    toHash.append(request.getPayload());
    toHash.append(user.getPassword());

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
