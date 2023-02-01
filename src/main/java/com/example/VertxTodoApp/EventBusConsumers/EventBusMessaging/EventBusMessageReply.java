package com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging;

import io.vertx.core.json.JsonObject;

public class EventBusMessageReply {

  private final Boolean isError;
  private final JsonObject messageReplyJsonObject;
  private int statusCode = 404;

  public EventBusMessageReply(Boolean isError, JsonObject messageReply, int statusCode) {
    this.isError = isError;
    this.messageReplyJsonObject = messageReply;
    this.statusCode = statusCode;
  }

  public JsonObject getMessageReplyJsonObject() {
    return messageReplyJsonObject;
  }

  public Boolean getError() {
    return isError;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
}
