package com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging;

import io.vertx.core.json.JsonObject;

public class EventBusMessageRequest {

  private final String user_id;
  private final JsonObject params;

  public EventBusMessageRequest(String user_id, JsonObject params) {
    this.user_id = user_id;
    this.params = params;
  }

  public String getUser_id() {
    return user_id;
  }

  public JsonObject getParams() {
    return params;
  }
}
