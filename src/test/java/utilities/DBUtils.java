package utilities;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBUtils {

    private static final String DB_PATH = "src/main/resources/testdb/testdata.db";

    private DBUtils() {
        // static-access only
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    private static String requiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable '" + key + "' is not set. "
                            + "Set it in your shell or IDE run configuration before running tests.");
        }
        return value;
    }

    private static String buildJdbcURL() {
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "3306");
        String database = env("DB_NAME", "testdb");
        return "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    private static Connection connect() throws SQLException {
        String user = requiredEnv("DB_USER");
        String password = requiredEnv("DB_PASSWORD");
        return DriverManager.getConnection(buildJdbcURL(), user, password);
    }

    public static void verifyDatabaseReady() {
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement("SELECT 1")) {
            ps.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed. Please ensure the database is running and accessible.", e);
        }
    }

    /** Fetches the expected price for a single product from the DB. */
    public static BigDecimal getExpectedPrice(String productName) {
        String sql = "SELECT price FROM products WHERE name = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return BigDecimal.valueOf(rs.getDouble("price")).setScale(2, java.math.RoundingMode.HALF_UP);
                }
                throw new IllegalArgumentException("No expected price found for product: " + productName);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed for product: " + productName, e);
        }
    }

    /**
     * Sums expected prices for a list of products directly in SQL (rather than
     * summing in Java) to demonstrate aggregate query usage.
     */
    public static BigDecimal getExpectedTotal(List<String> productNames) {
        if (productNames == null || productNames.isEmpty()) {
            return BigDecimal.ZERO.setScale(2);
        }
        String placeholders = String.join(",", productNames.stream().map(n -> "?").toList());
        String sql = "SELECT SUM(price) AS total FROM products WHERE name IN (" + placeholders + ")";

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < productNames.size(); i++) {
                ps.setString(i + 1, productNames.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return BigDecimal.valueOf(rs.getDouble("total")).setScale(2, java.math.RoundingMode.HALF_UP);
                }
                return BigDecimal.ZERO.setScale(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB aggregate query failed for products: " + productNames, e);
        }
    }
}
