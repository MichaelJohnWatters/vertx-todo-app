package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
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
    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.GET_SINGLE_TODO, todoId, reply -> setResponse(reply, context));
  }

  private void getAllTodos(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");
    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.GET_ALL_TODOS, "", reply -> setResponse(reply, context));
  }

  private void setResponse(AsyncResult<Message<EventBusMessageReply>> reply, RoutingContext context){
    if(reply.succeeded()) {

      // Extract the message from the Eventbus
      EventBusMessageReply replyObject = reply.result().body();

      // Set the response body.
      context.request().response().end(replyObject.getMessageReplyJsonObject().encode());

      // Set status code if was error
      if (replyObject.getError()){
        context.response().setStatusCode(replyObject.getStatusCode());
      }

    } else {
      context.response().setStatusCode(500);
      context.request().response().end("Internal Server Error: failed to send request to EventBus");
    }
  }
}
