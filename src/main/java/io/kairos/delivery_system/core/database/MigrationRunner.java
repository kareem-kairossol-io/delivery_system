package io.kairos.delivery_system.core.database;

import java.sql.*;
import java.util.List;

public class MigrationRunner {

    private final DataSource dataSource;
    private final List<Migration> migrations;

    public MigrationRunner(
            DataSource dataSource,
            List<Migration> migrations
    ) {
        this.dataSource = dataSource;
        this.migrations = migrations;
    }

    public void migrate() throws SQLException {

        try (Connection connection = dataSource.getConnection()) {

            createMigrationsTable(connection);

            for (Migration migration : migrations) {

                if (!hasRun(connection, migration.getName())) {

                    migration.up(connection);

                    recordMigration(connection, migration.getName());
                }
            }
        }
    }

    private void createMigrationsTable(Connection connection)
            throws SQLException {

        String sql = """
            CREATE TABLE IF NOT EXISTS migrations (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) UNIQUE NOT NULL,
                executed_at TIMESTAMP NOT NULL
            )
            """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private boolean hasRun(
            Connection connection,
            String name
    ) throws SQLException {

        String sql = """
            SELECT 1
            FROM migrations
            WHERE name = ?
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void recordMigration(
            Connection connection,
            String name
    ) throws SQLException {

        String sql = """
            INSERT INTO migrations (name, executed_at)
            VALUES (?, CURRENT_TIMESTAMP)
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.executeUpdate();
        }
    }
}