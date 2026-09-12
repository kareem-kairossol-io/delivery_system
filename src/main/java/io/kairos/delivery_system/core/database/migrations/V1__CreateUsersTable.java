package io.kairos.delivery_system.core.database.migrations;

import io.kairos.delivery_system.core.database.Migration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class V1__CreateUsersTable implements Migration {

    @Override
    public String getName() {
        return "V1__CreateUsersTable";
    }

    @Override
    public void up(Connection connection) throws SQLException {
        String sql = """
            CREATE TABLE users (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                username VARCHAR(255) UNIQUE NOT NULL,
                email VARCHAR(255) UNIQUE NOT NULL,
                phone VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                role_id BIGINT NOT NULL,
                is_active TINYINT NOT NULL DEFAULT 1,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    @Override
    public void down(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE users");
        }
    }
}