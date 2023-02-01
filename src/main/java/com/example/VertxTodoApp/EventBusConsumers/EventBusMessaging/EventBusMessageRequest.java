package com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging;

import io.vertx.core.json.JsonObject;

public class EventBusMessageRequest {

  private final int user_id;
  private final JsonObject params;

  public EventBusMessageRequest(Integer user_id, JsonObject params) {
    this.user_id = user_id;
    this.params = params;
  }

  public int getUser_id() {
    return user_id;
  }

  public JsonObject getParams() {
    return params;
  }
}
