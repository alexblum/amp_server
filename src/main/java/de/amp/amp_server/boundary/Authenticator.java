package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.control.helper.HashHelper;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Authenticator {

  private static final String SALT = "amp_server_rocks_";
  private static final Duration REQUEST_DEVIATION = Duration.ofMinutes(5L);
  private static final Map<String, String> AUTH = new HashMap<>();

  static {
    AUTH.put("user", "user");
    AUTH.put("admin", "admin");
  }

  public static boolean validRequest(final Request request) {
    if (timestampTooOld(request)) {
      return false;
    }
    if (hashInvalid(request)) {
      return false;
    }
    return true;
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
    final String user = request.getUser();
    final String passphrase = findPassphraseForUser(user);
    if (passphrase == null) {
      return true;
    }
    long timestamp = request.getTimestamp();

    final StringBuilder toHash = new StringBuilder();

    toHash.append(SALT);
    toHash.append(user);
    toHash.append(timestamp);
    toHash.append(passphrase);
    //TODO: append more request data

    try {
      return !HashHelper.hash(toHash.toString()).equals(request.getHash());
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
      Logger.getLogger(Authenticator.class.getName()).log(Level.SEVERE, null, ex);
    }
    return true;
  }

  private static String findPassphraseForUser(String user) {
    return AUTH.get(user);
  }
}
