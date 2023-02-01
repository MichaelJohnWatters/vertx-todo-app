package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.TodoConsumerVerticle;
import io.vertx.core.*;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;


public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> start) {
    deployAllVerticles(start);
  }

  void deployAllVerticles(Promise<Void> start){

    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(33061)
      .setHost("localhost")
      .setDatabase("todos")
      .setUser("user")
      .setPassword("password");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

    MySQLPool pool = MySQLPool.pool(vertx, connectOptions, poolOptions);

    CompositeFuture.all(
      vertx.deployVerticle(new HttpServerVerticle()),
      vertx.deployVerticle(new TodoConsumerVerticle(pool))
    ).onComplete(compositeFutureResult -> {
      if (compositeFutureResult.succeeded()) {
        start.complete();
      } else {
        // At least one server failed
        start.fail(compositeFutureResult.cause());
        System.out.println("somthing is dead");
        System.exit(0);
      }
    });
  }
}
