package de.codeptibull.maven.multijvm.vertx;

import de.codeptibull.multijvm.JvmProcess;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class VertxTest {
  @Test
  public void testVertxCluster() throws Exception{
    JvmProcess proc1 = JvmProcess.builder().addCurrentClasspath().mainClass(TestVertxMain.class.getName()).build();
    JvmProcess proc2 = JvmProcess.builder().addCurrentClasspath().mainClass(TestVertxMain.class.getName()).build();

    CountDownLatch cl = new CountDownLatch(1);
    VertxOptions options = new VertxOptions();

    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        Set<Integer> ints = new HashSet<>();
        vertx.eventBus().<Integer>consumer("sync", s -> {
          ints.add(s.body());
          if(ints.size() == 2)
            cl.countDown(); //wait until 2 JVMs sent a message, then exit
        });
      } else {
        cl.countDown();
      }
    });
    cl.await();
    //donÄt forget to clean up
    proc1.destroy();
    proc2.destroy();
  }
}
