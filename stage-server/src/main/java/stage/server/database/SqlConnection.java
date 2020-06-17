package stage.server.database;

import java.sql.*;
import javax.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.log4j.Log4j2;

import static stage.common.FileUtil.readFile;

/**
 * The {@link SqlConnection} provides access to the database by providing an SQL
 * connection.
 * <p>
 * In order to properly run this class, there must be the following properties
 * set either via VM arguments or via an entry in <code>application.yml</code>:
 * <ul>
 *     <li><code>stage.db.user=USER</code> The username to use to connect to the database.</li>
 *     <li><code>stage.db.pwd=PASSWORD</code> The password to use to connect to the database.</li>
 *     <li><code>jdbc=JDBC</code> The JDBC string to use to connect to the database.</li>
 * </ul>
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Log4j2
@Configuration
public class SqlConnection {
    private final String initialFill;

    private Connection connection;

    @Value("${stage.db.user}")
    private String user;

    @Value("${stage.db.pwd}")
    private String password;

    @Value("${jdbc}")
    private String jdbc;

    public SqlConnection() {
        initialFill = readFile("sql/startup.sql");
    }

    @PostConstruct
    public void openConnection() throws SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection(jdbc, user, password);
            connection.setAutoCommit(false);

            update(initialFill);
            commit();
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            if (isConnected()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Unable to close connection: {}", e.getMessage());
        }
    }

    /**
     * @return <code>true</code>, if the connection is open. Otherwise
     * <code>false</code>.
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Commits all changes.
     *
     * @throws SQLException if an database error occurs.
     */
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
     * @throws SQLException if an database error occurs.
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

    /**
     * Performs a SQL prepared statement.
     *
     * @param query      The query t perform.
     * @param parameters The parameters to put into the query (in order).
     * @throws SQLException if an database error occurs.
     */
    public void update(String query, Object... parameters) throws SQLException {
        try (PreparedStatement ps = prepareStatement(query, parameters)) {
            ps.executeUpdate();
        }
    }

    /**
     * Performs a SQL prepared query statement.
     *
     * @param query      The query t perform.
     * @param parameters The parameters to put into the query (in order).
     * @return The {@link ResultSet} of the query.
     * @throws SQLException if an database error occurs.
     */
    public ResultSet result(String query, Object... parameters)
        throws SQLException {
        return prepareStatement(query, parameters).executeQuery();
    }
}
