package de.codepitbull.maven.multijvm.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class TestVerticle extends AbstractVerticle{
  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.setPeriodic(100l, p -> {
      vertx.eventBus().send("sync", TestVerticle.this.hashCode());
    });
  }
}
