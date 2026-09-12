package io.kairos.delivery_system.modules.users.dtos;

public record CreateUserDto(String name, String username, String password, String email, String phone, Long role_id) {
}
