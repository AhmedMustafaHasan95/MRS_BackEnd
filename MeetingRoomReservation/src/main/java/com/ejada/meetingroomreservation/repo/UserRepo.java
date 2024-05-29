package com.ejada.meetingroomreservation.repo;

import com.ejada.meetingroomreservation.entity.Role;
import com.ejada.meetingroomreservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Modifying
    @Query("UPDATE User u SET u.tokenId = :tokenId WHERE u.id = :id")
    void updateTokenById(String tokenId, long id);

    @Modifying
    @Query("UPDATE User u SET u.tokenId = :tokenId WHERE u.username = :username")
    void updateTokenByUsername(String tokenId, String username);

    @Query("SELECT DISTINCT r FROM User u JOIN u.roles r WHERE u.username = :username")
    List<Role> findRolesByUsername(String username);
}
