package com.marine.customerservice.Repository;

import com.marine.customerservice.Entity.LoginEntity;
import com.marine.customerservice.Entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {
    Optional<UserInfoEntity> findByEmail(String email); // Use 'email' if that is the correct field for login

    UserInfoEntity findByEmailAndPassword(String username, String password);
}