package de.amp.amp_server.boundary.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChangeStateEntity {

  private boolean shutdown;

  public boolean isShutdown() {
    return shutdown;
  }

  public void setShutdown(boolean shutdown) {
    this.shutdown = shutdown;
  }
}
