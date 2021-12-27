package com.danikvitek.IdentityService.data.repository;

import com.danikvitek.IdentityService.data.model.entity.BannedUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
    boolean existsBannedUserByUserId(Long userId);
}