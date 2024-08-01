package com.currencyexchangeweb;

import com.currencyexchangeweb.model.Currency;
import com.currencyexchangeweb.repository.CurrencyRepository;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        CurrencyRepository repository = CurrencyRepository.getInstance();
        String str = "USDRUB";
        System.out.println(str.substring(0,3) + " " + str.substring(3, 6));
        System.out.println(repository.getCurrencyIdByCode("USD"));
    }


//    private final static String URL = "jdbc:sqlite:C:\\Users\\1\\IdeaProjects\\CurrencyExchange\\bin";
//
//    public static void main(String[] args) throws SQLException {
////        CurrencyDTO currency = new CurrencyDTO(1, "EUR", "Euro", "E");
////        CurrencyDTO currency2 = new CurrencyDTO();
////        System.out.println(Optional.of(currency));
////        System.out.println(Optional.of(currency2));
//        try {
//            Class.forName("org.sqlite.JDBC");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        String query = "SELECT * FROM Currencies WHERE code = ?";
//        Connection connection = connect();
//        Currency currency = new Currency();
//        PreparedStatement prst = connection.prepareStatement(query);
//        prst.setString(1, "USD");
//        ResultSet rs = prst.executeQuery();
//        if (!rs.next()) {
//            System.out.println("нет");
//        } else {
//            System.out.println(rs.getString("code"));
//        }
//        currency.setId(rs.getInt("id"));
//        currency.setCode(rs.getString("code"));
//        currency.setName(rs.getString("name"));
//        currency.setSign(rs.getString("sign"));
//        connection.close();
//
//    }
//
//    public static Connection connect() {
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(URL);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return connection;
//    }
}
