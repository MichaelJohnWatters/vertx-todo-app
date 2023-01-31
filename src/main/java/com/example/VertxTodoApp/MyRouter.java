package com.example.VertxTodoApp;

import com.example.VertxTodoApp.EventBusConsumers.EventBusAddresses;
import com.example.VertxTodoApp.EventBusConsumers.EventBusMessageReply;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class MyRouter {

  private final Router vertxRouter;
  private final Vertx vertx;

  public MyRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    // Post Methods.
    //    router.post().handler(BodyHandler.create());

    // Get Methods
    router.get("/todo/:todo_id").handler(this::getSingleTodoHandler);

    this.vertxRouter = router;
    this.vertx = vertx;
  }

  public Router getVertxRouter() {
    return vertxRouter;
  }

//  private void createTodoHandler(RoutingContext context) {
//    String user_id = context.request().getHeader("Authorization");
//    System.out.println(user_id);
//    JsonObject body = context.body().asJsonObject();
//    String name = body.getString("name");
//    String content = body.getString("content");
//
//    JsonArray errors = new JsonArray();
//
//    if (name == null || name.isEmpty() || name.isBlank()){
//      context.response().setStatusCode(422);
//      context.response().putHeader("Content-Type", "application/json");
//      errors.add(new JsonObject().put("name", "cannot be missing, null or empty."));
//      context.response().setChunked(true).write(errors.encode());
//      context.response().end();
//    }
//
//    if (content == null || content.isEmpty() || content.isBlank()){
//      context.response().setStatusCode(422);
//      context.response().putHeader("Content-Type", "application/json");
//      errors.add(new JsonObject().put("content", "cannot be missing, null or empty."));
//      context.response().setChunked(true).write(errors.encode());
//      context.response().end();
//    }
//
//    context.response().setStatusCode(201);
//    context.response().end();
//  }

  private void getSingleTodoHandler(RoutingContext context) {
    String user_id = context.request().getHeader("Authorization");
    String todoId = context.pathParam("todo_id"); // possible null.

    this.vertx.eventBus().request(EventBusAddresses.GET_SINGLE_TODO, todoId, reply -> {

      if(reply.succeeded()) {

        EventBusMessageReply replyObject = (EventBusMessageReply) reply.result();

        if (replyObject.getError()){
          context.response().setStatusCode(replyObject.getStatusCode());
          context.request().response().end(replyObject.getMessageReplyJsonObject().encode());
        }

        context.request().response().end(
          replyObject.getMessageReplyJsonObject().encode()
        );

      } else {
        context.response().setStatusCode(500);
        context.request().response().end("Internal Server Error");
      }
    });
  }
}
