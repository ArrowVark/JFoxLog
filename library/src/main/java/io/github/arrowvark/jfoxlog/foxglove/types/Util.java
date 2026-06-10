package io.github.arrowvark.jfoxlog.foxglove.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.arrowvark.jfoxlog.foxglove.util.OptionalTypeAdapter;

public class Util {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(OptionalTypeAdapter.factory())
            .setPrettyPrinting()
            .create();
}
