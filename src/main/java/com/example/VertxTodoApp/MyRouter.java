package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageReply;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging.EventBusMessageRequest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class MyRouter {

  private final Router vertxRouter;
  private final Vertx vertx;

  public MyRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    // Get Methods
    router.get("/todo/:todo_id").handler(this::getSingleTodoHandler);
    router.get("/todos").handler(this::getAllTodos);
    router.delete("/todo_delete/:todo_id").handler(this::deleteSingleTodoHandler);

    // Post methods
    router.route().handler(BodyHandler.create());
    router.post("/todo").handler(this::createTodo);

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


  private void createTodo(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");

    JsonObject body = context.body().asJsonObject();
    String name = body.getString("name");
    String content = body.getString("content");

    // TODO VALIDATION

    EventBusMessageRequest requestMessage = new EventBusMessageRequest(
      user_id, new JsonObject().put("name", name).put("content",content)
    );

    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.CREATE_TODO, requestMessage, reply -> setResponse(reply, context));
  }

  private void getAllTodos(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");

    EventBusMessageRequest requestMessage = new EventBusMessageRequest(
      user_id, new JsonObject()
    );

    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.GET_ALL_TODOS, requestMessage, reply -> setResponse(reply, context));
  }

  private void deleteSingleTodoHandler(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");
    String todoId = context.pathParam("todo_id"); // possible null.
    this.vertx.eventBus().<EventBusMessageReply>request(EventBusAddresses.DELETE_TODO, todoId, reply -> setResponse(reply, context));
  }

  private void setResponse(AsyncResult<Message<EventBusMessageReply>> reply, RoutingContext context){
    context.response().headers().add("Content-Type", "application/json");
    if(reply.succeeded()) {

      // Extract the message from the Eventbus
      EventBusMessageReply replyObject = reply.result().body();

      // Set the response body.
      context.request().response().end(replyObject.getMessageReplyJsonObject().encode());
      context.response().setStatusCode(replyObject.getStatusCode());

    } else {
      context.response().setStatusCode(500);
      context.request().response().end("Internal Server Error: failed to send request to EventBus");
    }
  }
}
