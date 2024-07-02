package Helper;

import java.util.*;

import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class Adverts {

    public final static Map<String, String> MODEL_TYPES_MAP = new HashMap<>() {{
        put("1", "Cpa");
        put("2", "RevShare");
        put("3", "Other");
    }};

    public final static Map<String, String> NOTES_TYPES_MAP = new HashMap<>() {{
        put("1", "Call");
       // put(2, "Conference");
        put("2", "Meeting");
    }};

    public final static Map<String, String> STATUS_MAP = new HashMap<>() {
        {
            put("put_on_search", "Put on search");
            put("assigned_to_sales_manager", "Assigned to Sales manager");
            put("in_touch", "In touch");
            put("discussion", "Discussion");
            put("integration", "Integration");
            put("active", "Active");
            put("paused", "Paused");
            put("declined", "Declined");
        }};

    public final static Map<String, String> PERSON_STATUS_MAP = new HashMap<String, String>() {
        {
            put("1", "Active");
            put("2", "Disabled");
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

    public static List<String> generateNameList(int count, String[] array) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(generateName(1, array));
        }
        return list;
    }


    public static List<String> generateCategoryList(int count) throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(getRandomValueFromBDWhere("id", "category", "lang", "'general'"));
        }
        return list;
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


    public static Object getRandomKey(Object[] keys) {
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
