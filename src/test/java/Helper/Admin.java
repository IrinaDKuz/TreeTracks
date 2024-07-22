package Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Admin {

    public final static Map<String, String> ADMIN_STATUS_MAP = new HashMap<>() {
        {
            put("disabled", "Disabled");
            put("enabled", "Enabled");
        }};

    public static final String[] ANIMAL_WORDS = {
            "Aardvark", "Axolotl", "Binturong", "Capybara", "Dugong",
            "Echidna", "Fossa", "Gerenuk", "Hyrax", "Ibis",
            "Jerboa", "Kinkajou", "Loris", "Mara", "Numbat",
            "Okapi", "Pangolin", "Quokka", "Ratel", "Saiga",
            "Tarsier", "Uakari", "Vicuna", "Wombat", "Xerus",
            "Yapok", "Zebu", "Aye-aye", "Basilisk", "Cuscus",
            "Dhole", "Eland", "Falanouc", "Galago", "Hartebeest",
            "Indri", "Jackal", "Kudu", "Lynx", "Muntjac",
            "Nyala", "Olingo", "Paca", "Quoll", "Raccoon Dog",
            "Serval", "Tapir", "Urial", "Vole", "Wolverine",
            "Xenops", "Yabby", "Zorilla", "Agouti", "Brocket Deer",
            "Coati", "Dik-dik", "Eland", "Fossa", "Goral",
            "Hutia", "Iriomote Cat", "Jabiru", "Kea", "Langur",
            "Margay", "Nabarlek", "Ocelot", "Pudu", "Quetzal",
            "Ratel", "Sifaka", "Tamandua", "Uromastyx", "Vaquita",
            "Wombat", "Xenarthra", "Yak", "Zebra Duiker", "Arapaima",
            "Bongo", "Civet", "Desman", "Elephant Shrew", "Fennec Fox",
            "Genet", "Hoatzin", "Ibex", "Javelina", "Kinkajou",
            "Lechwe", "Musk Ox", "Nabarlek", "Ocelot", "Pika"
    };



    public static String generateName(int count, String[] array) {
        Random random = new Random();
        StringBuilder companyName = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String randomWord = array[random.nextInt(array.length)];
            companyName.append(randomWord);
            if (i < count - 1) {
                companyName.append(" ");
            }
        }
        return companyName.toString();
    }

    public static String generateCompanyUrl(String companyName) {
        Random random = new Random();
        String url = companyName.toLowerCase().replaceAll("\\s+", "-");
        url += "-" + random.nextInt(10000);
        url += ".com";
        return url;
    }

    public static String generateIPAddress() {
        Random random = new Random();
        StringBuilder ipAddress = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ipAddress.append(random.nextInt(256)); // Максимальное значение - 255
            if (i < 3) {
                ipAddress.append(".");
            }
        }
        return ipAddress.toString();
    }

    public static String generateEmail(String companyName) {
        Random random = new Random();
        String emailPrefix = companyName.toLowerCase().replaceAll("\\s+", "");
        String[] domains = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "example.com"};
        String domain = domains[random.nextInt(domains.length)];
        return emailPrefix + "@" + domain;
    }


    public static String getRandomValue(Map<Integer, String> map) {
        Object[] keys = map.keySet().toArray();
        return map.get(getRandomKey(keys));
    }

    public static Integer getRandomKey(Object[] keys) {
        // Выбираем случайный ключ из массива ключей
        return (Integer) keys[new Random().nextInt(keys.length)];
    }

    public static Integer getKeyFromValue(String value, Map<Integer, String> map) {
        for (int i = 1; i <= map.size(); i++) {
            if (map.get(i).equals(value))
                return i;
        }
        return null;
    }
}
