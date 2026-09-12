package io.kairos.delivery_system.modules.users.repositories;

import io.kairos.delivery_system.core.database.DataSource;
import io.kairos.delivery_system.core.exceptions.DatabaseExeption;
import io.kairos.delivery_system.http.enums.ResponseCodesEnum;
import io.kairos.delivery_system.modules.users.contracts.UserRepository;
import io.kairos.delivery_system.modules.users.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository {
    private final DataSource dataSource;

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User findByEmail(String email) {
        String query = """
                SELECT * FROM users WHERE email = ?;
                """;

        try(
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseExeption(ResponseCodesEnum.INTERNAL_SERVER_ERROR, "Failed to get user", e);
        }
    }

    public User findByPhone(String phone) {
        String query = """
                SELECT * FROM users WHERE phone = ?;
                """;

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, phone);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseExeption(ResponseCodesEnum.INTERNAL_SERVER_ERROR, "Failed to get user", e);
        }
    }

    public User findByUsername(String username) {
        String query = """
                SELECT * FROM users WHERE username = ?;
                """;

        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUser(resultSet);
                }

                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseExeption(ResponseCodesEnum.INTERNAL_SERVER_ERROR, "Failed to get username", e);
        }
    }

    public User save(User user) {

        String query = """
            INSERT INTO users
                (name, username, email, password, phone, role_id, is_active)
            VALUES
                (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            ps.setLong(6, user.getRoleId());
            ps.setBoolean(7, user.isActive());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseExeption(ResponseCodesEnum.INTERNAL_SERVER_ERROR, "Failed to save user", e);
        }

        return user;
    }

    public User update(User user) {

        StringBuilder query = new StringBuilder("UPDATE users SET ");
        List<Object> parameters = new ArrayList<>();

        if (user.getName() != null) {
            query.append("name = ?, ");
            parameters.add(user.getName());
        }

        if (user.getUsername() != null) {
            query.append("username = ?, ");
            parameters.add(user.getUsername());
        }

        if (user.getEmail() != null) {
            query.append("email = ?, ");
            parameters.add(user.getEmail());
        }

        if (user.getPassword() != null) {
            query.append("password = ?, ");
            parameters.add(user.getPassword());
        }

        if (user.getPhone() != null) {
            query.append("phone = ?, ");
            parameters.add(user.getPhone());
        }

        if (user.getRoleId() != null) {
            query.append("role_id = ?, ");
            parameters.add(user.getRoleId());
        }

        if (user.isActive() != null) {
            query.append("is_active = ?, ");
            parameters.add(user.isActive());
        }

        query.append("updated_at = CURRENT_TIMESTAMP WHERE id = ?");
        parameters.add(user.getId());

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(query.toString())
        ) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseExeption(
                        ResponseCodesEnum.NOT_FOUND,
                        "User not found",
                        null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();

            throw new DatabaseExeption(
                    ResponseCodesEnum.INTERNAL_SERVER_ERROR,
                    "Failed to update user",
                    e
            );
        }

        return user;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getLong("role_id"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        );
    }
}
