package de.codeptibull.maven.multijvm;

/**
 * Calling the main-emthod in this class results in a RuntimeException.
 */
public class JvmTestFailMain {
  public static void main(String[] args) {
    throw new RuntimeException("I am dead inside.");
  }
}
