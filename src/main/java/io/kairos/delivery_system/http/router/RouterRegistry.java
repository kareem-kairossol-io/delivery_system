package io.kairos.delivery_system.http.router;

import io.kairos.delivery_system.modules.users.controller.UsersController;

public class RouterRegistry {
    private final UsersController usersController;

    public RouterRegistry(UsersController usersController) {
        this.usersController = usersController;
    }
    public void register() {
        Router.addRoute("POST", "/users", usersController::createUser);
        Router.addRoute("PUT", "/users", usersController::updateUser);
    }
}
