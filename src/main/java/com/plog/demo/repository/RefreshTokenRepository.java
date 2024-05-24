package com.plog.demo.repository;

import com.plog.demo.model.RefreshTokenTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenTable, String> {

}
