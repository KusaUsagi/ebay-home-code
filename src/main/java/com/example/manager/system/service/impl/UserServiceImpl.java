package com.example.manager.system.service.impl;

import com.example.manager.system.dto.request.AddUserRequest;
import com.example.manager.system.entity.UserInfo;
import com.example.manager.system.entity.UserInfoJsonArray;
import com.example.manager.system.properties.BizProperties;
import com.example.manager.system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BizProperties bizProperties;

    @Override
    public void addUser(AddUserRequest addUserRequest) {
        try {
            jsonWriter(addUserRequest.getUserId(), addUserRequest.getEndpoint());
        } catch (Exception e) {
            log.error("[{}] error occurred when add user", addUserRequest.getUserId(), e);
        }

    }

    @Override
    public boolean checkResourceAccess(String resourceName, Long userId) {
        try {
            resourceName = URLDecoder.decode(resourceName, "utf-8");
            List<String> accessibleResource = jsonReader(userId);
            return accessibleResource.contains(resourceName);
        } catch (IOException e) {
            log.error("[{}] error occurred when add user", userId, e);
            return false;
        }
    }

    private void jsonWriter(Long userId, List<String> resourceList) throws IOException {
        String relativePath = bizProperties.getResourceFileName();
        String classPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File file = new File(classPath + relativePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        ObjectMapper mapper = new ObjectMapper();
        UserInfoJsonArray array = null;
        List<UserInfo> userInfoList;
        try {
            array = mapper.readValue(file, UserInfoJsonArray.class);
            userInfoList = array.getUserInfoList();
        } catch (MismatchedInputException e) {
            log.error("The file is empty");
            userInfoList = new ArrayList<>();
        }

        boolean modify = false;
        for (UserInfo userInfo : userInfoList) {
            if (Objects.equals(userInfo.getUserId(), userId)) {
                userInfo.setEndpoint(resourceList);
                modify = true;
            }
        }
        if (!modify) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setEndpoint(resourceList);
            userInfoList.add(userInfo);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userInfoList", userInfoList);
        mapper.writeValue(file, data);
    }

    private List<String> jsonReader(Long userId) throws IOException {
        String relativePath = bizProperties.getResourceFileName();
        String classPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File file = new File(classPath + relativePath);
        if (!file.exists()) {
            return Collections.emptyList();
        }
        ObjectMapper mapper = new ObjectMapper();
        UserInfoJsonArray array = mapper.readValue(file, UserInfoJsonArray.class);
        List<UserInfo> userInfoList = array.getUserInfoList();
        for (UserInfo userInfo : userInfoList) {
            if (Objects.equals(userInfo.getUserId(), userId)) {
                return userInfo.getEndpoint();
            }
        }
        return Collections.emptyList();
    }
}
