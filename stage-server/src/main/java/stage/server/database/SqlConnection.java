package stage.server.database;

import java.sql.*;
import javax.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
@Log4j2
@Configuration
public class SqlConnection {
    private Connection connection;

    @Value("${stage.db.user}")
    private String user;

    @Value("${stage.db.pwd}")
    private String password;

    @Value("${jdbc}")
    private String jdbc;

    @PostConstruct
    public void openConnection() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection(jdbc, user, password);
            connection.setAutoCommit(false);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            log.error("Unable to close connection: {}", e.getMessage());
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Creates a {@link PreparedStatement}. Should be used in try-with-resources
     * to close the statement after execution.
     *
     * @param query      sql query
     * @param parameters sql parameters (varargs or array)
     * @return an executable statement
     */
    @SuppressWarnings("java:S2095") // We'll use try-with later on in the code
    public PreparedStatement prepareStatement(String query,
        Object... parameters) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }

    public void update(String query, Object... parameters) throws SQLException {
        try (PreparedStatement ps = prepareStatement(query, parameters)) {
            ps.executeUpdate();
        }
    }

    public ResultSet result(String query, Object... parameters)
        throws SQLException {
        return prepareStatement(query, parameters).executeQuery();
    }
}
