package SQL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static SQL.DatabaseTest.sqlQueryList;

public class OfferSQL {

    @Test
    public static void test() throws Exception {
    }

    public static int getRandomOffer() throws Exception {
        String sqlRequest = "SELECT id from offer;";
        List<String> list = sqlQueryList(sqlRequest, "id");
        return Integer.parseInt(list.get(new Random().nextInt(list.size())));
    }

}