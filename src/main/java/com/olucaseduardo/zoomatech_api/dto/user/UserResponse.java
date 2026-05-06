package com.olucaseduardo.zoomatech_api.dto.user;

import com.olucaseduardo.zoomatech_api.entity.RoleUser;
import com.olucaseduardo.zoomatech_api.entity.User;

public record UserResponse(
        String firstName,
        String lastName,
        String email,
        RoleUser role
) {

    public UserResponse(User user) {
        this(user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

}
