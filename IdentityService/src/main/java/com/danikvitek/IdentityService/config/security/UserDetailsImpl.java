package com.danikvitek.IdentityService.config.security;

import com.danikvitek.IdentityService.data.model.entity.User;
import com.danikvitek.IdentityService.data.repository.BannedUserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public final class UserDetailsImpl implements UserDetails {
    private final User user;
    private final BannedUserRepository bannedUserRepository;

    public User getUser() {
        return user;
    }

    @Override
    public @NotNull @Unmodifiable Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = user.getRole().getAuthority();
        return Set.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !bannedUserRepository.existsBannedUserByUserId(user.getId());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
