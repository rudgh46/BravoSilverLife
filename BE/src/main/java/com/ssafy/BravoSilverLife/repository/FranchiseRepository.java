package com.ssafy.BravoSilverLife.repository;

import com.ssafy.BravoSilverLife.entity.BDCode;
import com.ssafy.BravoSilverLife.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    List<Franchise> findTop3ByCategoryAndNameContainsOrderByCountDesc(String category, String name);
}
