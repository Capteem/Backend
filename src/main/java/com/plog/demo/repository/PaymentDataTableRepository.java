package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentDataTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDataTableRepository extends JpaRepository<PaymentDataTable, Integer> {

    List<PaymentDataTable> findAllByUserId(IdTable idTable);
}
