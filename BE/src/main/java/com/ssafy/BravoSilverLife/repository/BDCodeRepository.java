package com.ssafy.BravoSilverLife.repository;

import com.ssafy.BravoSilverLife.entity.BDCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BDCodeRepository extends JpaRepository<BDCode, Long> {

    List<BDCode> findByName(String Name);


    @Query(value = "select distinct gugun from bd_code where sido =\"서울특별시\" and gugun is not null", nativeQuery = true)
    List<String> findDistinctGugun();

    @Query(value = "select name from bd_code where gugun =:gugun and name is not null", nativeQuery = true)
    List<String> findDong(@Param("gugun") String gugun);

}
