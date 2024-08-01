package Helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Allure;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AllureHelper {

    public static final String CHECK = "Выполняем проверки";
    public static final String DELETE = "Удаляем ";
    public static final String DATA = "Данные для отправки: \n";
    public static final String GET_RESPONSE = "Ответ на get: ";
    public static final String ADD_RESPONSE = "Ответ на add: ";
    public static final String EDIT_RESPONSE = "Ответ на edit: ";
    public static final String DELETE_RESPONSE = "Ответ на delete: ";


    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue()) {
                        return new JsonPrimitive(src.longValue());
                    }
                    return new JsonPrimitive(src);
                }
            })
            .registerTypeAdapter(Long.class, new JsonSerializer<Long>() {
                @Override
                public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src);
                }
            })
            .registerTypeAdapter(Integer.class, new JsonSerializer<Integer>() {
                @Override
                public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src);
                }
            })
            .setPrettyPrinting()
            .create();

    public static void attachJson(String json, String name) {
        Object jsonObj = gson.fromJson(json, Object.class);
        String prettyJson = gson.toJson(jsonObj);
        Allure.addAttachment(name, "application/json", prettyJson);
    }
}