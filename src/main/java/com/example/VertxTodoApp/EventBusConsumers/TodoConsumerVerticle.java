package com.example.VertxTodoApp.EventBusConsumers;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReplyCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.concurrent.atomic.AtomicReference;


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
      String extractedTodoId = (String) message.body();

      if (extractedTodoId == null){
        message.reply(new EventBusMessageReply(true, new JsonObject().put("Invalid Param", "todo_id is null"), 422));
      }

      // Note: Sql injection
      RowSet<Row> results = runSqlQuery("SELECT * FROM todos WHERE todo_id = " + Integer.parseInt( extractedTodoId));

      if (results == null){
        message.reply(new EventBusMessageReply(true, new JsonObject().put("Not Found", "Not results for todo_id: " + extractedTodoId), 404));
      } else {
        System.out.println(results.columnsNames());

        message.reply(
          new EventBusMessageReply(false, new JsonObject().put("todo_id", 1).put("name", 1).put("content", 1)
            , 200)
        );
      }
    });

    // Get All Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {
      // TODO database Verticle
      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "yeoo"), 200));
    });
  }

  RowSet<Row> runSqlQuery(String sql) {


    pool.getConnection().compose(conn -> {
      System.out.println("Got a connection from the pool");
      return conn
        .query(sql)
        .execute()
        .onComplete(ar -> {
          // Release the connection to the pool
          conn.close();
        });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        RowSet<Row> result = ar.result();
        System.out.println("Done");
      } else {
        System.out.println("Something went wrong " + ar.cause().getMessage());
      }
    });


    return null;
  }
}
