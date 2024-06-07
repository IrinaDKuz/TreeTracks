package SQL;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static Helper.ActionsClass.getFutureOrPastDateTimeSQL;
import static SQL.DatabaseTest.*;

public class AffiliateSQL {

    public static long getCount(String table, int affId) throws Exception {
        String parameter = "COUNT(*)";
        String sqlRequest = "SELECT " + parameter + " FROM " + table +
                " WHERE affiliate_id = " + affId + ";";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
    }

    public static long getOfferCount() throws Exception {
        String parameter = "COUNT(*)";
        String sqlRequest = "SELECT " + parameter + " FROM offer " +
                "WHERE google_import = 0 " +
                "AND deleted_at IS NULL;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
    }

    public static long getPostbackCount(int affId) throws Exception {
        return getCount("affiliate_postback", affId);
    }

    public static long getGICount(int affId) throws Exception {
        return getCount("google_import", affId);
    }

    public static long getLeadsCount(int affId) throws Exception {
        return getCount("lead_import", affId);
    }

    public static long getTransactionCount(int affId) throws Exception {
        return getCount("transaction", affId);
    }


    public static long getStatisticClickCount(int affId) throws Exception {
        String parameter = "COUNT(*)";
        String sqlRequest = "SELECT " + parameter + " FROM lead_import " +
                "WHERE affiliate_id = " + affId +
                " AND type = 'web' " +
                " AND created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND created_at < '" + getFutureOrPastDateTimeSQL(+1) + "';";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
    }

    public static long getStatisticOfferCount(int affId) throws Exception {
        String parameter = "COUNT(offer_id)";
        String sqlRequest = "SELECT " + parameter + " FROM ( SELECT offer_id FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' GROUP BY offer_id) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameter))).get(parameter);
    }

    public static long getStatisticLandCount(int affId, String parameter) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT " + parameter + " FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND lead_import.type = 'web' " +
                " AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' GROUP BY " + parameter + " ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }

    public static long getStatisticGeoCount(int affId, String parameter) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT " + parameter + " FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' GROUP BY " + parameter + " ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }

    public static List<Map<String, Object>> getOffersData() throws SQLException {
        List<String> parameters = Arrays.asList("id", "title", "lang", "short_description");
        String sqlRequest = "SELECT id, title, lang, short_description FROM offer " +
                "WHERE google_import = 0 " +
                "AND deleted_at IS NULL;";
        return sqlQueryNew(sqlRequest, parameters);
    }


    public static List<String> getClickIdList(int affId) throws Exception {
        String parameter = "click_id";
        String sqlRequest = "SELECT " + parameter + " FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' AND " + parameter + " IS NOT NULL " +
                " GROUP BY " + parameter + ";";

        return sqlQueryList(sqlRequest, parameter);
    }

    public static long getCountByDay(int affId, String parameter, String date) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT " + parameter + " FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(date, 0) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(date, +1) +
                "GROUP BY " + parameter +
                "' ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }


    public static long getCountByWeek(int affId, String parameter, String byParameter, String byParameterValue) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT " + parameter + " FROM lead_import" +
                " JOIN `lead` ON lead_import.lead_id = `lead`.id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND " + byParameter + " = '" + byParameterValue +
                "' AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' GROUP BY " + parameter +
                " ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }

    public static long getConversionCountByDay(String status, int affId, String parameter, String date) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT lead_import." + parameter + " FROM lead_import" +
                " JOIN conversion ON lead_import.lead_id = conversion.lead_id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND status = '" + status +
                "' AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(date, 0) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(date, +1) +
                "GROUP BY " + parameter +
                "' ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }

    public static BigDecimal getConversionAmountByDay(String status, int affId, String date) throws Exception {
        String parameterCount = "SUM(amount)";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT amount FROM lead_import" +
                " JOIN conversion ON lead_import.lead_id = conversion.lead_id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND status = '" + status +
                "' AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(date, 0) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(date, +1) +
                "' ) AS subquery;";

        Object value = sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
        return (value != null) ? ((BigDecimal) value).stripTrailingZeros() : BigDecimal.valueOf(0.0);
    }


    public static long getConversionCountByWeek(String status, int affId, String parameter,
                                                String byParameter, String byParameterValue) throws Exception {
        String parameterCount = "COUNT(" + parameter + ")";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT lead_import." + parameter + " FROM lead_import" +
                " JOIN conversion ON lead_import.lead_id = conversion.lead_id " +
                " JOIN `lead` ON lead.id = conversion.lead_id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND conversion.status = '" + status +
                "' AND " + byParameter + " = '" + byParameterValue +
                "' AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' GROUP BY " + parameter +
                " ) AS subquery;";
        return (long) sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
    }

    public static BigDecimal getConversionAmountByWeek(String status, int affId,
                                                       String byParameter, String byParameterValue) throws Exception {
        String parameterCount = "SUM(amount)";
        String sqlRequest = "SELECT " + parameterCount + " FROM ( SELECT amount FROM lead_import" +
                " JOIN conversion ON lead_import.lead_id = conversion.lead_id " +
                " JOIN `lead` ON lead.id = conversion.lead_id " +
                " WHERE lead_import.affiliate_id = " + affId +
                " AND conversion.status = '" + status +
                "' AND " + byParameter + " = '" + byParameterValue +
                "' AND lead_import.created_at > '" + getFutureOrPastDateTimeSQL(-7) +
                "' AND lead_import.created_at < '" + getFutureOrPastDateTimeSQL(+1) +
                "' ) AS subquery;";
        Object value = sqlQuery(sqlRequest, new ArrayList<>(List.of(parameterCount))).get(parameterCount);
        return (value != null) ? ((BigDecimal) value).stripTrailingZeros() : BigDecimal.valueOf(0.0);
    }

    public static String getLandNameById(int affId) throws SQLException {
        String sqlRequest = "SELECT name FROM land" +
                " WHERE id = " + affId + ";";
        Object value = sqlQueryList(sqlRequest, "name").getFirst();
        return  value.toString();
    }
}
