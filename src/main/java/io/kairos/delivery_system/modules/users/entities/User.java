package io.kairos.delivery_system.modules.users.entities;

import io.kairos.delivery_system.core.database.BaseEntity;
import io.kairos.delivery_system.core.utilities.HashUtil;
import java.time.Instant;

public class User extends BaseEntity<Long> {
    private final String name;
    private final String username;
    private final String password;
    private final String email;
    private final String phone;
    private final Long roleId;
    private final Boolean isActive;

    // 1. Rehydration Constructor (Used by UserRepository to read SQL rows)
    public User(Long id, String name, String username, String password, String email,
                String phone, Long roleId, Boolean isActive, Instant createdAt, Instant updatedAt) {
        super(id, createdAt, updatedAt);
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.roleId = roleId;
        this.isActive = isActive;
    }

    // 2. Factory Method (Used by Service to create NEW users)
    public static User create(String name, String username, String rawPassword,
                              String email, String phone, Long roleId) {
        Instant now = Instant.now();
        String hashedPassword = HashUtil.make(rawPassword);

        return new User(
                null,
                name, username, hashedPassword, email, phone, roleId, true,
                now, now
        );
    }

    // Entity getters...
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Long getRoleId() { return roleId; }
    public Boolean isActive() { return isActive; }
}