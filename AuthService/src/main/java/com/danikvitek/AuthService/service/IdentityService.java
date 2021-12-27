package com.danikvitek.AuthService.service;

import com.danikvitek.AuthService.api.dto.SimpleUserDto;
import com.danikvitek.AuthService.util.exception.UserNotFoundException;

public interface IdentityService {
    T fetchByEmail(String email) throws UserNotFoundException;
}
