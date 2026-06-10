package io.github.arrowvark.jfoxlog.foxglove.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {
    private final TypeAdapter<T> valueAdapter;

    public OptionalTypeAdapter(TypeAdapter<T> valueAdapter) {
        this.valueAdapter = valueAdapter;
    }

    @Override
    public void write(JsonWriter out, Optional<T> value) throws IOException {
        if (value == null || value.isEmpty()) {
            out.nullValue();
        } else {
            valueAdapter.write(out, value.get());
        }
    }

    @Override
    public Optional<T> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return Optional.empty();
        }

        T result = valueAdapter.read(in);
        return Optional.of(result);
    }

    public static <T> TypeAdapterFactory factory() {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            @Override
            public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {

                if (type.getRawType() != Optional.class) return null;

                if (!(type.getType() instanceof java.lang.reflect.ParameterizedType)) {
                    return (TypeAdapter<R>) new OptionalTypeAdapter<>(gson.getAdapter(Object.class));
                }
                if (!Optional.class.isAssignableFrom(type.getRawType())) {
                    return null;
                }
                Type actualType = ((java.lang.reflect.ParameterizedType) type.getType())
                        .getActualTypeArguments()[0];
                TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(actualType));
                return (TypeAdapter<R>) new OptionalTypeAdapter<>(valueAdapter);
            }
        };
    }
}
