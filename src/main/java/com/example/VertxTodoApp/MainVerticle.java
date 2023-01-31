package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.TodoConsumerVerticle;
import io.vertx.core.*;


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
