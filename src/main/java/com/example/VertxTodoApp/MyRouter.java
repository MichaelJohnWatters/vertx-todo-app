package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class MyRouter {

  private final Router vertxRouter;
  private final Vertx vertx;

  public MyRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    // Get Methods
    router.get("/todo/:todo_id").handler(this::getSingleTodoHandler);
    router.get("/todos").handler(this::getAllTodos);

    this.vertxRouter = router;
    this.vertx = vertx;
  }

  public Router getVertxRouter() {
    return vertxRouter;
  }

  private void getSingleTodoHandler(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");
    String todoId = context.pathParam("todo_id"); // possible null.

    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.GET_SINGLE_TODO, todoId, reply -> {
      if(reply.succeeded()) {

        EventBusMessageReply replyObject = reply.result().body();

        if (replyObject.getError()){
          context.response().setStatusCode(replyObject.getStatusCode());
          context.request().response().end(replyObject.getMessageReplyJsonObject().encode());
        } else {
          context.request().response().end(
            replyObject.getMessageReplyJsonObject().encode()
          );
        }
      } else {
        context.response().setStatusCode(500);
        context.request().response().end("Internal Server Error: failed to send request to EventBus");
      }
    });
  }

  private void getAllTodos(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");
    String todoId = context.pathParam("todo_id"); // possible null.

    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.GET_ALL_TODOS, "", reply -> {

      if(reply.succeeded()) {

        EventBusMessageReply replyObject = reply.result().body();

        if (replyObject.getError()){
          System.out.println("getError");
          context.response().setStatusCode(replyObject.getStatusCode());
          context.request().response().end(replyObject.getMessageReplyJsonObject().encode());
        } else {
          System.out.println("seems good");
          context.request().response().end(
            replyObject.getMessageReplyJsonObject().encode()
          );
        }
      } else {
        System.out.println("500 oh dear");
        context.response().setStatusCode(500);
        context.request().response().end("Internal Server Error");
      }
    });
  }
}
