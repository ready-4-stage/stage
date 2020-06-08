package stage.server.database;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * // TODO description
 *
 * @author Julian Drees
 * @since 1.0.0
 */
@Service
@Log4j2
public class SqlConnection {

    private Connection connection;

    @PostConstruct
    public void openConnection() throws SQLException {
        if (!this.isConnected()) {
            log.info("trying to establish connection to database");
            try {
                Class.forName("org.sqlite.JDBC");
                this.connection = DriverManager.getConnection("jdbc:sqlite://DB");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @PreDestroy
    public void closeConnection() throws SQLException {
        if (this.isConnected()) {
            this.connection.close();
        }
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    /**
     * Creates a {@link PreparedStatement}. Should be used in try-with-resources to close
     * the statement after execution.
     *
     * @param query      sql query
     * @param parameters sql parameters (varargs or array)
     * @return an executable statement
     */
    public PreparedStatement prepareStatement(String query, Object... parameters) throws SQLException {
        PreparedStatement ps = this.connection.prepareStatement(query);
        for (int i = 0; i < parameters.length; i++) {
            ps.setObject(i + 1, parameters[i]);
        }
        return ps;
    }

    public void update(String query, Object... parameters) throws SQLException {
        try (PreparedStatement ps = this.prepareStatement(query, parameters)) {
            ps.executeUpdate();
        }
    }

    public ResultSet result(String query, Object... parameters) throws SQLException {
        return this.prepareStatement(query, parameters).executeQuery();
    }

}
