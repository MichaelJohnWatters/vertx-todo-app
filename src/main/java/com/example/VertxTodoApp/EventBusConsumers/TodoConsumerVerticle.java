package com.example.VertxTodoApp.EventBusConsumers;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReplyCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;


public class TodoConsumerVerticle extends AbstractVerticle {

  private final MySQLPool pool;

  public TodoConsumerVerticle(MySQLPool pool){
    this.pool = pool;
  }

  @Override
  public void start() {

    // Register Codecs
    vertx.eventBus().registerDefaultCodec(EventBusMessageReply.class, new EventBusMessageReplyCodec());


    // Get Single Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {
      // TODO database Verticle
      String extractedTodoId = (String) message.body();

      runSqlQuery("");

      if (extractedTodoId == null){
        message.reply(new EventBusMessageReply(true, new JsonObject().put("error", "oh no todo_id is null"), 422));
      }

      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "yeoo"), 200));
    });

    // Get All Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {
      // TODO database Verticle
      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "yeoo"), 200));
    });
  }

  void runSqlQuery(String sql) {
    // Get a connection from the pool
    pool.getConnection().compose(conn -> {
      System.out.println("Got a connection from the pool");

      // All operations execute on the same connection
      return conn
        .query(sql)
        .execute()
        .onComplete(ar -> {
          // Release the connection to the pool
          conn.close();
        });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Done");
      } else {
        System.out.println("Something went wrong " + ar.cause().getMessage());
      }
    });
  }
}
