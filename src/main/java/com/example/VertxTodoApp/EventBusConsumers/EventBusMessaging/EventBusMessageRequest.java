package com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging;

import io.vertx.core.json.JsonObject;

public class EventBusMessageRequest {

  private final int user_id;

  public EventBusMessageRequest(Integer user_id) {
    this.user_id = user_id;
  }

  public int getUser_id() {
    return user_id;
  }
}
