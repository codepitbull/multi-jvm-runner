package de.codepitbull.multijvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class InputStreamProcessor {
  private InputStream inputStream;
  private List<Consumer<String>> consumerList = new CopyOnWriteArrayList<>();
  private Thread thread;
  private volatile boolean stop = false;

  public InputStreamProcessor(InputStream inputStream) {
    this.inputStream = inputStream;
    thread = new Thread() {
      @Override
      public void run() {
        try {
          try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while (((line = br.readLine()) != null) && !stop) {
              for (Consumer<String> c : consumerList) {
                c.accept(line);
              }
            }
          }
        } catch (IOException ioe) {
          throw new RuntimeException(ioe);
        }
      }
    };
  }

  public InputStreamProcessor addConsumer(Consumer<String> consumer) {
    consumerList.add(consumer);
    return this;
  }

  public InputStreamProcessor removeConsumer(Consumer<String> consumer) {
    consumerList.remove(consumer);
    return this;
  }

  public InputStreamProcessor start() {
    thread.start();
    return this;
  }

  public InputStreamProcessor stop() {
    stop = true;
    thread.interrupt();
    return this;
  }
}
