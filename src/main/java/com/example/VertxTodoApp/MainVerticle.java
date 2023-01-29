package com.example.VertxTodoApp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(){

    // Create a Router
    Router router = Router.router(vertx);

    // Separate out, webserver, router, endpointService etc.

    // User endpoints
    // create user?
    // get user?
    // update user?
    // delete user and all todos.

    // Should we use auth?
    // see what the ad-server uses.

    // Mutate endpoints
    // create todos
    // delete todos
    // edit todos

    // Get endpoints
    // get todos
    // get all todo, (completed, pending or both) separate by status

    // Decide between mutiple instances of the main verticle or multiple instances of the applications.


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
