package com.plog.demo.repository;

import com.plog.demo.model.AuthTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthTableRepository extends JpaRepository<AuthTable, String> {
        AuthTable findByEmail(String email);
}
