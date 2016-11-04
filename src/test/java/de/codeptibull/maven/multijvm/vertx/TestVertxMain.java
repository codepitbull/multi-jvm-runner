package de.codeptibull.maven.multijvm.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class TestVertxMain {
  public static void main(String[] args) {
    VertxOptions options = new VertxOptions();
    Vertx.clusteredVertx(options, res -> {
      if (res.succeeded()) {
        Vertx vertx = res.result();
        vertx.deployVerticle(TestVerticle.class.getName());
        System.out.println("Started");
      } else {
        System.out.println("Failed: " + res.cause());
      }
    });
  }
}
