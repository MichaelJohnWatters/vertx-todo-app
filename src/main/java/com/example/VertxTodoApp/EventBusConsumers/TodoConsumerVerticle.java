package com.example.VertxTodoApp.EventBusConsumers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;


public class TodoConsumerVerticle extends AbstractVerticle {

  @Override
  public void start() {

    // Register Codecs, for decoding Custom Message Objects.
    vertx.eventBus().registerDefaultCodec(EventBusMessageReply.class, new GenericCodec<EventBusMessageReply>(EventBusMessageReply.class));

    // Consume from the event bus where the address is "hello.vertx.addr"
    vertx.eventBus().consumer(EventBusAddresses.GET_SINGLE_TODO, message -> {

      String extractedTodoId = (String) message.body();

      if (extractedTodoId == null){
        message.reply(new EventBusMessageReply(true, new JsonObject().put("error", "world"), 500));
      }

      message.reply(new EventBusMessageReply(false, new JsonObject().put("created", "world"), 200));
    });
  }
}
