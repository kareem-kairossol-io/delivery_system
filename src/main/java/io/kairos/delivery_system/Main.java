package io.kairos.delivery_system;

import io.kairos.delivery_system.core.database.DataSource;
import io.kairos.delivery_system.core.database.Migration;
import io.kairos.delivery_system.core.database.MigrationRunner;
import io.kairos.delivery_system.core.database.migrations.V1__CreateUsersTable;
import io.kairos.delivery_system.http.HttpServer;
import io.kairos.delivery_system.http.router.RouterRegistry;
import io.kairos.delivery_system.modules.users.contracts.UserRepository;
import io.kairos.delivery_system.modules.users.controller.UsersController;
import io.kairos.delivery_system.modules.users.repositories.JdbcUserRepository;
import io.kairos.delivery_system.modules.users.services.UserService;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        /* Load migration automatically */
        DataSource dataSource = new DataSource(
            "jdbc:mysql://localhost:3306/delivery_system",
            "root",
            "root"
        );

        List<Migration> migrations = List.of(
            new V1__CreateUsersTable()
        );

        MigrationRunner runner =
                new MigrationRunner(dataSource, migrations);

        try {
            runner.migrate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UserRepository userRepository = new JdbcUserRepository(dataSource);
        UserService userService = new UserService(userRepository);
        UsersController usersController = new UsersController(userService);

        RouterRegistry routerRegistry = new RouterRegistry(usersController);
        routerRegistry.register();

        /* start http server */
        HttpServer server = new HttpServer(8080);
        server.start();
    }
}