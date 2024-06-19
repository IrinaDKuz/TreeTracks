package Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Adverts {
    public static final String affiliateKey_73 = "d0a5b4521aa1bbcf3fec75b63c64ac79";
    public static final String affiliateKey_86 = "ddafc4211beffcd86627f346ce3b0e45";
    public static final String affiliateKey_87 = "728c1d0cfa05c465e22f7e2489554a03";
    public static final String affiliateKey_88 = "636ee2ecb1c98fb7a441ab620e26e567";
    public static final String affiliateKey_90 = "720481de6ec3c633e96a6fb53c3ae24d";
    public static final String affiliateKey_95 = "bccea111d4fb548739c8218a738740d4";


    public static Map<Integer, String> affiliatesKeys_map = new HashMap<Integer, String>() {{
        put(73, affiliateKey_73);
        put(86, affiliateKey_86);
        put(87, affiliateKey_87);
        put(88, affiliateKey_88);
        put(90, affiliateKey_90);
        put(95, affiliateKey_95);
    }};

    public final static Map<Object, String> MODEL_TYPES_MAP = new HashMap<>() {{
        put(1, "Cpa");
        put(2, "RevShare");
        put(3, "Other");
    }};

    public final static Map<Object, String> NOTES_TYPES_MAP = new HashMap<>() {{
        put(1, "Call");
        put(2, "Conference");
        put(3, "Meeting");
    }};

    public final static Map<Object, String> STATUS_MAP = new HashMap<>() {
        {
            put(1, "In Work");
            put(2, "Find Contact");
            put(3, "Discussion");
            put(4, "Integration");
            put(5, "Active");
            put(6, "Pause");
            put(7, "Discussion");
            put(8, "Decline");
        }};

    public final static Map<Object, String> PERSON_STATUS_MAP = new HashMap<Object, String>() {
        {
            put(1, "Active");
            put(2, "Disable");
        }};


    public static final String[] COMPANY_WORDS = {"Tech", "Global", "Future", "Innovative", "Smart", "Digital", "Creative",
            "Dynamic", "Eco", "Bright", "Alpha", "Omega", "Hyper", "Super", "Ultra",
            "Solutions", "Systems", "World", "Enterprise", "Services", "Lab", "Hub",
            "Labs", "Space", "Network", "Innovations", "Technologies", "Consulting",
            "Group", "Industries", "Corporation", "Inc.", "Ltd.", "Enterprises",
            "Ventures", "Associates", "Partners", "Industries", "Co.", "Global",
            "Worldwide", "International", "Solutions", "Tech"};


    public static final String[] CONTACT_WORDS = {
            "Rose", "Tulip", "Sunflower", "Daisy", "Lily",
            "Orchid", "Marigold", "Chrysanthemum", "Carnation", "Hyacinth",
            "Daffodil", "Peony", "Poppy", "Iris", "Violet",
            "Lavender", "Jasmine", "Dahlia", "Begonia", "Freesia",
            "Lilac", "Camellia", "Gardenia", "Anemone", "Azalea",
            "Bougainvillea", "Buttercup", "Calla Lily", "Cherry Blossom", "Crocus",
            "Cyclamen", "Forget-Me-Not", "Geranium", "Gladiolus", "Hibiscus",
            "Hollyhock", "Hydrangea", "Magnolia", "Morning Glory", "Narcissus",
            "Petunia", "Primrose", "Rhododendron", "Snapdragon", "Snowdrop",
            "Sweet Pea", "Verbena", "Wisteria", "Yarrow", "Zinnia",
            "Amaryllis", "Anthurium", "Aster", "Bachelor's Button", "Balloon Flower",
            "Bee Balm", "Bellflower", "Bird of Paradise", "Bleeding Heart", "Bluebell",
            "Borage", "Brugmansia", "Calendula", "Campanula", "Canna",
            "Clematis", "Columbine", "Cosmos", "Crown Imperial", "Dahlberg Daisy",
            "Delphinium", "Dianthus", "Echinacea", "Evening Primrose", "Foxglove",
            "Gazania", "Gerbera", "Gloriosa Daisy", "Godetia", "Goldenrod",
            "Helenium", "Heliotrope", "Hellebore", "Honeysuckle", "Impatiens",
            "Jacob's Ladder", "Lantana", "Lobelia", "Lupine", "Mallow",
            "Mimosa", "Monkshood", "Nasturtium", "Nemesia", "Nigella",
            "Passionflower", "Penstemon", "Phlox", "Plumeria", "Salvia",
            "Scabiosa", "Sedum", "Statice", "Stephanotis", "Tithonia" };


    public static final String[] REQUISITES_WORDS = {
            "Account Number", "Routing Number", "SWIFT Code", "IBAN", "BIC",
            "Bank Name", "Branch Name", "Branch Address", "Account Type", "Currency",
            "Account Holder Name", "Tax ID", "VAT Number", "Company Name", "Company Address",
            "Billing Address", "Shipping Address", "Invoice Number", "Purchase Order Number", "Due Date",
            "Payment Terms", "Contact Number", "Email Address", "Website URL", "Customer ID",
            "Supplier ID", "Transaction ID", "Payment Method", "Credit Card Number", "CVV"
    };


    public static final String[] DESCRIPTION_WORDS = {
            "Intelligent", "Kind", "Honest", "Brave", "Creative",
            "Loyal", "Empathetic", "Generous", "Hardworking", "Ambitious",
            "Optimistic", "Punctual", "Friendly", "Caring", "Patient",
            "Adaptable", "Confident", "Diligent", "Enthusiastic", "Humorous",
            "Polite", "Sincere", "Thoughtful", "Trustworthy", "Versatile",
            "Warm-hearted", "Charismatic", "Dependable", "Determined", "Energetic",
            "Fair", "Forgiving", "Helpful", "Imaginative", "Independent",
            "Meticulous", "Open-minded", "Organized", "Passionate", "Perceptive",
            "Persistent", "Practical", "Rational", "Reliable", "Respectful",
            "Resourceful", "Self-disciplined", "Sociable", "Supportive", "Tolerant"
    };

    public static String generateName(int count, String[] array) {
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String randomWord = array[random.nextInt(array.length)];
            name.append(randomWord);
            if (i < count - 1) {
                name.append(" ");
            }
        }
        return name.toString();
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


    public static Object getRandomValue(Map<Object, String> map) {
        Object[] keys = map.keySet().toArray();
        return map.get(getRandomKey(keys));
    }

    public static Object getRandomKey(Object[] keys) {
        // Выбираем случайный ключ из массива ключей
        return keys[new Random().nextInt(keys.length)];
    }

    public static Integer getKeyFromValue(String value, Map<Integer, String> map) {
        for (int i = 1; i <= map.size(); i++) {
            if (map.get(i).equals(value))
                return i;
        }
        return null;
    }
}
