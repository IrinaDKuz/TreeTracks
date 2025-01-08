package Helper;

import java.util.HashMap;
import java.util.Map;

public class Affiliates {

    public static final String affiliateKey_3 = "8c0ab426d5aa728a9059108461a53074";
    public static final String affiliateKey_2 = "c8873dfc0c2f45a6325b9e85785a5c2c";
  //  public static final String affiliateKey_87 = "728c1d0cfa05c465e22f7e2489554a03";
   // public static final String affiliateKey_88 = "636ee2ecb1c98fb7a441ab620e26e567";
   // public static final String affiliateKey_90 = "720481de6ec3c633e96a6fb53c3ae24d";
  //  public static final String affiliateKey_95 = "bccea111d4fb548739c8218a738740d4";

    public final static Map<String, String> AFFILIATE_STATUS_MAP = new HashMap<>() {
        {
            put("active", "Active");
            put("not_active", "Not Active");
            put("banned", "Banned");
            put("on_moderation", "On moderation");
        }
    };

    public static final String[] ADDRESS_TERMS = {
            "City", "Street", "Avenue", "Boulevard", "Road", "Square", "Lane", "Park", "Drive", "Circle",
            "Terrace", "Court", "Place", "Alley", "District", "Area", "Block", "Highway", "Route", "Crescent",
            "Mews", "Parade", "Close", "Hill", "Gardens", "Walk", "Way", "Esplanade", "Quay", "Plaza",
            "10", "25", "36", "48", "59", "63", "72", "85", "90", "101",
            "123", "134", "147", "159", "162", "178", "183", "195", "209", "221",
            "234", "246", "258", "271", "283", "297", "305", "318", "329", "342",
            "Times Square", "Hollywood", "Silicon Valley", "Oxford Street", "Bond Street", "Wall Street", "Piccadilly Circus",
            "Fifth Avenue", "Champs-Élysées", "Broadway", "Las Ramblas", "Red Square", "Pike Place", "Abbey Road", "Fleet Street",
            "Via Veneto", "Rue de Rivoli", "Nevsky Prospect", "Kurfürstendamm", "Copacabana", "Broad Street", "Sunset Boulevard",
            "Michigan Avenue", "Park Avenue", "Royal Mile", "Bourbon Street", "King's Road", "Collins Street", "Ginza", "Ocean Drive"
    };




    public static Map<Integer, String> affiliatesKeys_map = new HashMap<Integer, String>() {{
        put(3, affiliateKey_3);
        put(2, affiliateKey_2);
      // put(87, affiliateKey_87);
      //  put(88, affiliateKey_88);
      //  put(90, affiliateKey_90);
      //  put(95, affiliateKey_95);
    }};
}
