package controller.deserializers;

import com.google.gson.JsonElement;

public interface ObjectDeserializer {
    Object objectDeserialize(JsonElement json);
}