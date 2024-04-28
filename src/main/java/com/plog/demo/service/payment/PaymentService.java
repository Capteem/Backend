package com.plog.demo.service.payment;

import com.plog.demo.dto.payment.PayApproveResDto;
import com.plog.demo.dto.payment.PayInfoDto;
import com.plog.demo.dto.payment.PayReadyResDto;

public interface PaymentService {
    PayReadyResDto payReady(PayInfoDto payInfoDto);

    PayApproveResDto getApprove(String pgToken, String id) throws Exception;
}
