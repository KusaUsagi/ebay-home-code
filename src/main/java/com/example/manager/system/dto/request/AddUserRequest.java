package com.example.manager.system.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AddUserRequest {

    private Long userId;
    private List<String> endpoint;
}
