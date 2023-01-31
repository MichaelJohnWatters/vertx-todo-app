package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumerVerticles.TodoConsumerVerticle;
import io.vertx.core.*;

/**
 * Currently the main verticle only performs a deployment of
 * all the applications verticles
 * each verticle will handle its own setup, its self.
 */
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> start) {
    deployAllVerticles(start);
  }

  void deployAllVerticles(Promise<Void> start){
    CompositeFuture.all(
      vertx.deployVerticle(new HttpServerVerticle()),
      vertx.deployVerticle(new TodoConsumerVerticle())
    ).onComplete(compositeFutureResult -> {
      if (compositeFutureResult.succeeded()) {
        start.complete();
      } else {
        // At least one server failed
        start.fail(compositeFutureResult.cause());
      }
    });
  }
}
