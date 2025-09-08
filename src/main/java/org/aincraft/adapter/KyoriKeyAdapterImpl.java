package org.aincraft.adapter;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;

public final class KyoriKeyAdapterImpl extends TypeAdapter<Key> {

  @Override
  public void write(JsonWriter out, Key key) throws IOException {
    if (key == null) {
      out.nullValue();
      return;
    }
    out.beginObject();
    out.name("namespace").value(key.namespace());
    out.name("value").value(key.value());
    out.endObject();
  }

  @Override
  public Key read(JsonReader in) throws IOException {
    switch (in.peek()) {
      case NULL -> {
        in.nextNull();
        return null;
      }
      case BEGIN_OBJECT -> {
        String namespace = null, value = null;
        in.beginObject();
        while (in.hasNext()) {
          String name = in.nextName();
          switch (name) {
            case "namespace" -> namespace = in.nextString();
            case "value" -> value = in.nextString();
            default -> in.skipValue();
          }
        }
        in.endObject();
        if (namespace == null || value == null) {
          throw new com.google.gson.JsonParseException(
              "Key object must contain 'namespace' and 'value' or 'asString'.");
        }
        return new NamespacedKey(namespace, value);
      }
      default -> throw new com.google.gson.JsonParseException(
          "Expected STRING or OBJECT for Key but was " + in.peek());
    }
  }
}
