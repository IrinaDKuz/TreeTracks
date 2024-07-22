package Helper;

import java.util.*;

public class Settings {

    public final static Map<String, String> EMAIL_TYPE = new HashMap<>() {
        {
            put("system", "System");
            put("announcements", "Announcements");
            put("weekly_summaries", "Weekly summaries");
        }
    };

    public final static Map<String, String> EMAIL_PROTOCOL = new HashMap<>() {
        {
            put("smtp_ssl", "SMTP + SSL");
            put("smtp", "SMTP");
            put("smtp_tsl", "SMTP + TSL");
        }
    };

    public static final String[] COUNTRY_LETTERS = {
            "af", "al", "dz", "ad", "ao", "ag", "ar", "am", "au", "at", "az", "bs", "bh", "bd", "bb", "by", "be", "bz", "bj", "bt",
            "bo", "ba", "bw", "br", "bn", "bg", "bf", "bi", "ci", "cv", "kh", "cm", "ca", "cf", "td", "cl", "cn", "co", "km", "cr",
            "hr", "cu", "cy", "cz", "cd", "dk", "dj", "dm", "do", "ec", "eg", "sv", "gq", "er", "ee", "sz", "et", "fj", "fi", "fr",
            "ga", "gm", "ge", "de", "gh", "gr", "gd", "gt", "gn", "gw", "gf", "ht", "va", "hn", "hk", "hu", "is", "in", "id", "ir",
            "iq", "ie", "il", "it", "jm", "jp", "jo", "kz", "ke", "ki", "kw", "kg", "la", "lv", "lb", "ls", "lr", "ly", "li", "lt",
            "lu", "mg", "mw", "my", "mv", "ml", "mt", "mh", "mr", "mu", "mx", "fm", "md", "mc", "mn", "me", "ma", "mz", "mm", "na",
            "nr", "np", "nl", "nz", "ni", "ne", "ng", "kp", "mk", "no", "om", "pk", "pw", "ps", "pa", "pg", "py", "pe", "ph", "pl",
            "pt", "qa", "ro", "ru", "rw", "kn", "lc", "vc", "ws", "sm", "st", "sa", "sn", "rs", "sc", "sl", "sg", "sk", "si", "sb",
            "so", "za", "kr", "ss", "es", "lk", "sd", "sr", "se", "ch", "sy", "tj", "tz", "th", "tl", "tg", "to", "tt", "tn", "tr",
            "tm", "tv", "ug", "ua", "ae", "gb", "us", "uy", "uz", "vu", "ve", "vn", "ye", "zm", "zw"
    };

    public static final String[] CITIES_WORDS = {
            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
            "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville",
            "Fort Worth", "Columbus", "Charlotte", "San Francisco", "Indianapolis",
            "Seattle", "Denver", "Washington", "Boston", "El Paso", "Nashville", "Detroit",
            "Oklahoma City", "Portland", "Las Vegas", "Memphis", "Louisville", "Baltimore",
            "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento", "Kansas City",
            "Long Beach", "Mesa", "Atlanta", "Colorado Springs", "Virginia Beach", "Raleigh",
            "Omaha", "Miami", "Oakland", "Minneapolis", "Tulsa", "Wichita", "New Orleans",
            "Arlington", "Cleveland", "Bakersfield", "Tampa", "Aurora", "Honolulu", "Anaheim",
            "Santa Ana", "Riverside", "Corpus Christi", "Lexington", "Stockton", "Henderson",
            "Saint Paul", "St. Louis", "Cincinnati", "Pittsburgh", "Greensboro", "Anchorage",
            "Plano", "Lincoln", "Orlando", "Irvine", "Newark", "Toledo", "Durham", "Chula Vista",
            "Fort Wayne", "Jersey City", "St. Petersburg", "Laredo", "Madison", "Chandler",
            "Buffalo", "Lubbock", "Scottsdale", "Reno", "Glendale", "Gilbert", "Winston-Salem",
            "North Las Vegas", "Norfolk", "Chesapeake", "Garland", "Irving", "Hialeah",
            "Fremont", "Boise", "Richmond"
    };

