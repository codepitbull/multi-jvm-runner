package de.codepitbull.maven.multijvm;

import de.codepitbull.multijvm.InputStreamProcessor;
import de.codepitbull.multijvm.JvmProcess;
import org.junit.Test;


import java.util.concurrent.CountDownLatch;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class JvmProcessTest {

  public static final String STARTED = "STARTED";
  public static final String FAILED = "Exception in thread \"main\" java.lang.RuntimeException: I am dead inside.";

  @Test
  public void startOneJvm() {
    JvmProcess proc = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestMain.class.getName()).build();

    proc.waitFor();
    assertThat(proc.collectStandardOutput(), hasItem(STARTED));
  }

  @Test
  public void startOneJvmAndFail() {
    JvmProcess proc = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestFailMain.class.getName()).build();

    proc.waitFor();
    assertThat(proc.exitValue(), is(not(0)));
    assertThat(proc.collectErrorOutput(), hasItem(FAILED));
  }

  @Test
  public void startTwoJvms() {
    JvmProcess proc1 = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestMain.class.getName()).build();
    JvmProcess proc2 = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestMain.class.getName()).build();

    proc2.waitFor();
    assertThat(proc2.exitValue(), is(0));
    assertThat(proc2.collectStandardOutput(), hasItem(STARTED));

    proc1.waitFor();
    assertThat(proc1.exitValue(), is(0));
    assertThat(proc1.collectStandardOutput(), hasItem(STARTED));
  }

  @Test
  public void startTwoJvmsAndLetOneFail() {
    JvmProcess proc1 = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestMain.class.getName()).build();
    JvmProcess proc2 = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestFailMain.class.getName()).build();

    proc1.waitFor();
    assertThat(proc1.exitValue(), is(0));
    assertThat(proc1.collectStandardOutput(), hasItem(STARTED));

    proc2.waitFor();
    assertThat(proc2.exitValue(), is(not(0)));
    assertThat(proc2.collectErrorOutput(), hasItem(FAILED));
  }

  @Test
  public void startOneJvmAndTraceOutput() throws Exception{
    JvmProcess proc1 = JvmProcess.builder().addCurrentClasspath().mainClass(JvmTestOutputMain.class.getName()).build();
    InputStreamProcessor processor = proc1.stdOutProcessor();
    CountDownLatch cl = new CountDownLatch(3);
    processor.addConsumer(s -> {
      if(s.startsWith("TICK"))
        cl.countDown();
    });
    cl.await();
    proc1.destroy();
  }
}
