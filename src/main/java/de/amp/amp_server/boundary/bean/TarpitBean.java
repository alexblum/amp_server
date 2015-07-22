package de.amp.amp_server.boundary.bean;

import java.time.Instant;

public class TarpitBean {

  private Instant lastRequest;
  private int failedRequestCount;
  private Instant requestsDeniedSince;

  public TarpitBean() {
    lastRequest = Instant.now();
  }

  public Instant getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(Instant lastRequest) {
    this.lastRequest = lastRequest;
  }

  public int getFailedRequestCount() {
    return failedRequestCount;
  }

  public void setFailedRequestCount(int failedRequestCount) {
    this.failedRequestCount = failedRequestCount;
  }

  public Instant getRequestsDeniedSince() {
    return requestsDeniedSince;
  }

  public void setRequestsDeniedSince(Instant requestsDeniedSince) {
    this.requestsDeniedSince = requestsDeniedSince;
  }
}
