package com.example.VertxTodoApp;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class HttpServerVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> start) throws Exception {

    Router router = MyRouter.buildRoutes(vertx);

    loadConfigAndCreateWebsevber(start, router);
  }

  private void loadConfigAndCreateWebsevber(Promise<Void> start, Router router) {
    // What if you want to add configuration, this will add a config store
    ConfigStoreOptions defaultConfig = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "config.json"));

    ConfigRetrieverOptions opts = new ConfigRetrieverOptions()
      .addStore(defaultConfig);

    // Load the configuration
    // Since this is async the verticle must be a Promise
    ConfigRetriever cfgRetriever = ConfigRetriever.create(vertx, opts);

    // This is a curried
    Handler<AsyncResult<JsonObject>> handler = asyncResult -> this.handleConfigResults(start,router,asyncResult);
    cfgRetriever.getConfig(handler);
  }

  void handleConfigResults(Promise<Void> start, Router router, AsyncResult<JsonObject> aysncresult) {
    if(aysncresult.succeeded()) {

      JsonObject config = aysncresult.result();
      JsonObject http = config.getJsonObject("http");
      int httport = http.getInteger("port");
      vertx.createHttpServer().requestHandler(router).listen(httport);

      start.complete();

    } else {
      // do somthing bla bla
      start.fail("Unable to load config.");
    }
  }
}
