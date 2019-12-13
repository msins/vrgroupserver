package edu.vrgroup.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public final class JsonUtils {

  private JsonUtils() {
  }


  private static Gson getWritingGson() {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .create();
  }

  private static Gson getReadingGson() {
    return new GsonBuilder().create();
  }

  public static <T> T fromJson(String json, Type type) {
    return getReadingGson().fromJson(json, type);
  }

  public static String toJson(Object src) {
    return getWritingGson().toJson(src);
  }
}
