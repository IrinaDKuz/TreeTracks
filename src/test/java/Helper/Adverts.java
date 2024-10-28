package Helper;

import AdvertPackage.entity.AdvertPrimaryInfo;

import java.util.*;

import static SQL.AdvertSQL.getRandomValueFromBDWhere;
import static SQL.AdvertSQL.getValueFromBDWhere;

public class Adverts {

    public static final String ADD_ADVERT_BUTTON = "+ Add new Advertiser";
    public static final String NAME = "Name";
    public static final String COMPANY_LEGAL_NAME = "Company's legal name";
    public static final String SITE_URL = "Site URL";
    public static final String STATUS = "Status";
    public static final String PRICING_MODEL = "Pricing model";
    public static final String GEO = "GEO";
    public static final String MANAGER = "Manager";

    public static final String SALES_MANAGER = "Sales Manager";
    public static final String ACCOUNT_MANAGER = "Account Manager";
    public static final String CATEGORIES = "Categories";
    public static final String USER_REQUEST_SOURCE = "User Request Source";
    public static final String USER_REQUEST_SOURCE_VALUE = "User Request Source Value";
    public static final String TAG = "Tag";
    public static final String TAGS = "Tags";


    public static final String CONTACTS = "Contacts";
    public static final String PAYMENT_INFO = "Payment info";
    public static final String NOTES = "Notes";


    public final static Map<String, String> MODEL_TYPES_MAP = new HashMap<>() {{
        put("cpa", "Cpa");
        put("revshare", "RevShare");
        put("other", "Other");
        put("hybrid", "Hybrid");

    }};

    public final static Map<String, String> NOTES_TYPES_MAP = new HashMap<>() {{
        put("call", "Call");
        put("event", "Event");
        put("meeting", "Meeting");
    }};

    public final static Map<String, String> ADVERT_STATUS_MAP = new HashMap<>() {
        {
            put("put_on_search", "Put on search");
            put("assigned_to_sales_manager", "Assigned to Sales manager");
            put("in_touch", "In touch");
            put("discussion", "Discussion");
            put("integration", "Integration");
            put("active", "Active");
            put("paused", "Paused");
            put("declined", "Declined");
        }
    };

    public final static Map<String, String> PERSON_STATUS_MAP = new HashMap<>() {
        {
            put("1", "Active");
            put("2", "Disabled");
        }
    };

    public final static Map<String, String> ADVERT_ACCESS_TYPE = new HashMap<>() {
        {
            put("single", "Single");
            put("multiple", "Multiple");
        }
    };

    public static final String[] COMPANY_WORDS = {"Tech", "Global", "Future", "Innovative", "Smart", "Digital", "Creative",
            "Dynamic", "Eco", "Bright", "Alpha", "Omega", "Hyper", "Super", "Ultra",
            "Solutions", "Systems", "World", "Enterprise", "Services", "Lab", "Hub",
            "Labs", "Space", "Network", "Innovations", "Technologies", "Consulting",
            "Group", "Industries", "Corporation", "Inc", "Ltd", "Enterprises",
            "Ventures", "Associates", "Partners", "Industries", "Co", "Global",
            "Worldwide", "International", "Solutions", "Tech"};


    public static final String[] CONTACT_WORDS = {
            "Taylor", "Jordan", "Morgan", "Casey", "Parker", "Bailey", "Alex", "Dylan", "Avery", "Blake",
            "Cameron", "Sydney", "Harper", "Hunter", "Quinn", "Reagan", "Rowan", "Emerson", "Riley", "Skyler",
            "Kendall", "Logan", "Madison", "Spencer", "Tyler", "Finley", "Sawyer", "Hayden", "Payton", "Sullivan",
            "Terry", "Jamie", "Shawn", "Devon", "Ellis", "Lee", "Leslie", "Marley", "Mason", "Reed",
            "Robin", "Sage", "Shelby", "Shannon", "Tanner", "Teagan", "Tracy", "Vernon", "Whitney", "Adrian",
            "Blair", "Charlie", "Chris", "Dakota", "Drew", "Elliot", "Evan", "Francis", "Gale", "Jesse",
            "Jody", "Kelly", "Kennedy", "Lane", "Lynn", "Micah", "Pat", "Perry", "Quincy", "Ray",
            "Reese", "Robin", "Shay", "Shiloh", "Sidney", "Stevie", "Terry", "Tony", "Tracey", "Tristan",
            "Val", "Walker", "Wesley", "Wynn", "Ashton", "Casey", "Corey", "Dale", "Emery", "Harley",
            "Hayes", "Jaden", "Jay", "Jules", "Justice", "Kim", "Lane", "Lee", "Loren", "Reese",
            "Carter", "Miller", "Bennett", "Bailey", "Austin", "Bradley", "Brooks", "Bryant", "Campbell", "Chase",
            "Clark", "Cole", "Collins", "Cooper", "Curtis", "Davis", "Dean", "Douglas", "Duncan", "Ellis",
            "Fletcher", "Franklin", "Garrett", "Graham", "Grant", "Gray", "Greene", "Griffin", "Hale", "Hamilton",
            "Harris", "Harrison", "Hart", "Hawkins", "Hayes", "Henry", "Howard", "Hudson", "Jackson", "James",
            "Jenkins", "Johnson", "Jones", "Kelly", "Kennedy", "King", "Knight", "Lawrence", "Lee", "Lewis",
            "Marshall", "Mason", "Mitchell", "Morgan", "Murphy", "Nelson", "Newton", "Norris", "Palmer", "Parker",
            "Payne", "Porter", "Quinn", "Reed", "Reid", "Roberts", "Russell", "Ryan", "Scott", "Shaw",
            "Shelton", "Simpson", "Smith", "Spencer", "Stanley", "Steele", "Stevens", "Stewart", "Stone", "Taylor",
            "Thomas", "Thompson", "Turner", "Tyler", "Vaughn", "Wade", "Walker", "Wallace", "Walsh", "Ward",
            "Warren", "Washington", "Watson", "Webb", "West", "White", "Williams", "Wilson", "Wright", "Young"
    };

