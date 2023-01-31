package com.example.VertxTodoApp.EventBusConsumers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public final class ObjectMapperInstance {

  private enum Singleton {
    INSTANCE;

    private final ObjectMapper mapper;
    private Singleton() {
      mapper = new ObjectMapper();
      mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
  }

  public static final ObjectMapper get() {
    return Singleton.INSTANCE.mapper;
  }
}
