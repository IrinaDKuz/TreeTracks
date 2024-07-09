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