    public static final String[] FLOWER_WORDS = {
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
            "Scabiosa", "Sedum", "Statice", "Stephanotis", "Tithonia"};


    public static final String[] JOB_WORDS = {
            "Software Engineer", "Data Scientist", "Product Manager", "Marketing Specialist", "Sales Representative",
            "Graphic Designer", "Customer Service Representative", "Project Manager", "Human Resources Manager", "Business Analyst",
            "Network Administrator", "Web Developer", "Accountant", "Financial Analyst", "Operations Manager",
            "IT Support Specialist", "Content Writer", "Digital Marketing Manager", "SEO Specialist", "UX/UI Designer",
            "Database Administrator", "System Analyst", "Social Media Manager", "Legal Advisor", "Technical Writer",
            "Quality Assurance Engineer", "Biomedical Engineer", "Cybersecurity Analyst", "Cloud Architect", "Mobile App Developer"
    };


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


    public static final String[] LOGIN_WORDS = {
            "Cobra", "Viper", "Python", "Boa", "Anaconda",
            "Mamba", "Rattlesnake", "Adder", "Copperhead", "Coral",
            "Scarab", "Mantis", "Beetle", "Dragonfly", "Butterfly",
            "Wasp", "Hornet", "Ant", "Bee", "Mosquito",
            "Gnat", "Cricket", "Locust", "Termite", "Flea",
            "Centipede", "Millipede", "Spider", "Tarantula", "Scorpion",
            "Grasshopper", "Cicada", "Firefly", "Ladybug", "Weevil",
            "Tick", "Mite", "Garter", "King", "Milk",
            "Pit", "Bushmaster", "Fer_de_Lance", "Taipan", "Asp",
            "Krait", "Cottonmouth", "SeaSnake", "BoaConstrictor", "GaboonViper",
            "Copperhead", "Kingsnake", "Boomslang", "Keelback", "WaterMoccasin",
            "Sidewinder", "PuffAdder", "Saw scaledViper", "TreeViper", "BushViper",
            "RoughGreenSnake", "SmoothGreenSnake", "EasternBrownSnake", "Red_belliedBlackSnake", "Inland_Taipan",
            "DeathAdder", "WhipSnake", "BrownTreeSnake", "TigerSnake", "SandViper",
            "FalseWaterCobra", "NightSnake", "EarthSnake", "GrassSnake", "BlindSnake",
            "Blunt_nosedViper", "MoleSnake", "SpeckledKingsnake", "BrahminBlindSnake", "HognoseSnake",
            "ThreadSnake", "SmoothSnake", "SandSnake", "Leaf_nosedSnake", "SwampSnake",
            "Shieldtail", "WormSnake", "RatSnake", "Bullsnake", "GopherSnake",
            "RockPython", "ReticulatedPython", "Green_TreePython", "BlackMamba", "Blue_Krait"
    };


    public static void showAdvertPrimaryInfoInformation(AdvertPrimaryInfo advertPrimaryInfo) {
        System.out.println(NAME + ": " + advertPrimaryInfo.getCompany());
        System.out.println(COMPANY_LEGAL_NAME + ": " + advertPrimaryInfo.getCompanyLegalName());
        System.out.println(STATUS + ": " + advertPrimaryInfo.getStatus());
        System.out.println(SITE_URL + ": " + advertPrimaryInfo.getSiteUrl());
        System.out.println(PRICING_MODEL + ": " + advertPrimaryInfo.getPricingModel());

        System.out.println(MANAGER + ": " + advertPrimaryInfo.getManagerId() + ": " + advertPrimaryInfo.getManagerName());
        System.out.println(SALES_MANAGER + ": " + advertPrimaryInfo.getSalesManagerId() + ": " + advertPrimaryInfo.getSalesManagerName());
        System.out.println(ACCOUNT_MANAGER + ": " + advertPrimaryInfo.getAccountManagerId() + ": " + advertPrimaryInfo.getSalesManagerName());

        System.out.println(CATEGORIES + ": " + advertPrimaryInfo.getCategoriesId() + ": " + advertPrimaryInfo.getCategoriesName());
        System.out.println(TAGS + ": " + advertPrimaryInfo.getTagId() + ": " + advertPrimaryInfo.getTagName());
        System.out.println(USER_REQUEST_SOURCE + ": " + advertPrimaryInfo.getUserRequestSource());
        System.out.println(NOTES + ": " + advertPrimaryInfo.getNote());
    }

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


    public static List<String> generateCategoryList(int count, List<String> listOfAll) throws Exception {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(getRandomValueFromBDWhere("id", "category", "lang", "general"));
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

    public static Integer getExistManagerFromAdvert(String advertId) throws Exception {
        String managerId = getValueFromBDWhere("account_manager", "advert",
                "id", advertId);
        Integer managerIntId;
        if (managerId.equals("null")) {
            managerId = getValueFromBDWhere("sales_manager", "advert",
                    "id", advertId);
            if (managerId.equals("null")) {
                managerId = getValueFromBDWhere("manager", "advert",
                        "id", advertId);
                if (managerId.equals("null")) {
                    managerIntId = 1;
                } else managerIntId = Integer.valueOf(managerId);
            } else managerIntId = Integer.valueOf(managerId);
        } else managerIntId = Integer.valueOf(managerId);
        return managerIntId;
    }


}
