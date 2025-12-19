package com.marine.customerservice.Repository;

import com.marine.customerservice.Entity.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Long> {
    @Query("SELECT l FROM LoginEntity l WHERE l.email = ?1 and l.password =?2")
    LoginEntity findByEmailAndPassword(String email, String password);

    LoginEntity findByEmail(String email);
}
