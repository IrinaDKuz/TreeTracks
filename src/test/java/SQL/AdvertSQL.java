package SQL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static SQL.DatabaseTest.sqlQueryList;
import static java.sql.DriverManager.getConnection;

public class AdvertSQL {

    @Test
    public static void test() throws Exception {
        System.out.println(isInDatabaseWhere("id", "65", "category",
                "lang", "'general'"));

       // getValueFromBDWhere("title", "payment_system", "id", "57");
        //getRequisitesFromBDPaymentSystem(57);
    }

    public static String getRandomValueFromBD(String parameter, String tableName) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName + ";";
        List<String> list = sqlQueryList(sqlRequest, parameter);
        return list.get(new Random().nextInt(list.size()));
    }

    public static boolean isInDatabase(String parameter, String value, String tableName) {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + parameter + " = " + value + " ;";
        try {
            sqlQueryList(sqlRequest, parameter);
            return !sqlQueryList(sqlRequest, parameter).isEmpty();
        } catch (SQLException sqlException) {
            return false;
        }
    }

    public static boolean isInDatabaseWhere(String parameter, String value,
                                            String tableName, String where, String whereValue ) {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + parameter + " = " + value +
                " AND " + where + " = " + whereValue + " ;";
        try {
            sqlQueryList(sqlRequest, parameter);
            return !sqlQueryList(sqlRequest, parameter).isEmpty();
        } catch (SQLException sqlException) {
            return false;
        }
    }


    public static String getValueFromBDWhere(String parameter, String tableName, String where, String whereValue) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " = '" + whereValue + "' ;";
        return sqlQueryList(sqlRequest, parameter).getFirst();
    }

    public static List<String> getArrayFromBDWhere(String parameter, String tableName, String where, String whereValue) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " = '" + whereValue + "';";
        return sqlQueryList(sqlRequest, parameter);
    }

    public static List<String> getArrayFromBD(String parameter, String tableName) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName + ";";
        return sqlQueryList(sqlRequest, parameter);
    }

    public static List<String> getSomeValuesFromBD(String parameter, String tableName, int count) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName + ";";
        List<String> requestList = sqlQueryList(sqlRequest, parameter);
        Collections.shuffle(requestList);
        return requestList.subList(0, count);
    }

    public static List<String> getSomeValuesFromBDWhere(String parameter, String tableName,
                                                        String where, String whereValue, int count) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " = '" + whereValue + "';";
        List<String> requestList = sqlQueryList(sqlRequest, parameter);
        Collections.shuffle(requestList);
        return requestList.subList(0, count);
    }

    public static String getRandomValueFromBDWhere(String parameter, String tableName, String where, String whereValue) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " = '" + whereValue + "';";
        List<String> list = sqlQueryList(sqlRequest, parameter);
        return list.get(new Random().nextInt(list.size()));
    }


    public static String getRandomValueFromBDWhereNotNull(String parameter, String tableName, String where) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " IS NOT NULL AND " + where + " != '[]' ;";
        List<String> list = sqlQueryList(sqlRequest, parameter);
        return list.get(new Random().nextInt(list.size()));
    }


    public static String getRandomValueFromBDWhereMore(String parameter, String tableName, String where, String whereValue) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName +
                " WHERE " + where + " > " + whereValue + " ;";
        List<String> list = sqlQueryList(sqlRequest, parameter);
        return list.get(new Random().nextInt(list.size()));
    }

    public static String getRandomCurrencyFromBDPaymentSystem(int paymentSystemID) throws Exception {
        String sqlRequest = "SELECT currency from payment_system WHERE id = '" + paymentSystemID + "';";
        String currency = String.valueOf(sqlQueryList(sqlRequest, "currency"));
        currency = currency.replace("[[", "").replace("]]", "")
                .replace("\"", "");
        String[] currencies = currency.split(", ");
        String randomCurrency = currencies[new Random().nextInt(currencies.length)];
        return randomCurrency;
    }

    public static List<String> getRequisitesFromBDPaymentSystem(int paymentSystemID) throws Exception {
        String sqlRequest = "SELECT fields from payment_system WHERE id = '" + paymentSystemID + "';";
        String fieldsString = String.valueOf(sqlQueryList(sqlRequest, "fields").getFirst());

        List<Map<String, Object>> mapList;
        List<String> titles = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            mapList = objectMapper
                    .readValue(fieldsString, new TypeReference<>() {
                    });

            // Извлечение значений title и добавление их в список titles
            for (Map<String, Object> map : mapList) {
                if (map.containsKey("title")) {
                    titles.add((String) map.get("title"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titles;
    }

}


  /*  public static Double getBalanceFromDB(String brandTitle) throws Exception {
        String parameter = "balance_usd";
        String sqlRequest = "SELECT " + parameter + " from brand join advert a on advert_id = a.id" +
                " where brand.title  = '" + brandTitle + "';";

        BigDecimal decimalValue = (BigDecimal) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
        return decimalValue.doubleValue();
    }

    public static Double getHoldBalanceFromDB(String brandTitle) throws Exception {
        String parameter = "hold_balance_usd";
        String sqlRequest = "SELECT " + parameter + " from brand join advert a on advert_id = a.id" +
                " where brand.title  = '" + brandTitle + "';";

        BigDecimal decimalValue = (BigDecimal) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
        return decimalValue.doubleValue();
    }

    public static Double getLeadCostFromPaymentPolicy(AdvOrder advertOrder) throws Exception {
        return getLeadCostFromPaymentPolicy(advertOrder.getLang(), advertOrder.getGeo(),
                getLeadTypeKeyFromValue(advertOrder.getLeadType()),
                advertOrder.getPaymentType());
    }

    public static Double getLeadCostFromPaymentPolicy(String lang, String geo, int leadType, String paymentType) throws Exception {
        String SQLPaymentType = paymentType.toLowerCase().replace("+", "_");
        BigDecimal costDecimal;
        String parameter = "unique_cost";
        String sqlRequest = "SELECT unique_cost from payment_policy pp " +
                "WHERE pp.lead_type = " + leadType +
                " AND pp.lang LIKE '%" + lang + "%'" +
                " AND pp.geo LIKE '%" + geo + "%'" +
                " AND payment_model_type = '" + SQLPaymentType +
                "' AND `type` = 'base';";
        costDecimal = (BigDecimal) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
        if (costDecimal == null)
        {
            sqlRequest = "SELECT unique_cost from payment_policy pp " +
                    "WHERE pp.lead_type IS NULL " +
                    " AND payment_model_type = '" +  SQLPaymentType +
                    "' AND `type` = 'base';";

            costDecimal = (BigDecimal) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
        }
        return costDecimal.doubleValue();
    }
}
*/