package io.kairos.delivery_system.core.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface Migration {

    String getName();

    void up(Connection connection) throws SQLException;

    void down(Connection connection) throws SQLException;
}