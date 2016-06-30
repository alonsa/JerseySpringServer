//package com.alon.main.server.dao;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//import static com.alon.main.server.Const.Consts.JDBC_DRIVER;
//import static com.alon.main.server.Const.Consts.JDBC_PARAM;
//
///**
// * Created by alon_ss on 6/29/16.
// */
//@Service
//public class DaoMySqlImpl {
//  admins-MBP:bin alon_ss$ sudo mongod --dbpath /usr/local/Cellar/mongodb/data/db

//
//    @Value("${db.username}")
//    private String DB_USER_NAME = "root";
//
//    @Value("${db.password}")
//    private String DB_PASSWORD = "1234";
//
//    @Value("${db.connectionURL}")
//    private String JDBC_PLAIN_URL = "jdbc:mysql://localhost";
//
//    @Value("${db.name}")
//    private String DB_NAME = "MOVIES";
//
//    @Value("${db.table.name}")
//    private String TABLE_NAME = "MOVIE";
//
//    private String dbUrl;
//    private String dbTableUrl;
//
//
//    @PostConstruct
//    private void init(){
//        dbUrl = JDBC_PLAIN_URL + JDBC_PARAM;
//        dbTableUrl = JDBC_PLAIN_URL + "/" + DB_NAME + JDBC_PARAM;
//
//        testDbConnection();
//    }
//
//
//    private void testDbConnection() {
//
//        System.out.println("-------- MySQL JDBC Connection Testing ------------");
//
//        try {
//            Class.forName(JDBC_DRIVER);
//        } catch (ClassNotFoundException e) {
//            System.out.println("Where is your MySQL JDBC Driver?");
//            e.printStackTrace();
//            return;
//        }
//
//        System.out.println("MySQL JDBC Driver Registered!");
//        Connection connection;
//
//        connection = connectToDb();
//        if (connection == null){
//            connection = connectToDb();
//        }
//
//        if (connection != null) {
//            System.out.println("You made it, take control your database now!");
//        } else {
//            System.out.println("Failed to make connection!");
//        }
//        closeConnection(connection);
//    }
//
//    private Connection connectToDb() {
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(dbTableUrl, DB_USER_NAME, DB_PASSWORD);
//        } catch (SQLException e) {
//            System.out.println("Connection Failed! Check output console");
//            createDb();
//        }
//        return connection;
//    }
//
//    private void createDb(){
//        Connection dbConnection = null;
//        try {
//            dbConnection = DriverManager.getConnection(dbUrl, DB_USER_NAME, DB_PASSWORD);
//            Statement dbStmt = dbConnection.createStatement();
//            String createDbSql = "CREATE DATABASE "+ DB_NAME + " DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci";
//            dbStmt.executeUpdate(createDbSql);
//            System.out.println("Database created successfully...");
//
//            createTable();
//
//        } catch (SQLException sqlException) {
//            System.out.println("Connection Failed! Check output console");
//
//            sqlException.printStackTrace();
//
//        } finally {
//            closeConnection(dbConnection);
//        }
//    }
//
//    private void createTable(){
//        Connection connection = null;
//        try {
//            connection = DriverManager.getConnection(dbTableUrl, DB_USER_NAME, DB_PASSWORD);
//            Statement stmt = connection.createStatement();
//            String sql = "CREATE TABLE " + TABLE_NAME +
//                    "(id VARCHAR(45) not NULL, " +
//                    " uname VARCHAR(45), " +
//                    " msg VARCHAR(255), " +
//                    " date_created VARCHAR(45))";
//
//            stmt.executeUpdate(sql);
//            System.out.println("table created successfully...");
//        } catch (SQLException sqlException) {
//            System.out.println("Connection Failed! Check output console");
//            sqlException.printStackTrace();
//
//        } finally {
//            closeConnection(connection);
//        }
//    }
//
//    private static void closeConnection(Connection connection) {
//
//        if (connection != null){
//            try {
//                connection.close();
//            } catch (SQLException e1) {
//                e1.printStackTrace();
//            }
//        }
//    }
//}
//
//
