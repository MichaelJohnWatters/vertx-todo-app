//package com.example.VertxTodoApp.EventBusConsumers.EventBusMessaging;
//
//import com.example.VertxTodoApp.EventBusConsumers.Utils.ObjectMapperInstance;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.eventbus.MessageCodec;
//
//import java.io.IOException;
//
//public class GenericEventBusCodec<T> implements MessageCodec<T, T> {
//
//  private static final ObjectMapper mapper = ObjectMapperInstance.get();
//
//  @Override
//  public void encodeToWire(Buffer buffer, T message) {
//    try {
//      buffer.appendBytes(mapper.writeValueAsBytes(message));
//    }catch(JsonProcessingException e) {
//      System.out.println("Some error in encodeToWire");
////      logger.warn(e.getMessage(),e);
//    }
//  }
//
//  @Override
//  public T decodeFromWire(int i, Buffer buffer) {
//    try {
//      return mapper.readValue(buffer.getBytes(), T.class);
//    } catch(IOException e) {
//      System.out.println("Some error in decodeFromWire");
////      logger.error(e.getMessage(),e);
//    }
//    return null;
//  }
//
//  @Override
//  public T transform(T message) {
//    return message;
//  }
//
//  @Override
//  public String name() {
//    return this.getClass().getSimpleName();
//  }
//
//  @Override
//  public byte systemCodecID() {
//    return -1;
//  }
//}
