package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.plog.demo.model.ProviderTable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderTableRepository extends JpaRepository<ProviderTable, Integer> {
    Optional<ProviderTable> findByProviderName(String providerName);
    List<ProviderTable> findAllByUserId(IdTable userId);
}
