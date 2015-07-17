package de.amp.amp_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.Instant;

public class Server_old {

  private static final Charset CHARSET = Charset.forName("utf8");
  private static final String CONTENT_SEPARATOR = "\r\n\r\n";

  public void listen() throws IOException {
    System.out.println("starting server");
    final ServerSocket serverSocket = new ServerSocket(1337);
//    while (true) {
    final Socket socket = serverSocket.accept();
    handleSocket(socket);
//    }
  }

  private void handleSocket(final Socket socket) throws IOException {
    System.out.println("handling socket from: " + socket.getRemoteSocketAddress().toString());
    try {
      try (final InputStream inputStream = socket.getInputStream()) {
        try (final OutputStream outputStream = socket.getOutputStream()) {
          StringBuilder requestContent = readInput(inputStream);

          StringBuilder responseContent = handleRequest(requestContent);

          StringBuilder response = buildResponse(responseContent);
          outputStream.write(response.toString().getBytes());
        }
      }
    } finally {
      socket.close();
      System.out.println("socket closed");
    }
  }

  private StringBuilder buildResponse(StringBuilder responseContent) {
    final String nowString = Instant.now().toString();
    final StringBuilder response = new StringBuilder();
    response.append("HTTP/1.0 200 OK\r\n");
    response.append("Date: ").append(nowString).append("\r\n");
    response.append("Server: amp_server/0.1\r\n");
    response.append("Content-Type: text/html\r\n");
    response.append("Content-Length: ").append(responseContent.length()).append("\r\n");
    response.append("Expires: ").append(nowString).append("\r\n");
    response.append("Last-modified: ").append(nowString).append("\r\n");
    response.append("\r\n");
    response.append(responseContent);
    return response;
  }

  private StringBuilder handleRequest(StringBuilder requestContent) {
    System.out.println("building response");
    final StringBuilder responseContent = new StringBuilder();
//    responseContent.append(new RestHandler().handleRequest(requestContent.toString()));
    return responseContent;
  }

  private StringBuilder readInput(final InputStream inputStream) throws IOException {
    final byte[] buffer = new byte[1024 * 1024];
    final StringBuilder request = new StringBuilder();
    while (inputStream.available() > 0) {
      final int bytesRead = inputStream.read(buffer);
      request.append(new String(buffer, 0, bytesRead, CHARSET));
    }
    StringBuilder requestContent = new StringBuilder();
    final int positionOfContentSeparator = request.indexOf(CONTENT_SEPARATOR);
    if (positionOfContentSeparator > 0) {
      requestContent.append(request.substring(positionOfContentSeparator + CONTENT_SEPARATOR.length()));
    }
    return requestContent;
  }
}
