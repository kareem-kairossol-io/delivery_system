package io.kairos.delivery_system.modules.users.contracts;

import io.kairos.delivery_system.modules.users.entities.User;

public interface UserRepository {
    User findByEmail(String email);
    User findByPhone(String phone);
    User findByUsername(String username);
    User save(User user);
    User update(User user);
}
