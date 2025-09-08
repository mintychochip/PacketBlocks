package org.aincraft.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.aincraft.domain.ModelDataImpl;
import org.aincraft.api.ModelData;

public class ClientBlockDataFactoryImpl implements TypeAdapterFactory {

  @SuppressWarnings("unchecked")
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    if (type.getRawType() != ModelData.class) return null;

    final TypeAdapter<ModelDataImpl> delegate = gson.getAdapter(ModelDataImpl.class);

    return (TypeAdapter<T>) new TypeAdapter<ModelData>() {
      @Override public void write(JsonWriter out, ModelData value) throws IOException {
        // assume your implementation is ClientBlockDataImpl
        delegate.write(out, (ModelDataImpl) value);
      }

      @Override
      public ModelData read(JsonReader in) throws IOException {
        return delegate.read(in);
      }
    };
  }
}
