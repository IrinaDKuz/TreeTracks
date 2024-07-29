package Helper;

import java.util.*;

import static SQL.AdvertSQL.getRandomValueFromBDWhere;

public class Offers {

    public static final String ADD_ADVERT_BUTTON = "+ Add new Advertiser";
    public static final String NAME = "Name";
    public static final String COMPANY_LEGAL_NAME = "Company's legal name";
    public static final String SITE_URL = "Site URL";
    public static final String STATUS = "Status";
    public static final String PRICING_MODEL = "Pricing model";
    public static final String NOTES = "Notes";
    public static final String MANAGER = "Manager";
    public static final String USER_REQUEST_SOURCE = "User Request Source";
    public static final String USER_REQUEST_SOURCE_VALUE = "User Request Source Value";
    public static final String TAG = "Tag";


    public static final String[] OFFER_WORDS = {
            "Exclusive", "Limited", "Discount", "Deal", "Promotion",
            "Special", "Offer", "Save", "Free", "Bonus",
            "Sale", "Gift", "Reward", "Bargain", "Best",
            "Hot", "Amazing", "Incredible", "Unbeatable", "Huge",
            "New", "Launch", "Limited-time", "Flash", "Instant",
            "Cashback", "Prize", "Giveaway", "Sweepstake", "Contest",
            "Referral", "Subscription", "Membership", "Access", "VIP",
            "Premium", "Early-bird", "First", "Top", "Exclusive-access",
            "Secret", "Hidden", "Limited-stock", "One-time", "No-brainer",
            "Best-seller", "Top-rated", "Fan-favorite", "Must-have", "Essential",
            "Bundle", "Package", "Combo", "Mega", "Ultimate",
            "Supreme", "Extreme", "Powerful", "Effective", "Reliable",
            "Trusted", "Verified", "Proven", "Guaranteed", "Risk-free",
            "Secure", "Safe", "User-friendly", "Easy", "Convenient",
            "Fast", "Quick", "Instant-access", "Immediate", "On-demand",
            "24/7", "Support", "Customer", "Satisfaction", "Review",
            "Feedback", "Rating", "Testimonial", "Recommendation", "Endorsement",
            "Award-winning", "Certified", "Accredited", "Qualified", "Licensed",
            "Expert", "Professional", "Specialist", "Authority", "Leader",
            "Innovative", "Cutting-edge", "Advanced", "Modern", "Trendy"
    };

    public static final String[] TECHNOLOGY_WORDS = {
            "ArtificialIntelligence", "MachineLearning", "Blockchain", "Cryptocurrency", "VirtualReality",
            "AugmentedReality", "InternetOfThings", "CloudComputing", "BigData", "CyberSecurity",
            "QuantumComputing", "5G", "Automation", "Robotics", "Nanotechnology",
            "Biotechnology", "Wearables", "SmartHomes", "SelfDrivingCars", "Drones",
            "3DPrinting", "EdgeComputing", "Microservices", "DevOps", "Fintech",
            "EdTech", "MedTech", "CleanTech", "GreenTech", "SpaceTech",
            "Bioinformatics", "NeuralNetworks", "DataScience", "DigitalTwins", "SmartCities",
            "eCommerce", "Gaming", "SoftwareDevelopment", "Hardware", "Networking",
            "Telecommunications", "ArtificialGeneralIntelligence", "AugmentedAnalytics", "ComputerVision", "NaturalLanguageProcessing",
            "InternetSecurity", "DigitalTransformation", "SmartDevices", "ConnectedWorld", "QuantumInternet"
    };


    public final static Map<String, String> OFFER_STATUS_MAP = new HashMap<>() {
        {
            put("active", "Active");
            put("disabled", "Disabled");
            put("paused", "Paused");
        }};

    public final static Map<String, String> OFFER_PRIVACY_LEVEL = new HashMap<>() {
        {
            put("public", "Public");
            put("pre_moderation", "Pre Moderation");
            put("private", "Private");
            put("integration", "Integration");

        }};

    public final static Map<String, String> LANDING_PAGE_TYPE = new HashMap<>() {
        {
            put("landing", "landing");
            put("pre_landing", "prelanding");
        }};

    public final static Map<String, String> REDIRECT_TYPE = new HashMap<>() {
        {
            put("http_302", "HTTP 302 redirect");
            put("http_meta", "http_meta");
            put("js_meta", "js_meta");
            put("http_302_with_hidded_reffer", "HTTP 302 redirect with hidded reffer");

        }};


    public final static Map<String, String> PERSON_STATUS_MAP = new HashMap<>() {
        {
            put("1", "Active");
            put("2", "Disabled");
        }};


    public static final String[] COMPANY_WORDS = {"Tech", "Global", "Future", "Innovative", "Smart", "Digital", "Creative",
            "Dynamic", "Eco", "Bright", "Alpha", "Omega", "Hyper", "Super", "Ultra",
            "Solutions", "Systems", "World", "Enterprise", "Services", "Lab", "Hub",
            "Labs", "Space", "Network", "Innovations", "Technologies", "Consulting",
            "Group", "Industries", "Corporation", "Inc", "Ltd", "Enterprises",
            "Ventures", "Associates", "Partners", "Industries", "Co", "Global",
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


}
