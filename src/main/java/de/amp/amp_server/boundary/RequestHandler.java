package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.boundary.request.RequestMethod;
import de.amp.amp_server.boundary.request.RequestMethodRegistry;
import de.amp.amp_server.datasource.entity.User;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

public class RequestHandler {

  private static final Unmarshaller UNMARSHALLER;

  static {
    Map<String, Object> properties = new HashMap<>();
    properties.put("eclipselink.media-type", "application/json");
    properties.put("eclipselink.json.include-root", false);

    try {
      UNMARSHALLER = JAXBContextFactory.createContext(RequestMethodRegistry.getEntityClasses(), properties).createUnmarshaller();
    } catch (JAXBException ex) {
      throw new IllegalStateException(ex);
    }
  }

  Response handle(Request request, User user) {

    Optional<RequestMethod> requestMethodOptional = RequestMethodRegistry.getByName(request.getMethod());

    if (requestMethodOptional.isPresent()) {
      RequestMethod requestMethod = requestMethodOptional.get();

      Object payload = parsePayload(request, requestMethod);

      if (payload == null) {
        return new Response("error", "payload parse error");
      }

      return requestMethod.process(user, payload);
    }
    return new Response("error", "unknown method");
  }

  private Object parsePayload(Request request, RequestMethod requestMethod) {
    String payloadAsString = request.getPayload();

    if (requestMethod.getEntityClass() == String.class) {
      return payloadAsString;
    }

    try {
      try (StringReader reader = new StringReader(payloadAsString)) {
        Source source = new StreamSource(reader);
        return UNMARSHALLER.unmarshal(source, requestMethod.getEntityClass()).getValue();
      }

    } catch (JAXBException ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }
}
