package io.kairos.delivery_system.modules.users.services;

import io.kairos.delivery_system.core.exceptions.ValidationException;
import io.kairos.delivery_system.modules.users.contracts.UserRepository;
import io.kairos.delivery_system.modules.users.dtos.CreateUserDto;
import io.kairos.delivery_system.modules.users.dtos.UpdateUserDto;
import io.kairos.delivery_system.modules.users.dtos.UserDto;
import io.kairos.delivery_system.modules.users.entities.User;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDto save(CreateUserDto createUserDto) {

        // prepare user obj
        User user = User.create(
            createUserDto.name(),
            createUserDto.username(),
            createUserDto.password(),
            createUserDto.email(),
            createUserDto.phone(),
            createUserDto.role_id()
        );

        // validate unique keys
        if (findByEmail(user.getEmail()) != null)
            throw new ValidationException("Email already exists");

        if  (findByPhone(user.getPhone()) != null)
            throw new ValidationException("Phone already exists");

        if  (findByUsername(user.getUsername()) != null)
            throw new ValidationException("Username already exists");

        // save user
        User savedUser = userRepository.save(user);

        return this.mapToDto(savedUser);
    }

    public UserDto update(UpdateUserDto updateUserDto) {
        User user = new User(
            updateUserDto.id(),
            updateUserDto.name(),
            updateUserDto.username(),
            updateUserDto.password(),
            updateUserDto.email(),
            updateUserDto.phone(),
            updateUserDto.role_id(),
            updateUserDto.isActive(),
            null,
            null
        );

        // validate unique keys
        User userByEmail = findByEmail(user.getEmail());
        if (userByEmail != null && !userByEmail.getId().equals(user.getId()))
            throw new ValidationException("Email already exists");

        User userByPhone = findByPhone(user.getPhone());
        if  (userByPhone != null && !userByPhone.getId().equals(user.getId()))
            throw new ValidationException("Phone already exists");

        User userByUsername = findByUsername(user.getUsername());
        if  (userByUsername != null && !userByUsername.getId().equals(user.getId()))
            throw new ValidationException("Username already exists");

        User updatedUser = userRepository.update(user);

        return this.mapToDto(updatedUser);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getPhone(),
            user.getRoleId()
        );
    }
}
