package org.aincraft.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.aincraft.domain.ClientBlockDataImpl;
import org.aincraft.api.ClientBlockData;

public class ClientBlockDataFactoryImpl implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getRawType() != ClientBlockData.class) return null;

    final TypeAdapter<ClientBlockDataImpl> delegate = gson.getAdapter(ClientBlockDataImpl.class);

    return (TypeAdapter<T>) new TypeAdapter<ClientBlockData>() {
      @Override public void write(JsonWriter out, ClientBlockData value) throws IOException {
        // assume your implementation is ClientBlockDataImpl
        delegate.write(out, (ClientBlockDataImpl) value);
      }

      @Override
      public ClientBlockData read(JsonReader in) throws IOException {
        return delegate.read(in);
      }
    };
  }
}
