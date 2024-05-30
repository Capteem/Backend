package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.plog.demo.model.ProviderTable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderTableRepository extends JpaRepository<ProviderTable, Integer> {
    Optional<ProviderTable> findByProviderName(String providerName);
    List<ProviderTable> findAllByUserIdOrderByProviderIdDesc(IdTable userId);

    Optional<ProviderTable> findByUserId(IdTable idTable);

    @Query("select p from ProviderTable p "
            + "join p.workdateTableList wd "
            + "where p.providerStatus = :providerStatus "
            + "and p.providerPrice != -1")
    List<ProviderTable> findAllByProviderStatus(int providerStatus);

    Optional<ProviderTable> findByUserIdAndProviderId(IdTable idTable, Integer providerId);
    List<ProviderTable> findByUserIdAndProviderStatus(IdTable idTable, int providerStatus);
    ProviderTable findByProviderId(int providerId);

    List<ProviderTable> findAllOrderByProviderIdDesc();


}
