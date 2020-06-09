package stage.server.database;

import javax.annotation.*;

import java.sql.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;

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

    @PostConstruct
    @SuppressWarnings("java:S2115")
    public void openConnection() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:h2:mem:");
            connection.setAutoCommit(false);
        }
    }

    @PreDestroy
    public void closeConnection() throws SQLException {
        if (isConnected()) {
            connection.close();
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
