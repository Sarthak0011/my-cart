package com.sarthak.mycart.services.impl;

import com.sarthak.mycart.entities.Cart;
import com.sarthak.mycart.entities.User;
import com.sarthak.mycart.exceptions.AlreadyExistsException;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.repositories.UserRepository;
import com.sarthak.mycart.request.CreateUserRequest;
import com.sarthak.mycart.request.UpdateUserRequest;
import com.sarthak.mycart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d", userId)));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException(String.format("User with email %s", request.getEmail())));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d", userId)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException(String.format("User with id %d", userId));
                });
    }
}
