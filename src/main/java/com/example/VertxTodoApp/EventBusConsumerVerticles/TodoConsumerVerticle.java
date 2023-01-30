package com.example.VertxTodoApp.EventBusConsumerVerticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class TodoConsumerVerticle extends AbstractVerticle {

  // Create entry point to the verticle
  @Override
  public void start(){

    // Consume from the event bus where the address is "hello.vertx.addr"
    vertx.eventBus().consumer("hello.vertx.addr", message -> {
      message.reply("Hello from vertx");
    });

    // Consume from the event bus where the address is "hello.named.vertx.addr"
    vertx.eventBus().consumer("hello.named.addr", message -> {

        String extractedMessageBodyName = (String) message.body();
        message.reply(String.format("Hello %s!", extractedMessageBodyName));
      }
    );
  }
}
