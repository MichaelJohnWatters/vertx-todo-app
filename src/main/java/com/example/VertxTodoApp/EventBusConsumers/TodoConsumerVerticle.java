package com.example.VertxTodoApp.EventBusConsumers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;


public class TodoConsumerVerticle extends AbstractVerticle {

  @Override
  public void start() {

    // Register Codecs
    vertx.eventBus().registerDefaultCodec(EventBusMessageReply.class, new EventBusMessageReplyCodec());

    //
    // Create Consumers
    //

    // Get Single Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {
      // TODO database Verticle
      String extractedTodoId = (String) message.body();

      if (extractedTodoId == null){
        message.reply(new EventBusMessageReply(true, new JsonObject().put("error", "oh no todo_id is null"), 422));
      }

      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "yeoo"), 200));
    });

    // Get All Todos Consumer
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {
      // TODO database Verticle
      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "yeoo"), 200));
    });
  }
}
