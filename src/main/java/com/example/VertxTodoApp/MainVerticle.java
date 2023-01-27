package com.example.VertxTodoApp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(){

    // Create a Router
    Router router = Router.router(vertx);

    // Endpoint 1
    router.get("/api/v1/todo").handler( ctx -> {
      ctx.request().response().end("Hello from the /api/v1/hello endpoint!");
    });

    // Endpoint 2 with param (add /:name to route)
    router.get("/api/v1/todo/:name").handler( ctx -> {
      String name = ctx.pathParam("name");
      ctx.request().response().end(String.format("Hello from the /api/v1/hello with params: %s!", name));
    });

    // Endpoint 3 todos
    router.get("/api/v1/todo").handler( ctx -> {
      ctx.request().response().end("Hello from the /api/v1/hello endpoint!");
    });

    // Create the Http server, but set the servers hanlder to the router
    vertx.createHttpServer().requestHandler(router).listen(8080);
  }
}
