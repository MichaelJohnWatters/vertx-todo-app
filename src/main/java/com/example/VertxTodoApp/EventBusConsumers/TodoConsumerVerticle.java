package com.example.VertxTodoApp.EventBusConsumers;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReplyCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
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
      Future<RowSet<Row>> futureResults = runSqlQuery("SELECT * FROM todos WHERE todo_id = " + Integer.parseInt( extractedTodoId) + " LIMIT 1");

      futureResults.andThen( rowSetAsyncResult -> {
        if(rowSetAsyncResult.succeeded()){
          var rows = rowSetAsyncResult.result();
          if  (rows == null) {
            message.reply(new EventBusMessageReply(true, new JsonObject().put("Not Found", "Not results for todo_id: " + extractedTodoId), 404));
          }
          else {

            Integer todo_id = null;
            String name = null;
            String content = null;

            for (Row row : rows) {
              todo_id = row.getInteger("todo_id");
              name = row.getString("name");
              content = row.getString("content");
            }

            JsonObject json = new JsonObject()
              .put("todo_id", todo_id)
              .put("name", name)
              .put("content", content);

            message.reply(new EventBusMessageReply(false, json, 200));
          }
        } else {
          message.reply(new EventBusMessageReply(true, new JsonObject().put("Internal Server Error", "somthing is dead in " + EventBusAddresses.GET_ALL_TODOS), 500));
        }
      });
    });

    // Get All Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_ALL_TODOS, message -> {

      // Note: Sql injection
      Future<RowSet<Row>> futureResults = runSqlQuery("SELECT * FROM todos WHERE todo_id");

      futureResults.andThen( rowSetAsyncResult -> {
        if(rowSetAsyncResult.succeeded()){
          var rows = rowSetAsyncResult.result();
          if  (rows == null) {
            message.reply(new EventBusMessageReply(true, new JsonObject().put("Not Found", "No results found"), 404));
          }
          else {
            JsonArray json = new JsonArray();
            for (Row row : rows) {
              json.add(
                  new JsonObject()
                  .put("todo_id",  row.getInteger("todo_id"))
                  .put("name", row.getString("name"))
                  .put("content", row.getString("content"))
                );
            }
            message.reply(new EventBusMessageReply(false, new JsonObject().put("results", json), 200));
          }
        } else {
          message.reply(new EventBusMessageReply(true, new JsonObject().put("Internal Server Error", "somthing is dead in " + EventBusAddresses.GET_ALL_TODOS), 500));
        }
      });
    });
  }

  private Future<RowSet<Row>> runSqlQuery(String sql) {
    return pool.getConnection().compose(conn -> conn
      .query(sql)
      .execute()
      .onComplete(ar -> {
        conn.close();
      })).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("Done");
      } else {
        System.out.println("Something went wrong " + ar.cause().getMessage());
      }
    });
  }
}
