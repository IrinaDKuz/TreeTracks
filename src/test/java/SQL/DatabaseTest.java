package SQL;

import org.testng.annotations.Test;

import java.sql.*;
import java.util.*;

public class DatabaseTest {

    static String URL_NEWX_DEV = "jdbc:mysql://213.166.69.125:3306/newx_dev";
    static String URL_DEV = "jdbc:mysql://dev:DsHYtPp2V3UHQyNkti1e5ctj1CmCBMGF@159.69.222.11:3306/dev";


    @Test
    public void testSql() throws Exception {


        // Определение SQL-запроса
        String sql = "SELECT description FROM offer WHERE id = 7";
        List<String> param = new ArrayList<>();
        param.add("description");
        //param.add("value1");

        // Выполнение SQL-запроса и вывод результата
        Map<String, Object> stringMap = sqlQuery(sql, param);
        for (Map.Entry<String, Object> entry : stringMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }


        // Подготовка данных для тестирования с использованием DBUnit
        // IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("path/to/expectedDataSet.xml"));
        //IDatabaseConnection connection = new DatabaseConnection(DriverManager.getConnection(url, username, password));
        //connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

        // Получение данных из базы данных с использованием DBUnit
        //IDataSet databaseDataSet = connection.createDataSet();
        //ITable actualTable = databaseDataSet.getTable("offer");

        // Сравнение данных из базы данных с ожидаемыми данными
        //Assertion.assertEquals(expectedDataSet.getTable("offer"), actualTable);
    }

    public static List<Map<String, Object>> sqlQueryNew(String sql, List<String> parameters) throws SQLException {
        String url = "jdbc:mysql://213.166.69.125:3306/apileads_dev";
        String username = "irinakuznetsova";
        String password = "MXahAZpkGFHC5VbfT7Usr853pjQcGpj4";

        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> parameterValueMap = new HashMap<>();
                    for (String parameter : parameters) {
                        Object value = resultSet.getObject(parameter);
                        parameterValueMap.put(parameter, value);
                    }
                    resultList.add(parameterValueMap);
                }
            }
        }
        return resultList;
    }

    public static List<String> sqlQueryList(String sql, String parameter) throws SQLException {
        String url = URL_DEV;
        String username = "irinakuznetsova";
        String password = "MXahAZpkGFHC5VbfT7Usr853pjQcGpj4";

        List<String> resultList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Object value = resultSet.getObject(parameter);
                    resultList.add(String.valueOf(value));
                }
            }
        }
        return resultList;
    }


    public static Map<String, Object> sqlQuery(String sql, List<String> parameters) throws SQLException {
        String url = "jdbc:mysql://213.166.69.125:3306/apileads_dev";
        String username = "irinakuznetsova";
        String password = "MXahAZpkGFHC5VbfT7Usr853pjQcGpj4";

        Map<String, Object> parameterValueMap = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    for (String parameter : parameters) {
                        Object value = resultSet.getObject(parameter);
                        parameterValueMap.put(parameter, value);
                    }
                }
            }
        }
        return parameterValueMap;
    }
}

