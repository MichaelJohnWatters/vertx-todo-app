package com.example.VertxTodoApp;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class MyRouter {

  private final Router vertxRouter;

  public MyRouter(Vertx vertx) {

    Router router = Router.router(vertx);

    // Get Methods
    router.get("/todo/:todo_id").handler(this::getSingleTodoHandler);
    router.get("/todos").handler(this::getAllTodosHandler);

    // Post Methods.
    router.post().handler(BodyHandler.create());
    router.post("/todo").handler(this::createTodoHandler);

    this.vertxRouter = router;
  }

  public Router getVertxRouter() {
    return vertxRouter;
  }

  private void createTodoHandler(RoutingContext context) {
//    String user_id = context.request().getHeader("bearer");
    JsonObject body = context.body().asJsonObject();
    String name = body.getString("name");
    String content = body.getString("content");

    // Todo user is authorised check.

    if (name.isEmpty() || name.isBlank()){
      context.response().setStatusCode(422);
      context.response().setStatusMessage("Todo name cannot be blank or empty");
      context.response().end();
    }

    if (content.isEmpty() || content.isBlank()){
      context.response().setStatusCode(422);
      context.response().setStatusMessage("Todo content cannot be blank or empty");
      context.response().end();
    }

    context.response().setStatusCode(201);
    context.response().end();
  }

  private void getSingleTodoHandler(RoutingContext context) {
    // Todo user is authorised check.
    context.response().setStatusCode(200);
    context.response().end();
  }
  private void getAllTodosHandler(RoutingContext context) {
    // Todo user is authorised check.
    context.response().setStatusCode(200);
    context.response().end();
  }
}
