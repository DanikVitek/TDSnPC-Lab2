package com.danikvitek.IdentityService.service.impl;

import com.danikvitek.IdentityService.config.security.UserDetailsImpl;
import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.data.repository.BannedUserRepository;
import com.danikvitek.IdentityService.data.repository.UserRepository;
import com.danikvitek.IdentityService.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public final class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final BannedUserRepository bannedUserRepository;

    @Override
    public @NotNull UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
        return new UserDetailsImpl(user, bannedUserRepository);
    }
}