    public static final String[] CATEGORIES_WORDS = {
            "Sports Betting", "Casino Games", "Poker", "Slot Machines", "Roulette",
            "Blackjack", "Bingo", "Lottery", "Online Gambling", "Offline Betting",
            "Virtual Sports", "E-sports Betting", "Live Dealer Games", "Craps", "Keno",
            "Fantasy Sports", "Scratch Cards", "Arcade Games", "Social Gambling", "High Stakes",
            "Low Stakes", "Sportsbook", "Horse Racing", "Greyhound Racing", "Cockfighting",
            "Betting Exchanges", "Spread Betting", "Binary Options", "Financial Betting", "Political Betting",
            "Novelty Betting", "In-play Betting", "Daily Fantasy Sports", "Virtual Reality Gambling", "Skill Games",
            "Multiplayer Games", "Mobile Gambling", "Bitcoin Gambling", "Cryptocurrency Betting", "Provably Fair Games",
            "Blockchain Gambling", "Crypto Casinos", "Social Casinos", "Gamification", "Interactive Gaming",
            "Simulated Reality", "Futures Betting", "Asian Handicap", "Point Spread", "Over/Under Betting",
            "Proposition Bets", "Video Poker", "Baccarat", "Backgammon", "Monopoly Slots",
            "Wheel of Fortune", "Sic Bo", "Pachinko", "Mahjong Betting", "Election Betting",
            "Award Show Betting", "Stock Market Betting", "Weather Betting", "Chess Betting", "Bridge Tournaments",
            "Online Tournaments", "Offline Tournaments", "Tote Betting", "Exotic Bets", "Virtual Horse Racing",
            "Competitive Gaming", "Dota 2 Betting", "League of Legends Betting", "Counter-Strike Betting", "Fortnite Betting",
            "Apex Legends Betting", "Call of Duty Betting", "PUBG Betting", "FIFA Betting", "Madden NFL Betting",
            "NBA 2K Betting", "Rocket League Betting", "Street Fighter Betting", "Super Smash Bros Betting", "Tekken Betting",
            "Pokemon Betting", "Magic: The Gathering Betting", "World of Warcraft Betting", "StarCraft Betting", "Warhammer Betting"
    };

    public static final String[] GOALS_WORDS = {"Aixam", "Arrinera", "Aspark", "BAC", "Bitter", "Bizzarrini", "Bowler",
            "Brilliance", "Bufori", "Caterham", "Chatenet", "Cisitalia", "Donkervoort", "Eagle", "Faraday Future",
            "Fornasari", "Ginetta", "Gumpert", "Hispano Suiza", "Hommell", "Joss", "Karma", "KTM",
            "Laraki", "Lucid", "Mastretta", "Melkus", "Morgan", "Mosler", "Noble", "Panoz", "PGO", "Qoros", "Rimac",
            "Rinspeed", "Rossion", "Saleen", "Scuderia Cameron Glickenhaus", "Shelby SuperCars (SSC)", "Sin Cars",
            "Spania GTA", "Spyker", "Techrules", "Tramontana", "Tushek", "Vencer", "Venturi", "Wiesmann", "Zagato"
    };

    public static final String[] MESSENGERS_WORDS = {"WhatsApp", "Facebook Messenger", "WeChat", "Viber", "Telegram",
            "Line", "Signal", "Snapchat", "Kik", "iMessage", "Google Hangouts", "Skype", "Discord", "Slack", "Threema",
            "Wire", "GroupMe", "Zalo", "Tango", "BBM(BlackBerry Messenger)", "Hike", "IMO", "KakaoTalk", "Nimbuzz",
            "Wickr", "Yammer", "Chomp SMS", "Messenger Lite", "Google Messages", "Chare", "ICQ", "Tox", "Element(Riot)",
            "Jabber(XMPP)", "JioChat", "Yandex.Messenger", "Voxer", "VoIP", "Kaizala", "RumbleTalk", "Dust",
            "Trillian", "Paltalk", "Mattermost", "Microsoft Teams", "Cisco Webex Teams", "Flock", "Spike", "Troop Messenger"
    };

    public static String generateUTC() {
        Random random = new Random();
        String sign = random.nextBoolean() ? "+" : "-";
        return "UTC" + sign + "0" + random.nextInt(10) + ":00";
    }

    public static Integer generateRandomNumber(int numberOfDigits) {
        Random random = new Random();
        int lowerBound = (int) Math.pow(10, numberOfDigits - 1);
        int upperBound = (int) Math.pow(10, numberOfDigits) - 1;
        return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

}
