package SQL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static SQL.DatabaseTest.sqlQueryList;

public class AdminSQL {

    @Test
    public static void test() throws Exception {
        System.out.println(getPermissionsFromBD(1, "advert_contact").getFirst());
        //getRequisitesFromBDPaymentSystem(57);
    }
    public static List<String> getPermissionsFromBD(int adminId, String permission) throws Exception {
        String sqlRequest = "SELECT type from permission WHERE admin_id = '" + adminId + "' AND " +
                "object =  '" + permission + "' ;";
        List<String> list = sqlQueryList(sqlRequest, "type");
        return list;
    }
}