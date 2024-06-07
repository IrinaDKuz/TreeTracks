package Helper;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoAndLang {

    public static String getLangRandomValue() {
        return getRandomValue(lang_map);
    }

    public static String getLangRandomKey() {
        return getRandomKey(lang_map);
    }

    public static String getGeoRandomValue() {
        return getRandomValue(geo_map);
    }

    public static String getGeoRandomKey() {
        return getRandomKey(geo_map);
    }

    public static String getRandomValue(Map<String, String> map) {
        return map.get(getRandomKey(map));
    }

    public static <K, V> K getKeyFromValue(V value, Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    public static String getRandomKey(Map<String, String> map) {
        // Получаем массив ключей
        Object[] keys = map.keySet().toArray();
        // Выбираем случайный ключ из массива ключей
        return keys[new Random().nextInt(keys.length)].toString();
    }

    public static String getRandomCurrency() {
        // Выбираем случайный ключ из массива ключей
        return currency[new Random().nextInt(currency.length)];
    }



    public static String[] currency = {"Usd", "Eur", "Rub"};

    // lang
    public static Map<String, String> lang_map = Stream.of(new String[][]{
            {"aa", "Afar"}, {
            "ab", "Abkhazian"}, {
            "ae", "Avestan"}, {
            "af", "Afrikaans"}, {
            "ak", "Akan"}, {
            "am", "Amharic"}, {
            "an", "Aragonese"}, {
            "ar", "Arabic"}, {
            "as", "Assamese"}, {
            "av", "Avaric"}, {
            "ay", "Aymara"}, {
            "az", "Azerbaijani"}, {
            "ba", "Bashkir"}, {
            "be", "Belarusian"}, {
            "bg", "Bulgarian"}, {
            "bh", "Bihari"}, {
            "bi", "Bislama"}, {
            "bm", "Bambara"}, {
            "bn", "Bengali (bangla)"}, {
            "bo", "Tibetan"}, {
            "br", "Breton"}, {
            "bs", "Bosnian"}, {
            "ca", "Catalan"}, {
            "ce", "Chechen"}, {
            "ch", "Chamorro"}, {
            "co", "Corsican"}, {
            "cr", "Cree"}, {
            "cs", "Czech"}, {
            "cu", "Old church slavonic, old bulgarian"}, {
            "cv", "Chuvash"}, {
            "cy", "Welsh"}, {
            "da", "Danish"}, {
            "de", "German"}, {
            "dv", "Divehi, dhivehi, maldivian"}, {
            "dz", "Dzongkha"}, {
            "ee", "Ewe"}, {
            "el", "Greek"}, {
            "en", "English"}, {
            "eo", "Esperanto"}, {
            "es", "Spanish"}, {
            "et", "Estonian"}, {
            "eu", "Basque"}, {
            "fa", "Persian (farsi)"}, {
            "ff", "Fula, fulah, pulaar, pular"}, {
            "fi", "Finnish"}, {
            "fj", "Fijian"}, {
            "fl", "Philippines"}, {
            "fo", "Faroese"}, {
            "fr", "French"}, {
            "fy", "Western frisian"}, {
            "ga", "Irish"}, {
            "gd", "Gaelic (scottish)"}, {
            "gl", "Galician"}, {
            "gn", "Guarani"}, {
            "gu", "Gujarati"}, {
            "gv", "Gaelic (manx)"}, {
            "ha", "Hausa"}, {
            "he", "Hebrew"}, {
            "hi", "Hindi"}, {
            "ho", "Hiri motu"}, {
            "hr", "Croatian"}, {
            "ht", "Haitian creole"}, {
            "hu", "Hungarian"}, {
            "hy", "Armenian"}, {
            "hz", "Herero"}, {
            "ia", "Interlingua"}, {
            "ie", "Interlingue"}, {
            "ig", "Igbo"}, {
            "ii", "Sichuan yi"}, {
            "ik", "Inupiak"}, {
            "id", "Indonesian id,"}, {
            "io", "Ido"}, {
            "is", "Icelandic"}, {
            "it", "Italian"}, {
            "iu", "Inuktitut"}, {
            "ja", "Japanese"}, {
            "ji", "Yiddish yi,"}, {
            "jv", "Javanese"}, {
            "ka", "Georgian"}, {
            "kg", "Kongo"}, {
            "ki", "Kikuyu"}, {
            "kj", "Kwanyama"}, {
            "kk", "Kazakh"}, {
            "kl", "Kalaallisut, greenlandic"}, {
            "km", "Khmer"}, {
            "kn", "Kannada"}, {
            "ko", "Korean"}, {
            "kr", "Kanuri"}, {
            "ks", "Kashmiri"}, {
            "ku", "Kurdish"}, {
            "kv", "Komi"}, {
            "kw", "Cornish"}, {
            "ky", "Kyrgyz"}, {
            "la", "Latin"}, {
            "lb", "Luxembourgish"}, {
            "lg", "Luganda, ganda"}, {
            "li", "Limburgish ( limburger)"}, {
            "ln", "Lingala"}, {
            "lo", "Lao"}, {
            "lt", "Lithuanian"}, {
            "lu", "Luga-katanga"}, {
            "lv", "Latvian (lettish)"}, {
            "mg", "Malagasy"}, {
            "mh", "Marshallese"}, {
            "mi", "Maori"}, {
            "mk", "Macedonian"}, {
            "ml", "Malayalam"}, {
            "mn", "Mongolian"}, {
            "mo", "Moldavian"}, {
            "mr", "Marathi"}, {
            "ms", "Malay"}, {
            "mt", "Maltese"}, {
            "my", "Burmese"}, {
            "na", "Nauru"}, {
            "nb", "Norwegian bokmål"}, {
            "nd", "Northern ndebele"}, {
            "ne", "Nepali"}, {
            "ng", "Ndonga"}, {
            "nl", "Dutch"}, {
            "nn", "Norwegian nynorsk"}, {
            "no", "Norwegian"}, {
            "nr", "Southern ndebele"}, {
            "nv", "Navajo"}, {
            "ny", "Chichewa, chewa, nyanja"}, {
            "oc", "Occitan"}, {
            "oj", "Ojibwe"}, {
            "om", "Oromo (afaan oromo)"}, {
            "or", "Oriya"}, {
            "os", "Ossetian"}, {
            "pa", "Punjabi (eastern)"}, {
            "pi", "Pāli"}, {
            "pl", "Polish"}, {
            "ps", "Pashto, pushto"}, {
            "pt", "Portuguese"}, {
            "qu", "Quechua"}, {
            "rm", "Romansh"}, {
            "rn", "Kirundi"}, {
            "ro", "Romanian"}, {
            "ru", "Russian"}, {
            "rw", "Kinyarwanda (rwanda)"}, {
            "sa", "Sanskrit"}, {
            "sd", "Sindhi"}, {
            "se", "Sami"}, {
            "sg", "Sango"}, {
            "sh", "Serbo-croatian"}, {
            "si", "Sinhalese"}, {
            "sk", "Slovak"}, {
            "sl", "Slovenian"}, {
            "sm", "Samoan"}, {
            "sn", "Shona"}, {
            "so", "Somali"}, {
            "sq", "Albanian"}, {
            "sr", "Serbian"}, {
            "ss", "Siswati, swati"}, {
            "st", "Sesotho"}, {
            "su", "Sundanese"}, {
            "sv", "Swedish"}, {
            "sw", "Swahili (kiswahili)"}, {
            "ta", "Tamil"}, {
            "te", "Telugu"}, {
            "tg", "Tajik"}, {
            "th", "Thai"}, {
            "ti", "Tigrinya"}, {
            "tk", "Turkmen"}, {
            "tl", "Tagalog"}, {
            "tn", "Setswana"}, {
            "to", "Tonga"}, {
            "tr", "Turkish"}, {
            "ts", "Tsonga"}, {
            "tt", "Tatar"}, {
            "tw", "Twi"}, {
            "ty", "Tahitian"}, {
            "ug", "Uyghur"}, {
            "uk", "Ukrainian"}, {
            "ur", "Urdu"}, {
            "uz", "Uzbek"}, {
            "ve", "Venda"}, {
            "vi", "Vietnamese"}, {
            "vo", "Volapük"}, {
            "wa", "Wallon"}, {
            "wo", "Wolof"}, {
            "xh", "Xhosa"}, {
            "yo", "Yoruba"}, {
            "za", "Zhuang, chuang"}, {
            "zh", "Chinese"}, {
            "ns", "Chinese (Simplified)"}, {
            "nt", "Chinese (Traditional)"}, {
            "zu", "Zulu"}
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));


    public static Map<String, String> geo_map = Stream.of(new String[][]

            {
                    {"all", "All"},
                    {"af", "Afghanistan"},
                    {"al", "Albania"},
                    {"dz", "Algeria"},
                    {"ad", "Andorra"},
                    {"ao", "Angola"}, {
                    "ag", "Antigua and barbuda"}, {
                    "ar", "Argentina"}, {
                    "am", "Armenia"}, {
                    "au", "Australia"}, {
                    "at", "Austria"}, {
                    "az", "Azerbaijan"}, {
                    "bs", "Bahamas"}, {
                    "bh", "Bahrain"}, {
                    "bd", "Bangladesh"}, {
                    "bb", "Barbados"}, {
                    "by", "Belarus"}, {
                    "be", "Belgium"}, {
                    "bz", "Belize"}, {
                    "bj", "Benin"}, {
                    "bt", "Bhutan"}, {
                    "bo", "Bolivia"}, {
                    "ba", "Bosnia and herzegovina"}, {
                    "bw", "Botswana"}, {
                    "br", "Brazil"}, {
                    "bn", "Brunei"}, {
                    "bg", "Bulgaria"}, {
                    "bf", "Burkina faso"}, {
                    "bi", "Burundi"}, {
                    "ci", "Cote divoire"}, {
                    "cv", "Cabo verde"}, {
                    "kh", "Cambodia"}, {
                    "cm", "Cameroon"}, {
                    "ca", "Canada"}, {
                    "cf", "Central african republic"}, {
                    "td", "Chad"}, {
                    "cl", "Chile"}, {
                    "cn", "China"}, {
                    "co", "Colombia"}, {
                    "km", "Comoros"}, {
                    "cr", "Costa rica"}, {
                    "hr", "Croatia"}, {
                    "cu", "Cuba"}, {
                    "cy", "Cyprus"}, {
                    "cz", "Czechia"}, {
                    "cd", "Democratic republic of the congo"}, {
                    "dk", "Denmark"}, {
                    "dj", "Djibouti"}, {
                    "dm", "Dominica"}, {
                    "do", "Dominican republic"}, {
                    "ec", "Ecuador"}, {
                    "eg", "Egypt"}, {
                    "sv", "El salvador"}, {
                    "gq", "Equatorial guinea"}, {
                    "er", "Eritrea"}, {
                    "ee", "Estonia"}, {
                    "sz", "Eswatini"}, {
                    "et", "Ethiopia"}, {
                    "fj", "Fiji"}, {
                    "fi", "Finland"}, {
                    "fr", "France"}, {
                    "ga", "Gabon"}, {
                    "gm", "Gambia"}, {
                    "ge", "Georgia"}, {
                    "de", "Germany"}, {
                    "gh", "Ghana"}, {
                    "gr", "Greece"}, {
                    "gd", "Grenada"}, {
                    "gt", "Guatemala"}, {
                    "gn", "Guinea"}, {
                    "gw", "Guinea bissau"}, {
                    "gf", "Guyana"}, {
                    "ht", "Haiti"}, {
                    "va", "Holy see"}, {
                    "hn", "Honduras"}, {
                    "hk", "Hongkong"}, {
                    "hu", "Hungary"}, {
                    "is", "Iceland"}, {
                    "in", "India"}, {
                    "id", "Indonesia"}, {
                    "ir", "Iran"}, {
                    "iq", "Iraq"}, {
                    "ie", "Ireland"}, {
                    "il", "Israel"}, {
                    "it", "Italy"}, {
                    "jm", "Jamaica"}, {
                    "jp", "Japan"}, {
                    "jo", "Jordan"}, {
                    "kz", "Kazakhstan"}, {
                    "ke", "Kenya"}, {
                    "ki", "Kiribati"}, {
                    "kw", "Kuwait"}, {
                    "kg", "Kyrgyzstan"}, {
                    "la", "Laos"}, {
                    "lv", "Latvia"}, {
                    "lb", "Lebanon"}, {
                    "ls", "Lesotho"}, {
                    "lr", "Liberia"}, {
                    "ly", "Libya"}, {
                    "li", "Liechtenstein"}, {
                    "lt", "Lithuania"}, {
                    "lu", "Luxembourg"}, {
                    "mg", "Madagascar"}, {
                    "mw", "Malawi"}, {
                    "my", "Malaysia"}, {
                    "mv", "Maldives"}, {
                    "ml", "Mali"}, {
                    "mt", "Malta"}, {
                    "mh", "Marshall islands"}, {
                    "mr", "Mauritania"}, {
                    "mu", "Mauritius"}, {
                    "mx", "Mexico"}, {
                    "fm", "Micronesia"}, {
                    "md", "Moldova"}, {
                    "mc", "Monaco"}, {
                    "mn", "Mongolia"}, {
                    "me", "Montenegro"}, {
                    "ma", "Morocco"}, {
                    "mz", "Mozambique"}, {
                    "mm", "Myanmar"}, {
                    "na", "Namibia"}, {
                    "nr", "Nauru"}, {
                    "np", "Nepal"}, {
                    "nl", "Netherlands"}, {
                    "nz", "New zealand"}, {
                    "ni", "Nicaragua"}, {
                    "ne", "Niger"}, {
                    "ng", "Nigeria"}, {
                    "kp", "North korea"}, {
                    "mk", "North macedonia"}, {
                    "no", "Norway"}, {
                    "om", "Oman"}, {
                    "pk", "Pakistan"}, {
                    "pw", "Palau"}, {
                    "ps", "Palestine state"}, {
                    "pa", "Panama"}, {
                    "pg", "Papua new guinea"}, {
                    "py", "Paraguay"}, {
                    "pe", "Peru"}, {
                    "ph", "Philippines"}, {
                    "pl", "Poland"}, {
                    "pt", "Portugal"}, {
                    "qa", "Qatar"}, {
                    "ro", "Romania"}, {
                    "ru", "Russia"}, {
                    "rw", "Rwanda"}, {
                    "kn", "Saint kitts and nevis"}, {
                    "lc", "Saint lucia"}, {
                    "vc", "Saint vincent and the grenadines"}, {
                    "ws", "Samoa"}, {
                    "sm", "San marino"}, {
                    "st", "Sao tome and principe"}, {
                    "sa", "Saudi arabia"}, {
                    "sn", "Senegal"}, {
                    "rs", "Serbia"}, {
                    "sc", "Seychelles"}, {
                    "sl", "Sierra leone"}, {
                    "sg", "Singapore"}, {
                    "sk", "Slovakia"}, {
                    "si", "Slovenia"}, {
                    "sb", "Solomon islands"}, {
                    "so", "Somalia"}, {
                    "za", "South africa"}, {
                    "kr", "South korea"}, {
                    "ss", "South sudan"}, {
                    "es", "Spain"}, {
                    "lk", "Sri lanka"}, {
                    "sd", "Sudan"}, {
                    "sr", "Suriname"}, {
                    "se", "Sweden"}, {
                    "ch", "Switzerland"}, {
                    "sy", "Syria"}, {
                    "tj", "Tajikistan"}, {
                    "tz", "Tanzania"}, {
                    "th", "Thailand"}, {
                    "tl", "Timor leste"}, {
                    "tg", "Togo"}, {
                    "to", "Tonga"}, {
                    "tt", "Trinidad and tobago"}, {
                    "tn", "Tunisia"}, {
                    "tr", "Turkey"}, {
                    "tm", "Turkmenistan"}, {
                    "tv", "Tuvalu"}, {
                    "ug", "Uganda"}, {
                    "ua", "Ukraine"}, {
                    "ae", "United arab emirates"}, {
                    "gb", "United kingdom"}, {
                    "us", "United states of america"}, {
                    "uy", "Uruguay"}, {
                    "uz", "Uzbekistan"}, {
                    "vu", "Vanuatu"}, {
                    "ve", "Venezuela"}, {
                    "vn", "Vietnam"}, {
                    "ye", "Yemen"}, {
                    "zm", "Zambia"}, {
                    "zw", "Zimbabwe"}
            }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

}
