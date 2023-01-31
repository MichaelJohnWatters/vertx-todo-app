package com.example.VertxTodoApp.EventBusConsumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;

public class EventBusMessageReplyCodec implements MessageCodec<EventBusMessageReply, EventBusMessageReply> {

  private static final ObjectMapper mapper = ObjectMapperInstance.get();

  @Override
  public void encodeToWire(Buffer buffer, EventBusMessageReply eventBusMessageReply) {
    try {
      buffer.appendBytes(mapper.writeValueAsBytes(eventBusMessageReply));
    }catch(JsonProcessingException e) {
      System.out.println("Some error in encodeToWire");
//      logger.warn(e.getMessage(),e);
    }
  }

  @Override
  public EventBusMessageReply decodeFromWire(int i, Buffer buffer) {
    try {
      return mapper.readValue(buffer.getBytes(), EventBusMessageReply.class);
    } catch(IOException e) {
      System.out.println("Some error in decodeFromWire");
//      logger.error(e.getMessage(),e);
    }
    return null;
  }

  @Override
  public EventBusMessageReply transform(EventBusMessageReply eventBusMessageReply) {
    return eventBusMessageReply;
  }

  @Override
  public String name() {
    return this.getClass().getSimpleName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
