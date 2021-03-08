package ru.true_ip.trueip.gson.adapters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.true_ip.trueip.models.responses.ClaimModel;

/**
 * Created by ektitarev on 26.07.2018.
 */

public class ImagesFieldAdapter implements JsonDeserializer<List<ClaimModel.PhotoUrl>> {
    @Override
    public List<ClaimModel.PhotoUrl> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            Gson gson = new  Gson();
            return gson.fromJson(json, new TypeToken<List<ClaimModel.PhotoUrl>>() {}.getType());
        }

        return new ArrayList<>();
    }
}
