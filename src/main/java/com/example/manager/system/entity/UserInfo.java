package com.example.manager.system.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private Long userId;
    private List<String> endpoint;
}
