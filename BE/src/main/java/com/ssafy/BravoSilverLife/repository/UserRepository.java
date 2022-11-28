package com.ssafy.BravoSilverLife.repository;

import com.ssafy.BravoSilverLife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
    User findByNickname(String nickname);
    User findByIdAndPassword(String email, String password);

    User findByPassword(String password);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("select u from User u where u.idx=:idx")
    User findByIdx(@Param("idx") Long idx);

    @Query("select u.refreshToken from User u where u.idx=:idx")
    String getRefreshToken(@Param("idx") Long idx);

    @Query("select u from User u where u.refreshToken=:refreshToken")
    User findByToken(@Param("refreshToken") String refreshToken);

}
