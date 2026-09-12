package io.kairos.delivery_system.modules.users.dtos;

public record UpdateUserDto(Long id, String name, String username, String password, String email, String phone, Long role_id, Boolean isActive) {
}
