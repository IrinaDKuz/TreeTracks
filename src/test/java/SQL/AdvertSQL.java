package SQL;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Helper.LeadTypes.getLeadTypeKeyFromValue;
import static SQL.DatabaseTest.sqlQuery;
import static SQL.DatabaseTest.sqlQueryList;

public class AdvertSQL {

    @Test
    public static String getRandomValueFromBD(String parameter, String tableName) throws Exception {
        String sqlRequest = "SELECT " + parameter + " from " + tableName + ";";
        List<String> emailList = sqlQueryList(sqlRequest, parameter);
        System.out.println(emailList.get(new Random().nextInt(emailList.size())));
        return emailList.get(new Random().nextInt(emailList.size()));
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