package edu.vrgroup.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public final class JsonUtils {

  private JsonUtils() {
  }


  private static Gson getCustomizedGson() {
    return new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .create();
  }

  public static <T> T fromJson(String json, Type type) {
    return getCustomizedGson().fromJson(json, type);
  }

  public static String toJson(Object src) {
    return getCustomizedGson().toJson(src);
  }
}
