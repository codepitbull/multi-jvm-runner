package de.codeptibull.maven.multijvm;

/**
 * The main method in this class will enter an endless loop to print "TICK".
 */
public class JvmTestOutputMain {
  public static void main(String[] args) {
    while (true) {
      System.out.println("TICK");
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
