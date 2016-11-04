package de.codeptibull.maven.multijvm;

import de.codeptibull.multijvm.InputStreamProcessor;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class InputStreamProcessorTest {

  @Test
  public void testTracingForASearchTerm() throws Exception{
    CountDownLatch cl = new CountDownLatch(3);
    PipedInputStream in = new PipedInputStream();
    PipedOutputStream out = new PipedOutputStream(in);

    Thread tr = new Thread(() -> {
      while(true){
        try {
          out.write("check\n".getBytes());
          Thread.sleep(100);
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          return;
        }
      }
    });

    tr.start();

    new InputStreamProcessor(in).addConsumer(a -> {
      if(a.equals("check"))
        cl.countDown();
    }).start();

    cl.await();

    tr.interrupt();

  }
}
