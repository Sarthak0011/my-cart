package com.sarthak.mycart.services;

import com.sarthak.mycart.entities.User;
import com.sarthak.mycart.request.CreateUserRequest;
import com.sarthak.mycart.request.UpdateUserRequest;

public interface UserService {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);
}
