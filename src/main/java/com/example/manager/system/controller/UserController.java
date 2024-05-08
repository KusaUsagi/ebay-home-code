package com.example.manager.system.controller;

import com.example.manager.system.dto.request.AddUserRequest;
import com.example.manager.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/admin/addUser")
    public boolean addUser(@RequestParam String userId,
                           @RequestParam String accountName,
                           @RequestParam String role,
                           @RequestBody AddUserRequest addUserRequest) {
        if ("admin".equals(role)) {
            userService.addUser(addUserRequest);
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/user/{resource}")
    @ResponseBody
    public boolean checkResourceAccess(HttpServletRequest httpServletRequest,
                                       @PathVariable("resource") String resourceName) {
        Long userId = Long.valueOf(httpServletRequest.getParameter("userId"));
        String role = httpServletRequest.getParameter("role");
        Set<String> params = httpServletRequest.getParameterMap().keySet();
        for (String s: params) {
            System.err.println(s);
        }
        if ("admin".equals(role) || "user".equals(role)) {
            return userService.checkResourceAccess(resourceName, userId);
        } else {
            return false;
        }
    }
}
