package com.example.manager.system.service;

import com.example.manager.system.dto.request.AddUserRequest;

public interface UserService {

    void addUser(AddUserRequest addUserRequest);

    boolean checkResourceAccess(String resourceName, Long userId);
}
