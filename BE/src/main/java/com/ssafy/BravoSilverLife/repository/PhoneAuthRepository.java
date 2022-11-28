package com.ssafy.BravoSilverLife.repository;

import com.ssafy.BravoSilverLife.entity.PhoneAuth;
import com.ssafy.BravoSilverLife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface PhoneAuthRepository extends JpaRepository<PhoneAuth, Long> {
    PhoneAuth findByPhoneNumber(String phoneNumber);

    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}
