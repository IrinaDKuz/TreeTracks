package Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LeadTypes {
    public final static Map<Integer, String> LEAD_TYPES_MAP = new HashMap<Integer, String>() {{
        put(1, "General");
        put(2, "Crypto_namereq");
        put(3, "Loan");
        put(4, "Adult");
        // put(5, "Geo_required");
        // put(6, "Test2");
        // put(7, "Test33");
        // put(9, "Lt_one_field");
    }};

    public static String getRandomValue(Map<Integer, String> map) {
        Object[] keys = map.keySet().toArray();
        return map.get(getRandomKey(keys));
    }

    public static Integer getRandomKey(Object[] keys) {
        // Выбираем случайный ключ из массива ключей
        return (Integer) keys[new Random().nextInt(keys.length)];
    }

    public static String getLeadTypeRandomValue() {
        return getRandomValue(LEAD_TYPES_MAP);
    }

    public static Integer getLeadTypeKeyFromValue(String value) {
        for (int i = 1; i <= LEAD_TYPES_MAP.size(); i++) {
            if (LEAD_TYPES_MAP.get(i).equals(value))
                return i;
        }
        return null;
    }
}
