package com.plog.demo.repository;

import com.plog.demo.model.IdTable;
import com.plog.demo.model.PaymentDataTable;
import com.plog.demo.model.PaymentTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PaymentDataTableRepository extends JpaRepository<PaymentDataTable, Integer> {

    List<PaymentDataTable> findAllByPaymentId(PaymentTable paymentId);
}
