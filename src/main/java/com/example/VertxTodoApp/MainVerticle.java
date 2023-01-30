package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumerVerticles.TodoConsumerVerticle;
import io.vertx.core.*;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> start) {


//    Promise<String> dbVerticleDeployment = Promise.promise();  // <1>
//    vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment);  // <2>

    deployAllVerticles(start);

//      .onComplete(ar -> {   // <7>
//      if (ar.succeeded()) {
//        start.complete();
//      } else {
//        start.fail(ar.cause());
//      }
//    });
  }

  CompositeFuture deployAllVerticles(Promise<Void> start){
    return CompositeFuture.all(
      vertx.deployVerticle(new HttpServerVerticle()),
      vertx.deployVerticle(new TodoConsumerVerticle())
    ).onComplete(ar -> {
      if (ar.succeeded()) {
        // All servers startedz
        start.complete();
      } else {
        // At least one server failed
        start.fail(ar.cause());
      }
    });
  }
}
