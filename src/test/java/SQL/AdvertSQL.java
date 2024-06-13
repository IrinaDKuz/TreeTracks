package SQL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.*;

import static SQL.DatabaseTest.sqlQueryList;

public class AdvertSQL {

    @Test
    public static void test() throws Exception {
        getValueFromBDWhere("title", "payment_system", "id", "57");
        //getRequisitesFromBDPaymentSystem(57);
    }

    public static String getRandomValueFromBD(String parameter, String tableName) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName + ";";
        List<String> emailList = sqlQueryList(sqlRequest, parameter);
        System.out.println(emailList.get(new Random().nextInt(emailList.size())));
        return emailList.get(new Random().nextInt(emailList.size()));
    }

    public static String getValueFromBDWhere(String parameter, String tableName, String where, String whereValue ) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName  +
                " WHERE " + where + " = " + whereValue + " ;";
        return sqlQueryList(sqlRequest, parameter).getFirst();
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