package com.ssafy.BravoSilverLife.repository;

import com.ssafy.BravoSilverLife.entity.HDCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface HDCodeRepository extends JpaRepository<HDCode, Long> {

    List<HDCode> findByName(String name);

    @Transactional
    void deleteByCode(int col);

}
