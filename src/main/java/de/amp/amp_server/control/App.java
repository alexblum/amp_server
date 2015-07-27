package de.amp.amp_server.control;

import de.amp.amp_server.control.constants.AppState;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class App {

  private static App instance;
  //
  private boolean running;
  private Instant lastTick;
  private Duration elapsedSinceLastTick;
  //
  private static final Duration TICK_EVERY = Duration.ofSeconds(5);
  //
  private static final Object LOCK = new Object();

  private App() {
    running = true;
    String lastTickFromDb = AppStateController.singleton().getState(AppState.LAST_TICK);
    if (lastTickFromDb == null) {
      lastTick = Instant.now();
    } else {
      lastTick = Instant.ofEpochMilli(Long.parseLong(lastTickFromDb));
    }
  }

  public static void init() {
    synchronized (LOCK) {
      if (instance != null) {
        throw new IllegalStateException("app already initialized");
      }
      instance = new App();
    }
  }

  public static App getSingleton() {
    if (instance == null) {
      throw new IllegalStateException("app not initialized");
    }
    return instance;
  }

  public void stop() {
    synchronized (LOCK) {
      if (instance == null) {
        throw new IllegalStateException("app already initialized");
      }
      instance = null;
    }
  }

  public void interruptMainLoop() {
    running = false;
  }

  public void enterMainLoop() {
    mainLoop();
  }

  private void mainLoop() {
    while (running) {
      Instant tickStart = Instant.now();
      elapsedSinceLastTick = Duration.between(lastTick, tickStart);
      lastTick = tickStart;

      AppStateController.singleton().updateState(AppState.LAST_TICK, Long.toString(tickStart.toEpochMilli()));
      sleepForRestOfTick(tickStart);
    }
  }

  private void sleepForRestOfTick(Instant tickStart) {
    Duration tickDuration = Duration.between(tickStart, Instant.now());
    long sleepTime = TICK_EVERY.minus(tickDuration).toMillis();
    sleep(sleepTime);
  }

  private void sleep(long sleepTime) {
    try {
      TimeUnit.MILLISECONDS.sleep(sleepTime);
    } catch (InterruptedException ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    }
  }
}
