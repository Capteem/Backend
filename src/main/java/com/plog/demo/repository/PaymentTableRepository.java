package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTableRepository extends JpaRepository<PaymentTable, String> {

    PaymentTable findByPaymentId(String tid);

    List<PaymentTable> findAllByUserId(IdTable userId);
}
