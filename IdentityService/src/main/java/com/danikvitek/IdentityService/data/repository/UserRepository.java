package com.danikvitek.IdentityService.data.repository;

import com.danikvitek.IdentityService.data.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("select u from User u join PluginAuthor pa on u.id = pa.userId and pa.pluginId = ?1")
    Set<User> findByAuthoredPlugin(long pluginId);
}