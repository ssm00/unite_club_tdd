package com.example.unite_club_tdd.user.repository;

import com.example.unite_club_tdd.user.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByLoginId(String loginId);

    Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);

    
    //fetch join a.course에 별칭 줘서 true인것만 가져오기 하지 않기 일단 다가져오기(fetch join 별칭 주지 않기)
    @Query("select distinct u from UserEntity u " +
            "left join fetch u.applyList a " +
            "left join fetch a.course c " +
            "where u.userId = :userId")
    UserEntity findByIdWithApply(@Param("userId") Long userId);

}
