package com.rafael.vr.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {

    private static final String URL = "jdbc:mysql://avnadmin:AVNS_TUVI26Mf0tt172UN9Fo@rafael-vrsoftware-gecan.e.aivencloud.com:14590/sales?ssl-mode=REQUIRED";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "AVNS_TUVI26Mf0tt172UN9Fo";
    private static final String CERTIFICATE = "certificates/ca.pem";

    public static Connection getConnection() {
        try {
            System.setProperty("javax.net.ssl.trustStore", CERTIFICATE);
            System.setProperty("javax.net.ssl.trustStoreType", "PEM");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }


}
