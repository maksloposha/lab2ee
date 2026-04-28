package org.example.lab2ee.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class ConnectionPool {

    private static final String JNDI_NAME = "jdbc/FoodOrderingDS";

    private static DataSource dataSource;

    private ConnectionPool() {}

    private static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            try {
                InitialContext ctx = new InitialContext();
                dataSource = (DataSource) ctx.lookup(JNDI_NAME);
            } catch (NamingException e) {
                throw new RuntimeException(
                    "Не вдалось знайти DataSource за JNDI-ім'ям: " + JNDI_NAME +
                    ". Переконайтесь що JDBC Resource налаштований в GlassFish.", e);
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
