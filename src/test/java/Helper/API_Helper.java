package Helper;

import java.util.Random;

public class API_Helper {
    public static String getRandomBooleanString() {
        return new Random().nextBoolean() ? "true" : "false";
    }
}
