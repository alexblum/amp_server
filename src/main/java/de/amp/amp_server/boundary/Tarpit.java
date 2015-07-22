package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.boundary.bean.TarpitBean;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;

public final class Tarpit {

  static final ConcurrentHashMap<String, TarpitBean> CACHE = new ConcurrentHashMap<>();
  //
  private static final Duration MINIMUM_REQUEST_DELAY = Duration.ofMillis(10000);
  private static final Duration REQUEST_DENIAL_DURATION = Duration.ofMinutes(1);
  private static final int MAXIMUM_FAILED_ATTEMPTS = 3;

  private Tarpit() {
  }

  public static Response validateRequest(HttpServletRequest httpServletRequest) {
    String key = getKey(httpServletRequest);

    TarpitBean tarpitBean = CACHE.get(key);
    if (tarpitBean == null) {
      CACHE.put(key, new TarpitBean());
      return null;
    }

    Instant lastRequest = tarpitBean.getLastRequest();
    Instant now = Instant.now();
    tarpitBean.setLastRequest(now);

    Instant requestsDeniedSince = tarpitBean.getRequestsDeniedSince();
    if (requestsDeniedSince != null) {
      Duration elapsedDenialTime = Duration.between(requestsDeniedSince, now);
      Duration waitFor = elapsedDenialTime.minus(REQUEST_DENIAL_DURATION);
      if (waitFor.isNegative()) {
        return new Response("error", "your requests are denied for " + REQUEST_DENIAL_DURATION.getSeconds() + "s (please wait " + -waitFor.getSeconds() + "s)");
      } else {
        tarpitBean.setRequestsDeniedSince(null);
        tarpitBean.setFailedRequestCount(0);
      }
    }

    Duration delay = Duration.between(lastRequest, now);
    if (delay.minus(MINIMUM_REQUEST_DELAY).isNegative()) {
      return new Response("error", "last request younger than " + MINIMUM_REQUEST_DELAY.toMillis() + "ms (delay was " + delay.toMillis() + "ms)");
    }

    return null;
  }

  static void registerSuccessfulRequest(HttpServletRequest httpServletRequest) {
    String key = getKey(httpServletRequest);
    TarpitBean tarpitBean = CACHE.get(key);
    if (tarpitBean == null) {
      tarpitBean = new TarpitBean();
      CACHE.put(key, tarpitBean);
    }
    tarpitBean.setFailedRequestCount(0);
  }

  static void registerDeniedRequest(HttpServletRequest httpServletRequest) {
    String key = getKey(httpServletRequest);
    TarpitBean tarpitBean = CACHE.get(key);
    if (tarpitBean == null) {
      tarpitBean = new TarpitBean();
      CACHE.put(key, tarpitBean);
    }
    tarpitBean.setFailedRequestCount(tarpitBean.getFailedRequestCount() + 1);

    if (tarpitBean.getFailedRequestCount() >= MAXIMUM_FAILED_ATTEMPTS) {
      tarpitBean.setRequestsDeniedSince(Instant.now());
      tarpitBean.setFailedRequestCount(0);
    }
  }

  private static String getKey(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getRemoteAddr();
  }
}
