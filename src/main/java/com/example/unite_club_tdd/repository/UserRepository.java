package com.example.unite_club_tdd.repository;

import com.example.unite_club_tdd.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByLoginId(String loginId);

    Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);
}
