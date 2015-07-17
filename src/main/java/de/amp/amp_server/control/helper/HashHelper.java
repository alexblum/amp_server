package de.amp.amp_server.control.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashHelper {

  private HashHelper() {
  }

  public static String hash(final String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    final MessageDigest md = MessageDigest.getInstance("SHA-256");

    final byte[] buffi = md.digest(str.getBytes("UTF-8"));
    final StringBuffer hexString = new StringBuffer();

    for (final byte buf : buffi) {
      hexString.append(Integer.toHexString(0xFF & buf));
    }
    return hexString.toString();
  }
}
